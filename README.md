# 🎞🎬 같이볼까요? 🎥📽

시간이 지나 극장에서 관람할 기회를 놓쳐버린 영화들,  
혹은 OTT로 관람했던 영화를 사람들과 함께 극장에서 관람할 수 있게끔 하는 서비스입니다.

## ERD 
![Image](https://github.com/user-attachments/assets/ade2e7ba-76a4-4a4a-b2f8-872722143ee1)

## 프로젝트 기능 및 설계

### 사용자기능 👨‍💼
- 회원가입 기능
  - 사용자는 회원가입을 할 수 있다. 사용자 계정은 USER / PARTNER (공식 극장관리 계정) 계정으로 나뉜다.
  - 원한다면 프로필 사진을 업로드 할 수 있다(AWS S3 사용).
  - 회원가입시 수집하는 데이터
    - 멤버 ID (수정 불가 , 중복 불가)
    - 비밀번호
    - 이메일
    - 전화번호
    - 프로필 사진 (선택)

- 로그인 기능
  - 사용자는 로그인을 할 수 있다. 로그인시 회원가입때 사용한 아이디와 패스워드가 일치해야한다.
  - 아이디와 패스워드가 일치한다면, JWT 를 발급해준다.
 
- 회원정보 수정 및 탈퇴 기능
  - 사용자는 사용자 정보를 언제든 수정 / 탈퇴 할 수 있다. 

- 포인트 기능
  - 사용자는 계정에 포인트를 보유하고 있다.
  - 사용자는 포인트를 충전할 수 있다.
  - 이 포인트는 <같이 봐요> 생성 및 신청에 사용된다.
  - PARTNER 계정은 포인트 충전이 불가능하다.
***
### 극장 / 영화 관련 기능📽
- 극장 등록기능
  - PARTNER 권한의 계정을 이용해 극장을 등록한다.
  - 1개의 PARTNER 계정은 1개의 CINEMA 만 등록할 수 있다.
  - 원한다면 극장 프로필 사진을 업로드할 수 있다.(AWS S3 사용)
  - PARTNER 계정은 계정에 연결되어 있는 극장의 정보를 자유롭게 수정 / 삭제 할 수 있다.
  - 극장 등록할때 수집하는 데이터 ( 모두 수정 가능 , 모두 입력 필수 )
    - 극장 이름 
    - 극장 설명
    - 극장 경도 / 위도
    - 프로필 사진

- 찜 기능
  - 사용자는 상영가능 영화 목록에 존재하지 않는 영화들을 자유롭게 "찜"할 수 있다.
  - "찜" 갯수가 100개( 확정 X ) 이상인 영화들은 배급사와 합의를 통해 상영가능 영화로 변환된다.
  - "찜" 을 많이 당한 영화가 "상영가능 상태" 로 변경된다면 찜 테이블의 해당영화 데이터들은 삭제된다.
   
***
### 같이 봐요 기능👩‍💼🦸‍♀️
- <같이 봐요> 기능
  - 제휴매장들(DB 내부 극장중) 중 한곳을 선택해, 관람하고싶은 영화(상영 가능 영화중)를 선택해 날짜와 시간을 정한다.
  - 그 날짜와 시간에 해당영화의 <같이 봐요>가 존재하지 않는다면, 새로운 <같이 봐요> 를 생성한다.
  - <같이 봐요> 를 생성할때, <같이 봐요> 에 신청할때, 계정에 보유한 15000 포인트가 차감된다.
  - 포인트 보유량이 15000 포인트보다 낮은경우 포인트를 충전해야한다.
  - USER 계정과 PARTNER 계정 모두 <같이 봐요> 를 생성할 수 있다.  
    (PARTNER 계정은 포인트차감 X , PARTNER 계정은 연결되어있는 극장만 생성가능하다.)
  - <같이 봐요>는 상영일자 기준 3일전까지만 취소가 가능하다.
***
### API 기능🛠
- 영화 정보 가져오기 기능
  - <같이 봐요> 및 상영 가능 영화를 조회할 때 TMDB API 를 이용해 영화 정보를 가져온뒤, DB에 저장한다.  
  **Ref. https://developer.themoviedb.org/reference/intro/getting-started**

- 프로필 사진 업로드 기능
  - AWS S3 를 이용해 사용자 / 극장의 프로필 사진을 업로드 할 수 있다. 



## Trouble Shooting
[go to the trouble shooting section](doc/TROUBLE_SHOOTING.md)

### Tech Stack
![js]( https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
![js](	https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
![js](   https://img.shields.io/badge/json%20web%20tokens-323330?style=for-the-badge&logo=json-web-tokens&logoColor=pink)
<img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white">
<img src="https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white">
<img src="https://img.shields.io/badge/Amazon%20S3-569A31?style=for-the-badge&logo=Amazon%20S3&logoColor=white">
