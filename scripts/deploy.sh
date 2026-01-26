#!/bin/bash
set -e

BLUE_PORT=8080
GREEN_PORT=8081
NGINX_CONF="/etc/nginx/sites-available/default"
IMAGE="jihoonkim501/areumdap-backend:latest"
ENV_FILE="/home/ubuntu/app/.env.prod"
FIREBASE_JSON="/home/ubuntu/app/firebase-service-account.json"

echo "=== Blue-Green Deploy Start ==="

#1 현재 nginx가 바라보는 포트 확인 (없으면 기본 8080)
CURRENT_PORT=$(grep -oP 'set \$service_port \K[0-9]+' $NGINX_CONF | head -n 1)

if [ -z "$CURRENT_PORT" ]; then
  CURRENT_PORT=$BLUE_PORT
fi

if [ -z "$CURRENT_PORT" ]; then
  CURRENT_PORT=$BLUE_PORT
fi

if [ "$CURRENT_PORT" = "$BLUE_PORT" ]; then
  NEW_PORT=$GREEN_PORT
else
  NEW_PORT=$BLUE_PORT
fi

echo "현재 포트: $CURRENT_PORT"
echo "새 포트: $NEW_PORT"

#2 최신 이미지 pull
echo "이미지 pull 중..."
if ! docker pull $IMAGE; then
  echo "이미지 pull 실패"
  exit 1
fi

#3 혹시 남아있는 동일 이름 컨테이너 제거
docker rm -f areumdap-backend-${NEW_PORT} 2>/dev/null || true

#4 새 컨테이너 실행
echo "새 컨테이너 실행 (${NEW_PORT})"
docker run -d \
  --name areumdap-backend-${NEW_PORT} \
  --network areumdap-net \
  -p ${NEW_PORT}:8080 \
  --env-file ${ENV_FILE} \
  -v ${FIREBASE_JSON}:${FIREBASE_JSON}:ro \
  --restart unless-stopped \
  $IMAGE

#5 헬스체크 (재시도 10회)
echo "헬스체크 확인 중..."
SUCCESS=false

for i in {1..10}
do
  if curl -fsS --connect-timeout 1 --max-time 3 http://localhost:${NEW_PORT}/actuator/health > /dev/null 2>&1; then
    SUCCESS=true
    echo "헬스체크 성공"
    break
  fi
  echo "헬스체크 재시도 ($i)"
  sleep 5
done

if [ "$SUCCESS" != "true" ]; then
  echo "헬스체크 최종 실패 → 롤백"
  docker stop areumdap-backend-${NEW_PORT}
  docker rm areumdap-backend-${NEW_PORT}
  exit 1
fi

#6 nginx 포트 전환
echo "nginx 전환 중..."
sudo sed -i "s/set \$service_port .*/set \$service_port $NEW_PORT;/" $NGINX_CONF
sudo nginx -t
if ! grep -q "set \\$service_port $NEW_PORT;" "$NGINX_CONF"; then
  echo "nginx 포트 전환 실패"
  docker stop areumdap-backend-${NEW_PORT} 2>/dev/null || true
  docker rm areumdap-backend-${NEW_PORT} 2>/dev/null || true
  exit 1
fi
sudo systemctl reload nginx

#7 기존 컨테이너 종료
echo "기존 컨테이너 종료 (${CURRENT_PORT})"
docker stop areumdap-backend-${CURRENT_PORT} 2>/dev/null || true
docker rm areumdap-backend-${CURRENT_PORT} 2>/dev/null || true

#8 사용하지 않는 이미지 정리
echo "이미지 정리"
docker image prune -f

echo "=== 배포 완료 (포트: $NEW_PORT) ==="
