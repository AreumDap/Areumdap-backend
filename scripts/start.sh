#!/bin/bash
set -e

CONTAINER_NAME=areumdap-backend
IMAGE=jihoonkim501/areumdap-backend:latest

echo "Starting new container..."

docker pull $IMAGE

docker run -d \
  --name $CONTAINER_NAME \
  -p 8080:8080 \
  --restart always \
  $IMAGE

