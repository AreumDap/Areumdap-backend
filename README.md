# Areumdap-BackEnd

2025.12.22 ~ 2026.2.20  
UMC 9th 프로젝트 아름답 - Backend

---

## 📌 Git Convention

### (1) Branch type

- 기본 브랜치
    - **main**
        - 배포 및 최종 결과물 브랜치
        - 직접 커밋 ❌
    - **develop**
        - 개발 통합 브랜치
        - 기능 개발 완료 후 PR을 통해 병합

- 작업 브랜치
    - 새로운 기능 구현  
      - `feat/#이슈번호`
    - 버그 및 오류 해결  
      - `fix/#이슈번호`
    - 리팩토링 (기능 변경 없음)  
      - `refactor/#이슈번호`
    - 긴급 수정  
      - `hotfix/#이슈번호`


### (2) COMMIT 메시지

- 형식 
  - `[Type] #이슈번호 작업내용`
- 규칙
  - 커밋은 하나의 목적 단위로 작성
  - 불필요하게 여러 작업을 한 커밋에 포함 ❌
  - 메시지는 한글로 작성
- 예시
  - `[Feat] #2 회원가입 API 구현`
  - `[Fix] #3 로그인 토큰 재발급 오류 수정`
  - `[Refactor] #4 UserService 로직 분리`


### (3) Pull Request

- PR 생성 규칙
  - **develop 브랜치로만 PR 생성**
  - PR 제목은 Issue 제목과 동일하게 작성
- PR 내용
  - 작업 내용 요약
  - 관련 이슈 번호 명시
  - API 변경 시 Swagger 캡처 첨부
- 병합 규칙
  - 최소 **2명 리뷰 필수**
  - 충돌 발생 시 PR 생성자가 직접 해결


### (4) Issue 관리

- 모든 작업은 Issue 생성 후 진행
- 기능 단위로 Issue 분리
- 작업 브랜치는 Issue 번호 기반으로 생성
- Issue 제목 예시
  - `[Feat] 회원가입 API 구현`
  - `[Fix] 이메일 인증 만료 오류`
  - `[Refactor] 인증 로직 구조 개선`

---

## 🧩 Coding Convention

### (1) 프로젝트 구조
```
src
└─ main
   ├─ common
   │  ├─ exception
   │  ├─ response
   │  ├─ status
   │  │  ├─ error
   │  │  └─ success
   │  ├─ base
   │  ├─ config
   │  ├─ jwt
   │  └─ properties
   │
   └─ domain
      └─ example-domain
         ├─ controller
         ├─ service
         ├─ repository
         ├─ entity
         ├─ enums
         └─ dto
            ├─ request
            └─ response
```
- CQRS 패턴 적용
- Service는 인터페이스 없이 단일 구현체


### (2) 네이밍

- 클래스 / 컴포넌트
    - PascalCase  
      `UserController`, `UserService`
- 메서드 / 변수
    - camelCase  
      `getUserProfile()`, `userId`
- 상수
    - UPPER_SNAKE_CASE  
      `MAX_TOKEN_EXPIRE_TIME`
- Entity: 단수형
- Table: 복수형
- API Path: kebab-case


### (3) 코드 스타일

- DTO는 `record` 사용 및 한 파일에 하나만 선언
- Repository는 `Optional<T>` 반환, Service 계층에서 null이면 즉시 예외 처리
- Service 클래스 최상단에 `@Transactional`


### (4) 공통 응답 포맷

```json
{
  "isSuccess": true,
  "code": "AREUMDAP_200",
  "message": "성공입니다.",
  "data": {}
}
```

### (5) 예외 처리

- `@RestControllerAdvice` 사용
- 모든 커스텀 예외는 `BaseStatus` 기반으로 정의
- HTTP Status 명확히 구분하여 반환

### (6) 코드 리뷰 기준

- 네이밍이 직관적인가
- 각 클래스와 메서드의 책임이 명확한가
- 중복 코드가 없는가
- 컨벤션이 지켜졌는가
- 리팩토링 여지가 있는가
