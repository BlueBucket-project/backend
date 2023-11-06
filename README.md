# BlueBuket 중고마켓 프로젝트

## 프로젝트 설명

각 지역에 오프라인 센터가 있다는 가정하에 판매자가 오프라인 센터에 판매자가 물건을 가서 등록을 하면 오프라인 센터가 물건을 검수하여 사이트에 물건에 대한 정보와 이미지를 올려놓으면 구매자는 사이트를 둘러보다 원하는 물건이 있으면 예약을 걸어놓고 현장에서 구매한다. 당근마켓의 직거래가 `일대일`관계라면 이 사이트 컨셉은 판매자와 구매자 사이에 껴서 거래를 순조롭게 도와주는 컨셉이다. 즉 쇼핑몰보다는 중고마켓과 같은 느낌입니다. 

## 중고마켓 컨셉을 고른 이유

처음에 쇼핑몰을 구현할 것인지 중고마켓을 구현할 것인지 고민을 많이 했습니다. 고민 끝에 중고마켓을 선택한 이유는 기존에 쇼핑몰들은 많은 반면에 중고마켓 같은 사이트는 별로 없고 요즘 젊은 사람들은 물건을 고장날 때 까지 사용하는 것이 아니라 사용하다 중고로 판매하고 새로운 제품을 구매하거나 중고로 구매를 많이해서 사용하는데 기존에 있는 중고관련 사이트들은 판매자와 구매자간의 거래를 책임지지 않는 모습들이 있고 일대일 거래에서 문제가 생기는 경우가 많습니다. 그래서 평소에 생각하던 있었으면 좋겠다고 생각하던 중고마켓을 선택하게 되었습니다. 

# 배포링크
[BlueBucket]() 아직 미완

# GitHub 주소
[코드보러 가기](https://github.com/orgs/BlueBucket-project/repositories)

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
# ERD
![](https://velog.velcdn.com/images/zxzz45/post/438cfe73-ba11-49e7-be4a-74d22e1ae220/image.png)

# 프로젝트 세부 진행

[프로젝트 파악하기](https://velog.io/@zxzz45/%EB%B0%B0%ED%8F%AC-c39fncia)
여기서는 프로젝트를 코드와 설명으로 어떻게 진행하고 왜 그렇게 했는지 설명하면서 진행할 생각입니다. 이곳에는 프로젝트를 진행하면서 만났던 문제들도 기록하고 있습니다.

# 구현 기능(백엔드 기준)

- [x] 문향이 있어야 제가 구현한 기능이고 없으면 다른 팀원이 구현한 기능입니다.

## 회원(유요한)

- [x] 회원가입
- [x]  로그인
> JWT 반환
- [x]  OAuth2 로그인
> JWT 반환
- [x]  로그아웃
- [x]  마이페이지
>- 회원 수정
>- 회원 탈퇴
-   주문내역 조회 
> 회원페이지에서 구매/판매 내역을 조회할 수 있게 한다.


## 게시글(김민성)

-  공지 & 이벤트 게시글 작성 기능 - 관리자(ADMIN)
-   공지 & 이벤트 게시글 수정 - 관리자(ADMIN)
-   공지 & 이벤트 게시글 삭제 - 관리자(ADMIN)
-  공지 & 이벤트 게시글 전체 보기 - 전체
-  공지 & 이벤트 게시글 상세 페이지 - 전체
-  관리자 소개 페이지 작성 - 관리자(ADMIN)
-  문의글 작성 - 유저(USER)

## 댓글(유요한 & 김민성)

댓글은 상품에서는 없고 게시글에서만 댓글이 존재합니다. 대댓글을 회원이 관리자에게 문의글을 남겼을 때 답변할 수 있도록 대댓기능을 추가했습니다.

- [x]  답글(댓글 구현)
-  대 댓글 기능(김민성)

## 상품(오현진 & 유요한)

- [x]  상품 등록 
- [x]  상품 수정 
- [x]  상품 & 이미지 삭제 
-  장바구니 기능
>- 유저가 상품을 장바구니에 담는다.
>- 유저가 주문 수량을 조절한다. 하지만 1개인 상품은 조절이 불가능하다.
>- 주문하기를 누른다.
>- Item의 상태가 `예약`으로 바뀐다.
-  장바구니 물건 삭제기능
> 일부삭제 및 전체삭제 구현
-  장바구니 수량 수정
-   주문하기
>- 위에서 `예약`으로 바뀐 상품을 주문(예약)해준다.
>- 주문한 수량에 따라 Item의 `stockNumber`을 줄여준다.
>- 예약한 상품과 유저를 등록해준다.
>- 1개인 상품을 주문했다고 하면 상품의 상태를 `SOLD_OUT`바꿔준다.
>- 수량이 남아 있으면 계속 `SELL`
-  주문 취소
- [x]  이미지만 삭제 
- [x]  상품 수정할 때 이미지를 업데이트하는게 아니라 추가해줄 경우 
- [x]  상품 전체 보기  
- [x]  상품 검색 기능  
- [x]  상품 상세 페이지 

## 이미지 넣기 기능(유요한)

- [x]  S3 이미지 넣기

## 어드민(유요한 & 오현진)

- [x]  관리자가 게시글 삭제
- [x]  관리자가 상품 삭제
- [x]  댓글 삭제
- [x]  관리자가 예약된 상품 확인하는 기능
- [x] 관리자가 판매 상품 확인하는 기능
- [x] 특정 상태 변환된 상품만 상세 페이지 보기
- 상품 상태를 바꿔주는 기능

## Swagger(유요한)

- [x]  swagger 기능

## 예외처리(유요한)

- [x]  예외처리
- [x] 전체 예외처리, 유저를 못 찾을 경우 발생하는 예외 처리
- [x] 게시글을 못 찾을 경우 발생하는 예외 처리
- [x] 상품을 못 찾을 경우 발생하는 예외 처리
- [x] 검증 오류 예외 처리
- [x] 인증 예외 처리
- [x] 파일 업로드 예외 처리
- [x] 파일 다운로드 예외 처리
- [x] 외부 서비스 예외 처리
- [x] 서비스 로직 예외 처리
- [x] JWT 권한 예외 처리
- [x] JWT 인증 예외 처리

## Spring jacoco(유요한)

-  [x] jacoco기능

## 배포(유요한 & 오현진)

- [x]  EC2
- [x]  RDS
-  Git action

## ERD(다같이)

- [x]  ERD 작성

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
 

