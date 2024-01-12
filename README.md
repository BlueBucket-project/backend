# BlueBuket 중고마켓 프로젝트

## 프로젝트 설명

각 지역에 오프라인 센터가 있다는 가정하에 판매자가 오프라인 센터에 판매자가 물건을 가서 등록을 하면 오프라인 센터가 물건을 검수하여 사이트에 물건에 대한 정보와 이미지를 올려놓으면 구매자는 사이트를 둘러보다 원하는 물건이 있으면 예약을 걸어놓고 현장에서 구매한다. 당근마켓의 직거래가 `일대일`관계라면 이 사이트 컨셉은 판매자와 구매자 사이에 껴서 거래를 순조롭게 도와주는 컨셉이다. 즉 쇼핑몰보다는 중고마켓과 같은 느낌입니다. 

## 중고마켓 컨셉을 고른 이유

처음에 쇼핑몰을 구현할 것인지 중고마켓을 구현할 것인지 고민을 많이 했습니다. 고민 끝에 중고마켓을 선택한 이유는 기존에 쇼핑몰들은 많은 반면에 중고마켓 같은 사이트는 별로 없고 요즘 젊은 사람들은 물건을 고장날 때 까지 사용하는 것이 아니라 사용하다 중고로 판매하고 새로운 제품을 구매하거나 중고로 구매를 많이해서 사용하는데 기존에 있는 중고관련 사이트들은 판매자와 구매자간의 거래를 책임지지 않는 모습들이 있고 일대일 거래에서 문제가 생기는 경우가 많습니다. 그래서 평소에 생각하던 있었으면 좋겠다고 생각하던 중고마켓을 선택하게 되었습니다. 

---
# 링크
## GitHub 주소
[코드보러 가기](https://github.com/orgs/BlueBucket-project/repositories)
## Velog 주소
[프로젝트 블로그](https://velog.io/write?id=32ca9a46-42b9-41da-bcc2-28eb45cec5c6)

---
# 백엔드 소개

  |          유요한         |       오현진                                                                                                                          
|:---------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------------------: |
 |                   <img width="160px" src="https://avatars.githubusercontent.com/u/110465572?v=4" />    |                 <img width="160px" src="https://avatars.githubusercontent.com/u/84078029?v=4"/>   |  [@YuYoHan](https://github.com/YuYoHan)  | [@hyeonjin-OH](https://github.com/jDaHyun)  |
 | Back-end | Back-end | 
 
 프론트는 별도로 리드미를 꾸밀거기 때문에 프론트에 대해서는 작성하지 않았습니다. 
 
 ## 소통 방법
 
 오프라인으로 모였고 정기적인 시간을 정해서 진행하고 질문이 있거나 긴급히 모여야할 때는 별도로 모여서 회의를 진행하고 수정되어야 하는 부분이 있으면 서로 회의를 거쳐서 피드백을 받고 하는 등 한명의 의견을 무시하지 않고 진행했습니다.
 
 **소통하는 도구**
 - 노션
[노션링크](https://www.notion.so/18bcdec9d73a4b12a811bdffdbc73cc7?pvs=4)
> 노션으로 회의를 진행한 것을 작성했고 프론트에게 의견을 제시할 때 기록하는 용으로 사용했으며 프론트와 백엔드 파트를 나누는 용으로 사용했습니다. 그리고 배포전 프론트가 API문서화를 볼 수 있게 스샷으로 남기고 후에 배포해서 API문서를 직접 볼 수 있게 했습니다.
- 디스코드
 
---
## 화면 구성 (일반 유저)📺
| 로그인 | 회원가입  | 회원 수정 | 회원 탈퇴 |
| :-------------------------------------------: | :----------------------: | :---------------------:| :--------------------------:|
|  <img width="220" src="https://velog.velcdn.com/images/zxzz45/post/1830e0c9-4d46-4a75-8144-034542b831c1/image.gif"/> |  <img width="220" src="https://github.com/BlueBucket-project/backend/assets/110465572/988d6155-3515-466d-9bb3-456377450b21"/>| <img width="220" src="https://github.com/BlueBucket-project/backend/assets/110465572/8bf8d64a-2034-4175-af95-701994d8c213"/> | <img width="220" src="https://velog.velcdn.com/images/zxzz45/post/2f373513-6bf1-436d-b7a8-20570c58f78c/image.gif"/>|
| 장바구니 등록  |장바구니 수정 | 장바구니 삭제 | 문의글 등록 |
| <img width="220" src="https://velog.velcdn.com/images/zxzz45/post/29354694-561b-4c8f-8548-e65a85fdbb8e/image.gif"/> | <img width="220" src="https://velog.velcdn.com/images/zxzz45/post/dbcccaf7-da0f-4753-a68e-518436ad9fff/image.gif"/> | <img width="220" src="https://github.com/BlueBucket-project/backend/assets/110465572/074ad4c4-8de1-4d51-888a-0e9f5b29d327"/> |<img width="220" src="https://github.com/BlueBucket-project/backend/assets/110465572/3d089649-8f2c-4715-bbd5-2c1d970201ee"/> |
| 문의글 수정 | 문의글 삭제 | 상품 등록 | 상품 수정
| <img width="220" src="https://velog.velcdn.com/images/zxzz45/post/ab5e6f65-1bdf-4bdc-8c85-53ecdcdfac06/image.gif"/>   |  <img width="220" src="https://velog.velcdn.com/images/zxzz45/post/36f16440-b284-4aee-8176-a23237ab7038/image.gif"/> |  <img width="220" src="https://velog.velcdn.com/images/zxzz45/post/e7c6bbcb-4b52-41f3-bac4-291b50792c10/image.gif"/> | <img width="220" src="https://velog.velcdn.com/images/zxzz45/post/b0b92bca-daa9-4bdf-b152-e5c04b1566a2/image.gif"/>|
|  상품 삭제 | 상품 주문 탭 | 문의 관리 - 문의 수정 | 문의 관리 - 삭제 |
|   <img width="220" src="https://velog.velcdn.com/images/zxzz45/post/3f519de4-c38a-428f-b4b5-3e73e7c9548c/image.gif"/>  |  <img width="220" src="https://velog.velcdn.com/images/zxzz45/post/6ce41364-8959-41d0-bf9f-9423df47a093/image.gif"/>  | <img width="220" src="https://velog.velcdn.com/images/zxzz45/post/34ec3ca9-5cca-444d-ae93-ed1ecafc5dcf/image.gif"/>  | <img width="220" src="https://velog.velcdn.com/images/zxzz45/post/75c320f2-7f82-4ee6-bac8-462c6885d1c7/image.gif"/>  | 
| 검색 |
|  <img width="220" src="https://github.com/BlueBucket-project/backend/assets/110465572/f36c3843-6079-4152-8d8d-095cdf71adbe"/> | 

---
# ERD
![image](https://github.com/BlueBucket-project/backend/assets/110465572/22fdbe5b-f56a-4432-aa15-a440b6a73af4)

---

# 프로젝트 세부 진행

[프로젝트 파악하기](https://velog.io/@zxzz45/%EB%B0%B0%ED%8F%AC-c39fncia) <br />
여기서는 프로젝트를 코드와 설명으로 어떻게 진행하고 왜 그렇게 했는지 설명하면서 진행할 생각입니다. 이곳에는 프로젝트를 진행하면서 만났던 문제들도 기록하고 있습니다.

---
# 역할 분담

## 회원(유요한)

- 회원가입
- 로그인
>JWT 반환
- OAuth2 로그인
> JWT 반환
- 로그아웃
- 마이페이지
- 회원 수정
- 회원 탈퇴
- 주문내역 조회
> 회원페이지에서 구매/판매 내역을 조회할 수 있게 한다.
- 나의 문의 보기


## 상품문의(유요한)

- 상품 문의 작성 기능
- 상품 문의 수정
- 상품 문의 게시글 삭제
- 상품 문의 게시글 전체 보기(해당 상품만)
- 상품 안에 있는 문의글은 게시글 형태

## 상품(오현진 & 유요한)

- 상품 등록 (유요한)
- 상품 수정 (유요한)
- 상품 삭제 (오현진) 
- 전체 상품 가져오기 (유요한)
> 조건에 따라서 상품 검색, 가격 검색, 지역 검색, 그리고 상태에 따라서 을 페이지처리해서 가져옵니다.

## 장바구니 기능(오현진)
- 장바구니 기능 
>- 유저가 상품을 장바구니에 담는다.
>- 유저가 주문 수량을 조절한다. 하지만 1개인 상품은 조절이 불가능하다.
>- 주문하기를 누른다.
>- Item의 상태가 `예약`으로 바뀐다.
- 장바구니 물건 삭제기능
- 일부삭제 및 전체삭제 구현
- 장바구니 수량 수정
- 주문하기
>- 위에서 `예약`으로 바뀐 상품을 주문(예약)해준다.
>- 주문한 수량에 따라 Item의 `stockNumber`을 줄여준다.
>- 예약한 상품과 유저를 등록해준다.
>- 1개인 상품을 주문했다고 하면 상품의 상태를 `SOLD_OUT`바꿔준다.
>- 수량이 남아 있으면 계속 `SELL`
- 주문 취소
- 상품 전체 보기  
- 상품 검색 기능  
- 상품 상세 페이지 

## 이미지 넣기 기능(유요한)
- S3 이미지 넣기

## 관리자(유요한 & 오현진)
- 상품 문의 답변 기능 (유요한)
- 관리자가 상품 삭제 (유요한)
- 관리자가 게시글 삭제 (유요한)
- 관리자가 모든 상품 문의 전체 보기(펼쳐보기) (유요한)
- 관리자가 특정 유저의 문의글을 가져온다. (유요한)
- 관리자가 상품을 최종 구매 확정하는 기능 (오현진)
- 관리자 회원가입
> 관리자 회원가입의 경우 이메일에 인증번호를 보내주는데 인증번호를 인증해야 합니다.
- 관리자가 상품 관리를 위한 전체 상품 가져오기 (유요한)
> 조건에 따라서 상품 검색, 가격 검색, 지역 검색, 그리고 상태에 따라서 을 페이지처리해서 가져옵니다.

## Swagger(유요한)

- swagger 기능

## 예외처리(유요한)

- 전체 예외처리, 유저를 못 찾을 경우 발생하는 예외 처리
- 게시글을 못 찾을 경우 발생하는 예외 처리
- 상품을 못 찾을 경우 발생하는 예외 처리
- 검증 오류 예외 처리
- 인증 예외 처리
- 파일 업로드 예외 처리
- 파일 다운로드 예외 처리
- 외부 서비스 예외 처리
- 서비스 로직 예외 처리
- JWT 권한 예외 처리
- JWT 인증 예외 처리

## Spring jacoco(유요한)

- jacoco기능

## 배포(유요한 & 오현진)

- EC2
- RDS
- Git action
- S3

## ERD(유요한 & 오현진)

- ERD 작성

---
# 발생한 문제

여기서는 단순 실수로 인한 오류를 제외하고 공부에 도움이 되었던 오류를 정리하겠습니다.

## JWT 생성시 암호화 오류
로컬에서 이미지를 S3에 올리려고 할 때 에러가 발생
JWT를 만들어 주려고 하는데 암호화하는데 지속적으로 오류가 발생했습니다.
```
ECDSA signing keys must be PrivateKey instances.
```

### 해결방법
```java
    private Key key;

    public JwtProvider(
            @Value("${jwt.secret_key}") String secret_key
    ) {
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secret_key);
        this.key = Keys.hmacShaKeyFor(secretByteKey);
    }
 ```

## 2. 소셜 로그인시 JWT 반환
 
 소셜 로그인을 성공하면 JWT를 반환해주는 로직을 작성하려고 했는데 계속 컨트롤러에서 값을 가지고 오는 방식이 실패를 해서 JWT를 반환하지 못했습니다. <br/>
 그래서 여러 방법을 고민하던 와중에 프론트가 소셜 로그인을 해서 성공하면 서버에서 정보를 받아와서 바로 HTTP body에 넣어서 프론트에 반환해주는 방법을 사용했습니다.
 ```java
@Log4j2
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    // Jackson ObjectMapper를 주입합니다.
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            log.info("OAuth2 Login 성공!");
            String email = authentication.getName();
            log.info("email : " + email);
            TokenEntity findToken = tokenRepository.findByMemberEmail(email);
            log.info("token : " + findToken);
            MemberEntity findUser = memberRepository.findByEmail(email);
            // 헤더에 담아준다.
            response.addHeader("email", findToken.getMemberEmail());

            // 바디에 담아준다.
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("providerId", findUser.getProviderId());
            responseBody.put("accessToken", findToken.getAccessToken());
            responseBody.put("refreshToken", findToken.getRefreshToken());
            responseBody.put("email", findToken.getMemberEmail());

            // JSON 응답 전송
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        } catch (Exception e) {
            // 예외가 발생하면 클라이언트에게 오류 응답을 반환
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("OAuth 2.0 로그인 성공 후 오류 발생: " + e.getMessage());
            response.getWriter().flush();
        }
    }
}
```
```java
        http
                // oauth2Login() 메서드는 OAuth 2.0 프로토콜을 사용하여 소셜 로그인을 처리하는 기능을 제공합니다.
                .oauth2Login()
                // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정 담당
                .userInfoEndpoint()
                // OAuth2 로그인 성공 시, 후작업을 진행할 서비스
                .userService(principalOAuth2UserService)
                .and()
                .successHandler(oAuth2SuccessHandler);
   ```

## 3. Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Handler dispatch failed; nested exception is java.lang.StackOverflowError] with root cause

두개의 객체에서 `@ToString`을 사용해서 무한 순환 참조가 발생한 오류였습니다. 이 오류가 발생하는 이유는  메소드 호출 스택이 너무 깊게 들어가서 스택 메모리를 초과했을 때 발생하는 오류입니다. 이 오류는 주로 재귀 호출이 무한으로 이어지거나 메소드가 스택에 계속해서 쌓이는 경우에 발생합니다. 

@ToString 애너테이션은 Lombok 프로젝트에서 제공하는 애너테이션 중 하나로, 해당 클래스의 toString() 메소드를 자동으로 생성해주는 기능을 제공합니다. 그러나 이 애너테이션을 사용할 때 주의해야 할 사항이 있습니다. 만약 클래스 간에 서로 참조하는 관계가 있다면, toString() 메소드에서 이를 출력하려고 할 때 무한 루프에 빠질 수 있습니다. 예를 들어, 클래스 A가 클래스 B를 참조하고, 클래스 B가 다시 클래스 A를 참조하는 경우가 있을 때, 두 클래스에 @ToString 애너테이션이 적용되면 서로를 계속해서 출력하려고 하다가 스택 오버플로우가 발생할 수 있습니다.

따라서 엔티티에 @ToString을 사용하는 것을 지양하거나 사용해야 한다면 exclude를 추가하여 사용하자. 저 같은 경우 엔티티에서 로그를 찍을 일이 있으면 @ToString에서 엔티티를 제외해서 무한 참조가 일어나지 않게 하고 아니면 @ToString을 제외했습니다.

## 4. 댓글 등록시 N+1문제
댓글 등록시 N+1문제가 발생했습니다. 이 오류로 JPA 연관관계에 대해 더 공부가 되었습니다.
문의글 엔티티와 댓글 엔티티 사이에 양방향 연관관계를 맺고 `cascade = CascadeType.ALL`을 했기 때문에 댓글만 저장해도 리스트에 올라가는데 리스트에도 올려주고 저장도 했기 때문에 `N+1문제` 발생했습니다.
그래서 리스트에 추가하는 것을 제외하고 댓글 저장만 처리하도록 처리했습니다.

## 5. 게시글 수정 에러

**발생한 에러**
```
"message": "A collection with cascade=\"all-delete-orphan\" was no longer referenced by the owning entity instance: com.example.shopping.entity.board.BoardEntity.commentEntityList",
        "suppressed": [],
        "localizedMessage": "A collection with cascade=\"all-delete-orphan\" was no longer referenced by the owning entity instance: com.example.shopping.entity.board.BoardEntity.commentEntityList"
    },
    "suppressed": [],
    "localizedMessage": "A collection with cascade=\"all-delete-orphan\" was no longer referenced by the owning entity instance: com.example.shopping.entity.board.BoardEntity.commentEntityList; nested exception is org.hibernate.HibernateException: A collection with cascade=\"all-delete-orphan\" was no longer referenced by the owning entity instance: com.example.shopping.entity.board.BoardEntity.commentEntityList"
```
문제의 원인은 BoardEntity와 CommentEntity 사이의 양방향 관계 설정에서 발생하고 있습니다. 현재의 설정에서는 @OneToMany 어노테이션에 cascade = CascadeType.ALL와 orphanRemoval = true가 적용되어 있습니다. 이로 인해 게시글을 저장할 때 댓글들도 함께 저장되며, 게시글 엔티티와 댓글 엔티티 사이의 연관관계가 맺어집니다.

문제는 게시글을 업데이트할 때 해당 게시글의 댓글 리스트가 변경되었음에도 불구하고 Hibernate가 이를 감지하지 못하고 있다는 것입니다. 게시글과 댓글 간의 연관관계에서 댓글 엔티티들이 올바르게 관리되지 않았을 때 발생할 수 있는 문제입니다.

**문제 해결**
정말 간단한 실수였습니다. 

```java
  findBoard = BoardEntity.builder()
                        .boardId(findBoard.getBoardId())
                        .title(boardDTO.getTitle() != null ? boardDTO.getTitle() : findBoard.getTitle())
                        .content(boardDTO.getContent() != null ? boardDTO.getContent() : findBoard.getContent())
                        .item(findBoard.getItem())
                        .member(findBoard.getMember())
                        .boardSecret(BoardSecret.UN_LOCK)
                        .commentEntityList(findBoard.getCommentEntityList())
                        .build();
 ```
 여기서 빌드처리를 하는데 댓글 리스트부분을 빼먹은 거였습니다. 
 하루종일 각종 디버깅과 로그로 테스트해봤는데 엄청 사소한 실수였습니다.

 ## 6. fetch join시 페이지 처리 문제

JPQL로 fetch Join을 사용하려고 했는데 에러가 발생했습니다. <br/>
페이징은 count로 게시글이 몇개인지 찾고 그에 맞는 데이터 즉, 10개만 찾아오면 10개만 찾아주는 역할을 하는데 fetch join은 count가 아니라 필요한 것을 모두 데이터로 가져오는 것이기 때문에 에러가 발생합니다. CountQuery를 정상적으로 자동으로 만들어주지 못한다. 아마 그래서 발생하는 문제인 것같습니다.

**해결방법**
```java
 @Query(value = "select b from board b" +
            " join fetch b.member " +
            " join fetch b.item " +
            " order by b.boardId DESC ",
            countQuery = "select count(b) from board b")
    Page<BoardEntity> findAll(Pageable pageable);

  @Query(value = "select  b from board  b " +
            " join fetch b.member " +
            " join fetch b.item " +
            " where b.member.email = :email and b.title like %:searchKeyword%" +
            " order by b.boardId DESC ",
            countQuery = "select count(b) from board b " +
                    "where b.member.email = :email and b.title like %:searchKeyword%")
    Page<BoardEntity> findByMemberEmailAndTitleContaining(@Param("email") String email,
                                                          Pageable pageable,
                                                          @Param("searchKeyword") String searchKeyword);
```        
## 7. could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement 에러

**발생 이유**
유저를 회원 탈퇴하려고 할 때 다른 엔티티에서 연관관계를 맺고 있어서 SQL상으로는 외래 키(FK)로 이어져 있어서 무결성이 깨져서 이런 오류가 발생했습니다.

**해결 방법1**
유저와 연관된 엔티티를 양방향으로 묶어주고 `cascade`로 해줘서 JPA에서 회원이 삭제되면 연관된 엔티티를 삭제하게 한다.

**해결 방법2**
유저와 연관된 엔티티를 단방향으로 처리하고 각각의 엔티티를 삭제해준다.

저같은 경우는 방법2를 사용했습니다. 그 이유는 양방향은 필요한 경우를 제외하고는 단방향으로 처리하고자 했기 때문입니다. 현재 프로젝트에서는 유저가 양방향 연관관계를 맺을 필요가 없기 때문에 `단방향`으로 처리했습니다. 

 ---
 # 프로젝트 세부 진행
## 유저 기능

### 메인페이지(비로그인) 

![](https://velog.velcdn.com/images/zxzz45/post/47376a57-7459-47ea-921e-76d5a1bef9c9/image.png)



### 회원가입

![](https://velog.velcdn.com/images/zxzz45/post/ab987016-5939-4bdd-8cb7-75f79da51d4c/image.png)
![회원가입](https://github.com/BlueBucket-project/backend/assets/110465572/3d19209c-e933-416a-a43a-aa545b8ca2c6)



### 로그인

![](https://velog.velcdn.com/images/zxzz45/post/32e4a1ef-3260-46b0-a9a7-17dcf212e456/image.png)

![](https://velog.velcdn.com/images/zxzz45/post/1830e0c9-4d46-4a75-8144-034542b831c1/image.gif)

### 메인 페이지(로그인 - 유저)

![](https://velog.velcdn.com/images/zxzz45/post/b8908a5f-3251-4e93-ac07-fe6ca1518296/image.png)

### 검색

![](https://velog.velcdn.com/images/zxzz45/post/4e39ea4f-8ece-475e-b4a3-74858a758079/image.gif)

검색은 처음에는 상품 이름으로 검색하고 그 후에 상세 검색을 통해서 가격, 지역을 설정해서 검색할 수 있습니다.

### 장바구니
**장바구니 등록**
![](https://velog.velcdn.com/images/zxzz45/post/44c516cd-7915-46a1-a49c-79fc9c594e18/image.png)


**장바구니**
![](https://velog.velcdn.com/images/zxzz45/post/a41dfd98-1c07-4f0c-a0c3-03d4ec422ac8/image.png)

![](https://velog.velcdn.com/images/zxzz45/post/4ca3f890-8254-4bcf-b106-c0e85f934ad7/image.png)

![](https://velog.velcdn.com/images/zxzz45/post/29354694-561b-4c8f-8548-e65a85fdbb8e/image.gif)

**수정**
![](https://velog.velcdn.com/images/zxzz45/post/dbcccaf7-da0f-4753-a68e-518436ad9fff/image.gif)
**삭제**
![장바구니삭제](https://github.com/BlueBucket-project/backend/assets/110465572/cd7b44ce-dfd5-4e93-b805-4ced96ccffdc)

**예약중인 상품 처리**
![](https://velog.velcdn.com/images/zxzz45/post/b645ec18-6816-4d89-b5c2-8066558b36e6/image.png)

![](https://velog.velcdn.com/images/zxzz45/post/f0afa4ba-e543-4e68-a86e-e94c02927f5a/image.gif)

![](https://velog.velcdn.com/images/zxzz45/post/f24cdfea-ff38-493e-abf6-7d74edcf763a/image.gif)


### 마이 페이지

![](https://velog.velcdn.com/images/zxzz45/post/0a5c9aa0-df89-45a3-8754-57ebb7c6dc0c/image.png)

![](https://velog.velcdn.com/images/zxzz45/post/4dfc629a-0cce-4863-a3b6-e9786581f9bb/image.png)

여기서는 본인이 여태 구매한 이력을 볼 수 있고 본인이 무슨 문의글을 작성했는지 볼 수 있고 답변이 달렸는지 확인할 수 있습니다. 문의글을 클릭하면 해당 문의글을 수정할 수 있고 상품 정보를 클릭하면 해당 문의글을 작성한 상품으로 이동합니다.

**회원정보 수정**
![](https://velog.velcdn.com/images/zxzz45/post/3acee2e3-2bab-4944-970f-5fa9f232ddd6/image.png)

![](https://velog.velcdn.com/images/zxzz45/post/a8942091-de20-4dc6-9aa2-11063ab1675a/image.gif)

**회원탈퇴**
![](https://velog.velcdn.com/images/zxzz45/post/e3e81bbf-6498-44b2-8439-9c20c1e62737/image.png)
![회원탈퇴](https://github.com/BlueBucket-project/backend/assets/110465572/80dc7bef-8350-46d5-b206-17c2488fb505)



### 문의글

**문의글 등록 & 수정 & 삭제**

![문의글등록](https://github.com/BlueBucket-project/backend/assets/110465572/51c11c88-b7db-4456-bc5d-238f60f42478)

![](https://velog.velcdn.com/images/zxzz45/post/738d799d-5dde-477e-a74d-a14ac9ecb86f/image.gif)

![](https://velog.velcdn.com/images/zxzz45/post/ab5e6f65-1bdf-4bdc-8c85-53ecdcdfac06/image.gif)

![](https://velog.velcdn.com/images/zxzz45/post/36f16440-b284-4aee-8176-a23237ab7038/image.gif)


## 관리자 기능
### 메인 페이지(로그인 - 관리자)
![](https://velog.velcdn.com/images/zxzz45/post/bac16e92-8776-4c74-bd04-4af3ef338924/image.png)


### 상품

**상품 등록**
![](https://velog.velcdn.com/images/zxzz45/post/224873b0-7b06-4906-b100-03870b8246fb/image.png)

![](https://velog.velcdn.com/images/zxzz45/post/e7c6bbcb-4b52-41f3-bac4-291b50792c10/image.gif)

**상품 상세 페이지**
![](https://velog.velcdn.com/images/zxzz45/post/d7708f49-ff97-406d-81db-8a1d90766da1/image.png)


**상품 수정**
![](https://velog.velcdn.com/images/zxzz45/post/99111e3e-5129-43e9-8209-9f67c23cb1f3/image.png)


![](https://velog.velcdn.com/images/zxzz45/post/b0b92bca-daa9-4bdf-b152-e5c04b1566a2/image.gif)

**상품 삭제**
![](https://velog.velcdn.com/images/zxzz45/post/3f519de4-c38a-428f-b4b5-3e73e7c9548c/image.gif)


### 상품 주문 탭
![](https://velog.velcdn.com/images/zxzz45/post/ed561cc5-e558-4a48-bd08-360788d5ec23/image.png)

![](https://velog.velcdn.com/images/zxzz45/post/6ce41364-8959-41d0-bf9f-9423df47a093/image.gif)

### 상품 관리
![](https://velog.velcdn.com/images/zxzz45/post/96a38e0a-c777-4322-b5d5-d77f4c370571/image.png)



### 문의 관리
![](https://velog.velcdn.com/images/zxzz45/post/7e3dea0a-c359-4849-9d70-1a5e235d5452/image.png)

![](https://velog.velcdn.com/images/zxzz45/post/34ec3ca9-5cca-444d-ae93-ed1ecafc5dcf/image.gif)

![](https://velog.velcdn.com/images/zxzz45/post/75c320f2-7f82-4ee6-bac8-462c6885d1c7/image.gif)
 

## 사용한 기술 및 라이브러리
![SpringBoot](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=SpringBoot&logoColor=black) 
<img src="https://img.shields.io/badge/JPA-brown?style=flat-square&logo=JPA&logoColor=white"> 
<img src="https://img.shields.io/badge/springsecurity-6DB33F?style=flat-square&logo=springsecurity&logoColor=white"> 
<img src="https://img.shields.io/badge/springOAuth2-black?style=flat-square&logo=springOAuth2&logoColor=white"> 
<img src="https://img.shields.io/badge/JWT-purple?style=flat-square&logo=JWT&logoColor=white"> 
<br/>
<img src="https://img.shields.io/badge/mysql-4479A1?style=flat-square&logo=mysql&logoColor=white"> 
<img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white">
<img src="https://img.shields.io/badge/JaCoCo-181717?style=flat-square&logo=JaCoCo&logoColor=white"> 
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=Swagger&logoColor=white"> 
<img src="https://img.shields.io/badge/QueryDsl-blue?style=flat-square&logo=QueryDsl&logoColor=white"> 
<img src="https://img.shields.io/badge/Spring Actuator-green?style=flat-square&logo=Spring Actuator&logoColor=white"> 
<br/>

#### 배포
<img src="https://img.shields.io/badge/AWS-232F3E?style=flat-square&logo=AWS&logoColor=white">
<img src="https://img.shields.io/badge/EC2-FF9900?style=flat-square&logo=EC2&logoColor=white">
<img src="https://img.shields.io/badge/RDS-527FFF?style=flat-square&logo=RDS&logoColor=white"> 
<img src="https://img.shields.io/badge/github Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white">
<br/>

#### 협업
<img src="https://img.shields.io/badge/github-181717?style=flat-square&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/git-F05032?style=flat-square&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/notion-000000?style=flat-square&logo=notion&logoColor=white">

#### 사용 IDE
<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=flat-square&logo=IntelliJIDEA&logoColor=white">

## 후기

중고마켓 프로젝트를 진행하면서 느낀점은 1차 프로젝트에서 진행했을 때보다 더욱 공부하니 프로젝트를 진행하는데 더욱 매끄러웠습니다. 무슨 에러를 만났을 때 구글 검색 등으로 그 문제를 해결하는데 어떻게 해결해야하는지 금방 감이 왔습니다. 에러코드를 보는 것에 익숙해졌습니다. 1차 프로젝트 때에는 에러가 발생하면 왜 안되지라고 짜증이 났었지만 2차 프로젝트를 진행하면서 바뀐점은 짜증보다는 `왜 안될까?`라는 고민을 하고 여러가지의 방식으로 진행해보고 찾는 과정을 겪으면서 느낀점은 아 코드는 상황에 따라서 똑같은 코드라도 안될 수 있구나라고 생각이 들었고 개발자는 그 상황에 따라 맞춰서 개발할 수 있어야 겠다는 것을 느꼈습니다.

1차 프로젝트는 단순히 MyBatis로 블로그 형식을 구현했다면 2차 프로젝트는 평소에 생각하던 이런 사이트가 있으면 좋을텐데라고 생각한 사이트를 구현했습니다. 물론 제가 리더라서 무작정 정하기 보다는 프로젝트를 기획할 때 팀원들에게 프로젝트의 컨셉을 말하고 `왜`하고 싶은지 구체적으로 설명해서 현재 프로젝트를 하게 되었습니다. 

>**JPA로 진행하면서 느낀점**
>
MyBatis가 자바에서 MySQL 쿼리문으로 작성했지만 1차 프로젝트를 진행할 때 떠오른 생각은 <span style="color: #d81b60">그러면 만약 회사에 따라서 사용하는 DB가 다를텐데 `의존적`이지 않나?</span> 라는 생각을 하게 되었습니다. 그래서 2차 프로젝트때는 `JPA`를 선택하게 되었습니다. JPA를 사용하면서 느낀점은 편하다였습니다. 사용법과 조심해야할 부분을 숙지하고 있다면 JPA가 처리를 해주니 개발에 더욱 집중하게 되었고 JPA는 아무 DB를 사용해도 상관이 없으니 <span style="color: #d81b60">의존적</span>이지 않다고 생각했습니다. 그리고 개인적으로 엔티티로 테이블을 파악하다보니 저희가 구성한 테이블을 파악하기 더욱 편했습니다. 

그리고 1차 프로젝트와의 큰 차이점은 로그인 방식입니다. 기존의 프로젝트에서는 단순히 회원가입하고 로그인을 세션과 쿠키로 관리했다면 현재 프로젝트에서는 시큐리티와 소셜 로그인(Google, Naver)을 사용하고 회원가입을 구현하면서 JWT로 인증, 인가처리를 했습니다. JPA를 사용하고 시큐리티와 소셜 로그인 그리고 JWT를 사용한 이유는 1차 프로젝트에서 세션과 쿠키로 진행했을 때 아쉽고 부족한 기능을 채우기 위해서 입니다. 그리고 변화의 흐름을 따라가고 싶었습니다. 기술들은 더욱 발전해가고 사이트는 보안이 중요합니다. 그렇기 때문에 기술의 발전에 따라가는 개발자가 되고 싶었고 발전하는 개발자가 되고 싶었습니다. 소셜 로그인 같은 경우는 제가 사이트들을 사용했을 때 소셜 로그인을 사용하지 않는 사이트들은 요즘에 보기가 힘들었습니다. 그렇다는 것은 보안쪽을 외부에 맡겨서 처리하는 것에 장점이 있다는 것이고 그러한 장점 중 하나가 보안상 좋다고 생각하기 때문에 기존에 공부한 MyBatis를 넘어서 JPA를 선택했고 그리고 소셜 로그인을 구현하고 JWT를 선택하게 되었습니다.

이번에 프로젝트는 더욱 프론트와 소통하기 위해서 미리 배포하고 `Swagger`로 문서화해서 보여주었고 `노션`을 통해서 회의를 정리하고 문서화해서 각자의 파트를 정리하고 한 부분을 표시하는 등 소통하였고 막히거나 의문이 있으면 디스코드로 모여서 간략한 회의를 진행하고 정해진 시간에 규칙적으로 회의를 진행했습니다. 의문점이 있으면 질문을 하였고 팀원의 말에서 제대로 이해하지 못한거 같으면 다시 설명을 하는 등 `소통`부분을 중요하게 여겼습니다. 그렇게 한 이유는 <span style="background-color: #d500f9">개발은 혼자하는 것이 아니라 다같이 진행하는 것이고 실무에서도 결국 다같이 프로젝트를 진행할 것이니 의문을 의문으로 남기면 안되고 의문을 해결하기 위해서는 소통이 중요하다고 생각했기 때문입니다.</span>그리고 이번에는 Git에서 하나의 레포지토리로 진행한 것이 아니라 Organizations을 만들어서 2개의 레포지토리를 만들어서 프론트와 백엔드로 나눠서 진행하였습니다. 아쉬웠던 점은 지속적으로 노션으로 문서화하면 좋았을텐데 큰 틀만 노션으로 정리하고 나머지는 즉각적인 디스코드 회의를 통해서 이야기를 했기 때문에 문서화하지 못했던 점입니다. 이 부분에서 더욱 성장했던 점은 리더로서 2차 프로젝트에서는 더욱 성장되었던 점입니다. 예를들어, 지속적인 코드리뷰를 이끌었고 팀원간에 어떻게 대화를 이끌어내야 분쟁이 생기지 않고 매끄럽게 진행할 수 있는지에 대해 깨닫게 되었습니다. 

이번 프로젝트를 진행하면서 더욱 느꼈지만 사람마다 코드를 작성하는 것이 다르구나라고 느꼈습니다. 거기서 배울 점을 배우게 되었습니다. 예를들어, 원래 저는 서비스를 구현할 때 `~Service`이렇게 클래스를 구성했는데 같은 팀원이 `~Service`이렇게 인터페이스로 구성하고 `~ServiceImpl` 클래스로 상속받아서 구현하는 것을 보고 `가독성`이 더욱 좋고 `유지보수`가 더욱 좋겠다고 생각했습니다. 그래서 코드 스타일을 바꿨습니다. 

마지막으로 AWS을 사용하는 부분에서는 기존의 프로젝트와 똑같이 EC2와 RDS(MySQL)을 선택했지만 차이점은 있습니다. 일단 기존의 프로젝트는 이미지를 프론트가 S3에 올리고 서버에서는 DB에 주소값만 저장했다면 이번에는 서버에서 직접 S3에 올리는 작업을 했습니다. 그리고 Git Action으로 `CI/CD`를 사용해서 git main에 머지가 되면 바로 배포가 적용되도록 적용했습니다. 이 방법을 추가한 이유는 1차 프로젝트에서는 수정할 때마다 재배포를 해야해서 시간이 딜레이가 생기고 배포하는 사람이 일이 있을 때는 재배포가 불가능하다는 점이 불편했기 때문에 코드가 수정이 되면 배포환경으로 올라가게 CI/CD를 적용하게 되었습니다. 젠킨스를 선택하지 않고 git action을 선택한 이유는 프리티어에서 젠킨스를 사용하면 너무 느리다는 정보를 찾게 되어서 git action을 선택하게 되었습니다. 하지만 이 부분이 아쉬웠기 때문에 별도로 Docker와 Jenkins로 배포하였습니다.

마지막으로 이 역할을 감당했을 때 목적은 제가 배운 기술들을 프로젝트에서 나타내고 싶었습니다. 단순히 이론으로만 배우면 제것이 될 수없기 때문입니다. 실제로 프로젝트롤 진행하면서 저의 역할에 제가 배운 기술들을 적용했을 때 이론 부분에서는 이해하던 부분이 실제로는 막히고 에러가 걸리는 경우가 있었습니다. 그 에러를 해결하다 보니 기술에 대해 더 깊게 이해할 수 있었습니다. 상황에 맞게 기술들을 조합해서 사용할 수 있게 된 것입니다.

## 힘들었던 점

힘들었던 점은 처음에 프로젝트를 시작할 때 팀원 부분이였습니다. 팀원을 구해서 프로젝트를 시작했는데 프로젝트에 참여하겠다던 팀원이 도망가서 다시 팀원을 구하니라 딜레이가 많이 걸렸던점입니다. 

