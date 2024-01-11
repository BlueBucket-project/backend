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
## 화면 구성 (일반 유저)📺
| 로그인 팝업 | 회원가입 팝업 | 
| :-------------------------------------------: | :------------: |
|  <img width="320" src="https://github.com/YuYoHan/Belog/assets/43868558/8b744b55-7a88-411c-9ddd-cd49e7313520/image.gif"/> |  <img width="320" src="https://github.com/YuYoHan/Belog/assets/43868558/5b4a27b5-de49-476c-bf64-1d8cdc3ec9b5/image.gif"/>| 
| 메인 페이지  | 설정 페이지 |
| <img width="320" src="https://github.com/YuYoHan/Belog/assets/43868558/e70a8be9-f362-4359-8b49-5701290c52f1/image.gif"/> | <img width="320" src="https://github.com/YuYoHan/Belog/assets/43868558/0ae10d02-a743-4b87-9777-4fd0aa4bbbc5/image.gif"/> |
| 게시글 등록 |  게시글 수정 |
| <img width="320" src="https://velog.velcdn.com/images/cleooo5857/post/7f35b58a-e0f6-487e-b667-e93b0aef4e6b/image.gif"/>   |  <img width="320" src="https://velog.velcdn.com/images/cleooo5857/post/9a63a18b-daa2-4f50-9433-a2ea47c87803/image.gif"/>     |  
|   게시글 삭제 | 댓글 | 
|   <img width="320" src="https://velog.velcdn.com/images/cleooo5857/post/6925dc0d-1fda-44a2-be1e-245cb9a3276d/image.gif"/>  |  <img width="320" src="https://github.com/YuYoHan/Belog/assets/43868558/895f0056-69ab-4298-8d77-199cc0f1e766/image.gif"/>     |

---
# ERD
![image](https://github.com/BlueBucket-project/backend/assets/110465572/22fdbe5b-f56a-4432-aa15-a440b6a73af4)


# 프로젝트 세부 진행

[프로젝트 파악하기](https://velog.io/@zxzz45/%EB%B0%B0%ED%8F%AC-c39fncia) <br />
여기서는 프로젝트를 코드와 설명으로 어떻게 진행하고 왜 그렇게 했는지 설명하면서 진행할 생각입니다. 이곳에는 프로젝트를 진행하면서 만났던 문제들도 기록하고 있습니다.

# 구현 기능(백엔드 기준)

- [x] 문향이 있어야 제가 구현한 기능이고 없으면 다른 팀원이 구현한 기능입니다.

## 회원(유요한)

- [x]  회원가입
- [x]  로그인
- JWT 반환
- [x]  OAuth2 로그인
- JWT 반환
- [x]  로그아웃
- [x]  마이페이지
- 회원 수정
- 회원 탈퇴
- [x]  주문내역 조회 -오현진
- 회원페이지에서 구매/판매 내역을 조회할 수 있게 한다.
- 나의 문의 보기
- 나의 문의글을 페이지, 검색기능을해서 보여준다.



- 해당 유저의 문의글을 가져온다.
- 하지만 상세보기는 없다. (펼처보기로 보기 때문)


## 상품문의(유요한 & 오현진)

- [x]  상품 문의 작성 기능
- [x]  상품 문의 답변 기능 - 관리자(ADMIN) : 댓글
- 작성
- 삭제
- 수정
- [x]  상품 문의 수정
- [x]  상품 문의 게시글 삭제
- [x]  문의글 상세 보기
- 상품 안에 있는 문의글은 게시글 형태로 되어 있기 때문에  상세보기로 들어가야 한다.
- 해당 상세보기 기능은 유저를 가리지 않고 그 상품에 관한 문의글이다.



- [x]  상품 문의 게시글 전체 보기(해당 상품만)
- 상품 안에 있는 문의글은 게시글 형태(펼쳐보기 X)
- 이 기능은 item 상세 보기를 누르면 상품에 관련된 문의글이 페이지 처리되서 나온다.
- 페이지 처리에 대한 코드



## 상품(오현진 & 유요한)

- [x]  상품 등록 - 유요한
- [x]  상품 수정 - 유요한
- [x]  상품 & 이미지 삭제 - 유요한
- [x]  장바구니 기능
- 유저가 상품을 장바구니에 담는다.
- 유저가 주문 수량을 조절한다. 하지만 1개인 상품은 조절이 불가능하다.
- 주문하기를 누른다.
- Item의 상태가 `예약`으로 바뀐다.
- [x]  장바구니 물건 삭제기능
- 일부삭제 및 전체삭제 구현
- [x]  장바구니 수량 수정
- [x]  주문하기
- 위에서 `예약`으로 바뀐 상품을 주문(예약)해준다.
- 주문한 수량에 따라 Item의 `stockNumber`을 줄여준다.
- 예약한 상품과 유저를 등록해준다.
- 1개인 상품을 주문했다고 하면 상품의 상태를 `SOLD_OUT`바꿔준다.
- 수량이 남아 있으면 계속 `SELL`
- [x]  주문 취소
- [x]  이미지만 삭제 - 유요한
- [x]  상품 수정할 때 이미지를 업데이트하는게 아니라 추가해줄 경우 - 유요한
- [x]  상품 전체 보기  - 유요한
- [x]  상품 검색 기능  - 유요한
- [x]  상품 상세 페이지 - 유요한

## 이미지 넣기 기능(유요한)

이 부분은 일단 구현은 했는데 제대로 넣어지는지

테스트가 필요함

- [x]  S3 이미지 넣기

## 어드민(유요한)

- [x]  관리자가 상품삭제
- [x]  관리자가 게시글 삭제
- [x]  관리자가 상품 전체보기 기능



- [x]  모든 상품 문의 전체 보기(펼쳐보기)


- [x]  상품 상세 보기



- [x]  관리자가 상품을 최종 구매 확정하는 기능(오현진)
- [x]  관리자가 회원의 문의글 보기(펼쳐보기)
- 모든 문의글 보기

- [x]  관리자가 예약된 상품 확인하는 기능
- 예약으로 상태 변환된 상품만 전체 페이지로 보기
- 예약으로 상태 변환된 상품만 상세 페이지 보기
- 다른 조건을 넣어서 보기(오현진)

## Swagger(유요한)

- [x]  swagger 기능

## 예외처리(유요한)

- [x]  예외처리
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

- [x]  jacoco기능

## 배포(유요한)

- [x]  EC2
- [x]  RDS
- [x]  Git action

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
<img src="https://img.shields.io/badge/QueryDsl-blue?style=flat-square&logo=QueryDsl&logoColor=white"> 
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

1차 프로젝트는 단순히 MyBatis로 블로그형식을 구현했다면 2차 프로젝트는 평소에 생각하던 이런 사이트가 있으면 좋을텐데라고 생각한 사이트를 구현했습니다. 물론 제가 리더라서 무작정 정하기 보다는 프로젝트를 기획할 때 팀원들에게 프로젝트의 컨셉을 말하고  `왜`하고 싶은지 구체적으로 설명해서 현재 프로젝트를 하게 되었습니다. 

>**JPA로 진행하면서 느낀점**
>
> MyBatis가 자바에서 MySQL 쿼리문으로 작성했지만 1차 프로젝트를 진행할 때 떠오른 생각은 <span style="color: #d81b60">그러면 만약 회사에 따라서 사용하는 DB가 다를텐데 `의존적`이지 않나?</span> 라는 생각을 하게 되었습니다. 그래서 2차 프로젝트때는 > `JPA`를 선택하게 되었습니다. JPA를 사용하면서 느낀점은 편하다였습니다. 사용법과 조심해야할 부분을 숙지하고 있다면 JPA가 처리를 해주니 개발에 더욱 집중하게 되었고 JPA는 아무 DB를 사용해도 상관이 없으니 <span style="color: #d81b60">의존적</span> > 이지 않다고 생각했습니다. 그리고 개인적으로 엔티티로 테이블을 파악하다보니 저희가 구성한 테이블을 파악하기 더욱 편했습니다. 다만 아쉬웠던 점은 QueryDsl을 제대로 사용하고 싶었지만 그러지 못했던 점이 아쉬웠습니다. 

그리고 1차 프로젝트와의 큰 차이점은 로그인 방식입니다. 기존의 프로젝트에서는 단순히 회원가입하고 로그인을 세션과 쿠키로 관리했다면 현재 프로젝트에서는 시큐리티와 소셜 로그인(Google, Naver)을 사용하고 회원가입을 구현하면서 JWT로 인증, 인가처리를 했습니다. JPA를 사용하고 시큐리티와 소셜 로그인 그리고 JWT를 사용한 이유는 변화의 흐름을 따라가고 싶었습니다. 기술들은 더욱 발전해가고 사이트는 보안이 중요합니다. 그렇기 때문에 기술의 발전에 따라가는 개발자가 되고 싶었고 발전하는 개발자가 되고 싶었습니다. 소셜 로그인 같은 경우는 제가 사이트들을 사용했을 때 소셜 로그인을 사용하지 않는 사이트들은 요즘에 보기가 힘들었습니다. 그렇다는 것은 보안쪽을 외부에 맡겨서 처리하는 것에 장점이 있다는 것이고 그러한 장점 중 하나가 보안상 좋다고 생각하기 때문에 기존에 공부한 MyBatis를 넘어서 JPA를 선택했고 그리고 소셜 로그인을 구현하고 JWT를 선택하게 되었습니다.

이번에 프로젝트는 더욱 프론트와 소통하기 위해서 미리 배포하고 `Swagger`로 문서화해서 보여주었고 `노션`을 통해서 회의를 정리하고 문서화해서 각자의 파트를 정리하고 한 부분을 표시하는 등 소통하였고 막히거나 의문이 있으면 디스코드로 모여서 간략한 회의를 진행하고 정해진 시간에 규칙적으로 회의를 진행했습니다. 의문점이 있으면 질문을 하였고 팀원의 말에서 제대로 이해하지 못한거 같으면 다시 설명을 하는 등 `소통`부분을 중요하게 여겼습니다. 그렇게 한 이유는 <span style="background-color: #d500f9">개발은 혼자하는 것이 아니라 다같이 진행하는 것이고 실무에서도 결국 다같이 프로젝트를 진행할 것이니 의문을 의문으로 남기면 안되고 의문을 해결하기 위해서는 소통이 중요하다고 생각했기 때문입니다.</span>그리고 이번에는 Git에서 하나의 레포지토리로 진행한 것이 아니라 Organizations을 만들어서 2개의 레포지토리를 만들어서 프론트와 백엔드로 나눠서 진행하였습니다. 

이번 프로젝트를 진행하면서 더욱 느꼈지만 사람마다 코드를 작성하는 것이 다르구나라고 느꼈습니다. 거기서 배울 점을 배우게 되었습니다. 예를들어, 원래 저는 서비스를 구현할 때 `~Service`이렇게 클래스를 구성했는데 같은 팀원이 `~Service`이렇게 인터페이스로 구성하고 `~ServiceImpl` 클래스로 상속받아서 구현하는 것을 보고 `가독성`이 더욱 좋고 `유지보수`가 더욱 좋겠다고 생각했습니다. 그래서 코드 스타일을 바꿨습니다. 

마지막으로 AWS을 사용하는 부분에서는 기존의 프로젝트와 똑같이 EC2와 RDS(MySQL)을 선택했지만 차이점은 있습니다. 일단 기존의 프로젝트는 이미지를 프론트가 S3에 올리고 서버에서는 DB에 주소값만 저장했다면 이번에는 서버에서 직접 S3에 올리는 작업을 했습니다. 그리고 Git Action으로 `CI/CD`를 사용해서 git main에 머지가 되면 바로 배포가 적용되도록 적용했습니다. 젠킨스를 선택하지 않고 git action을 선택한 이유는 프리티어에서 젠킨스를 사용하면 너무 느리다는 정보를 찾게 되어서 git action을 선택하게 되었습니다. 다만 현재 git action에 대한 것이 제가 공부한 것과 차이가 있는데 처음에는 제가 공부한 Git action으로 CI/CD를 하고 배포를 했지만 같은 팀원이 배포를 유지하는 것으로 바꿨습니다. 바꿨을 때 팀원이 공부한 Git action으로 수정했기 때문에 다릅니다.

**바꾼 이유**
- 프리티어 기간의 만료
- 팀원이 MAC이라 MAC환경에서는 더욱 빠르다는 정보를 봐서 제가 배포한 것을 제거하고 팀원이 다시 배포하였습니다.
 

