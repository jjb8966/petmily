# 😺사지말고 입양하세요! [Petmily](http://petmily.cf/)🐶
### Petmily는 유기동물 입양을 장려하는 웹 서비스입니다.

## 목차
### [1. 프로젝트 구성도](#1-프로젝트-구성도-1)
### [2. 개발 환경](#2-개발-환경-1)
### [3. Class Diagram](#3-class-diagram-1)
### [4. ERD](#4-erd-1)
### [5. 주요 기능](#5-주요-기능-1)

# 1. 프로젝트 구성도

![image](https://user-images.githubusercontent.com/87421893/211825229-9a731541-1947-4e8d-9d59-cf1921349556.png)

# 2. 개발 환경

### Back
<img src="https://img.shields.io/badge/Java(JDK 11)-orange?style=for-the-badge&logo=java"/>
<img src="https://img.shields.io/badge/Spring(5.3)-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>

  - Springboot
  - Spring MVC
  - Spring Data Jpa
  
<img src="https://img.shields.io/badge/JPA(Hibernate 5.6)-A111C4?style=for-the-badge&logo=Hibernate&logoColor=white"/>
  
  - Querydsl

### Front

<img src="https://img.shields.io/badge/html-E34F26?style=for-the-badge&logo=Html5&logoColor=white"/> <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=CSS3&logoColor=white"/> <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white"/> <img src="https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white"/> 

### DB

<img src="https://img.shields.io/badge/MySQL(5.7) 서버용-blue?style=for-the-badge&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/Oracle(19c) 개발용-red?style=for-the-badge&logo=Oracle&logoColor=white"/>

### IDE

<img src="https://img.shields.io/badge/IntelliJ-A111C4?style=for-the-badge&logo=IntelliJIDEA&logoColor=white"/> <img src="https://img.shields.io/badge/dbeaver-blue?style=for-the-badge"/>

# 3. Class Diagram

![image](https://user-images.githubusercontent.com/87421893/211825265-dc6424fb-4802-4fae-89d6-8e71528db54d.png)

# 4. ERD

![image](https://user-images.githubusercontent.com/87421893/211825291-7eb780db-c4e8-4707-a9be-bc35bcc2c286.png)

# 5. 주요 기능
## 5.1 로그인 & 회원가입
<div>
<img width="1178" alt="스크린샷 2023-04-01 오후 6 09 04" src="https://user-images.githubusercontent.com/87421893/229275607-2150567f-b009-4726-8a8c-8d2620499b49.png" style="width:55%; height: 350px">
<img width="1129" alt="스크린샷 2023-04-01 오후 6 11 14" src="https://user-images.githubusercontent.com/87421893/229275710-6c6e7490-3a1b-4e33-9fd2-62f4ece38675.png" style="width:35%; height: 350px">
</div>

### 회원가입 시 이메일과 연락처의 형식이 잘못된 경우 재입력 받을 수 있습니다.
### 이메일
- 골뱅이(@)와 점(.)이 반드시 들어가야 합니다.
<img width="759" alt="스크린샷 2023-04-01 오후 9 18 52" src="https://user-images.githubusercontent.com/87421893/229288372-3c72f219-dc97-4a4c-9e4c-713896a87e54.png" style="width:70%">


### 연락처
1. '010'으로 시작해야 합니다.
2. 중간 번호와 마지막 번호는 반드시 4자릿수여야 합니다.
<img width="759" alt="스크린샷 2023-04-01 오후 9 18 43" src="https://user-images.githubusercontent.com/87421893/229288365-019ff7e3-fb35-4d78-84e8-6dcd6dfc84cd.png" style="width:70%">

## 5.2 유기동물 조회
<div>
<img width="1178" alt="스크린샷 2023-04-01 오후 6 09 04" src="https://user-images.githubusercontent.com/87421893/229277022-a5fcfc2a-d8dc-4d94-a3f3-7996b64b408d.png" style="width:45%; height: 350px">
<img width="1129" alt="스크린샷 2023-04-01 오후 6 11 14" src="https://user-images.githubusercontent.com/87421893/229277144-e498eac1-0e4b-4068-8fee-cc9ee2d37631.png" style="width:45%; height: 350px">
</div>

- 유기동물 후원, 임시보호, 입양 신청은 로그인한 회원만 가능합니다.
- 만약 로그인하지 않고 접근 시, 로그인 화면으로 이동합니다.
  - 로그인을 하면, 접근했던 페이지로 이동합니다. 
  - ex) 후원하기 -> 로그인 -> 후원하기

### 5.2.1 후원하기
- 후원금의 최소 금액은 10,000원입니다.

<div>
<img width="1178" alt="스크린샷 2023-04-01 오후 6 09 04" src="https://user-images.githubusercontent.com/87421893/229288912-ff199938-2b2c-4f69-b277-2f9e0d9aa928.png" style="width:45%; height: 350px">
<img width="1129" alt="스크린샷 2023-04-01 오후 6 11 14" src="https://user-images.githubusercontent.com/87421893/229288828-56e3c8fc-1007-4e07-bddc-a57bf46dbcd0.png" style="width:45%; height: 350px">
</div>

### 5.2.2 임시보호하기
<img width="1000" alt="스크린샷 2023-04-01 오후 6 16 55" src="https://user-images.githubusercontent.com/87421893/229277446-ca16b267-5a6a-4f84-a628-bf46879d8b9a.png" style="width:70%">

### 5.2.3 입양하기
<img width="990" alt="스크린샷 2023-04-01 오후 6 20 17" src="https://user-images.githubusercontent.com/87421893/229277606-a15d003e-665b-4b70-848b-db3596f3d00d.png" style="width:70%">

## 5.3 반려동물 찾아요 & 유기동물 봤어요
<div>
<img width="1178" alt="스크린샷 2023-04-01 오후 6 09 04" src="https://user-images.githubusercontent.com/87421893/229285754-498dcb53-77a2-46ff-909e-3f0c8aff295d.png" style="width:45%; height: 350px">
<img width="1129" alt="스크린샷 2023-04-01 오후 6 11 14" src="https://user-images.githubusercontent.com/87421893/229285792-c4be9dc4-a1ab-4b60-a014-5c8b3e6e493c.png" style="width:45%; height: 350px">
</div>

### 5.3.1 검색 기능
### (1) 종, 상태, 키워드를 통해 등록된 유기동물 중 검색 조건에 해당하는 동물을 찾을 수 있습니다.

- **게시글 제목, 내용, 동물 이름, 종류, 종** 5가지 중 하나라도 키워드를 포함하고 있으면 검색됩니다. 
<img width="942" alt="스크린샷 2023-04-01 오후 8 34 48" src="https://user-images.githubusercontent.com/87421893/229286381-54921c31-b66f-4bcb-8d87-331d66325b63.png" style="width:70%">

### (2) 유기동물로 등록된 시간 순서에 따라 조회할 수 있습니다.

### 5.3.2 매칭 기능
### (1) 종, 이름, 종류, 나이, 몸무게 중 하나라도 일치하는 경우, 두 게시판의 상태가 서로 매칭됨으로 바뀝니다.

- 게시글 작성자가 아닌 다른 회원이 작성한 게시글인 경우에만 매칭됩니다.

### [member1] 반려동물 찾아요 게시글 등록
<div>
<img width="1178" alt="스크린샷 2023-04-01 오후 6 09 04" src="https://user-images.githubusercontent.com/87421893/229287217-e6453109-dad1-453e-ab43-761f41ddc33c.png" style="width:45%; height: 300px">
<img width="1129" alt="스크린샷 2023-04-01 오후 6 11 14" src="https://user-images.githubusercontent.com/87421893/229287222-cd7fd6c1-9667-4473-8d7e-eebc57daf6aa.png" style="width:45%; height: 300px">
</div>

### [member2] 유기동물 봤어요 게시글 등록 -> 매칭
<div>
<img width="1178" alt="스크린샷 2023-04-01 오후 6 09 04" src="https://user-images.githubusercontent.com/87421893/229287483-1ef8d8cc-0a65-4dee-9f0c-0cbed371f9de.png" style="width:45%; height: 300px">
<img width="1129" alt="스크린샷 2023-04-01 오후 6 11 14" src="https://user-images.githubusercontent.com/87421893/229287491-bcb3807c-0858-41d9-ab6b-1c818b4fc005.png" style="width:45%; height: 300px">
</div>

<div>
<img width="1178" alt="스크린샷 2023-04-01 오후 6 09 04" src="https://user-images.githubusercontent.com/87421893/229287876-c30dbc62-c85e-461c-bdb7-da0d34a6bf5b.png" style="width:45%; height: 300px">
<img width="1129" alt="스크린샷 2023-04-01 오후 6 11 14" src="https://user-images.githubusercontent.com/87421893/229287878-1810b926-2153-450c-8d70-ed676bb8b8c6.png" style="width:45%; height: 300px">
</div>

### (2) 매칭된 게시글을 마이페이지에서 별도로 확인 가능합니다.
### member1
<img width="1046" alt="스크린샷 2023-04-01 오후 9 05 12" src="https://user-images.githubusercontent.com/87421893/229287614-0e05e391-7831-4a94-b89d-431d6f2ef25a.png" style="width:70%">

### member2
<img width="1046" alt="스크린샷 2023-04-01 오후 9 05 45" src="https://user-images.githubusercontent.com/87421893/229287643-dfedbc6b-dc7a-468f-817f-ff8b515038e0.png" style="width:70%">


## 5.4 게시판
### (1) 게시판은 자유, 문의, 입양 후기 게시판 3종류의 게시판이 있습니다.
<img width="1328" alt="스크린샷 2023-04-02 오후 4 40 28" src="https://user-images.githubusercontent.com/87421893/229339456-6dd587f1-2793-4f78-9bb2-2bf2e042795e.png" style="width:70%">

### (2) 게시글 작성시 게시글의 공개 여부를 설정할 수 있습니다.
- 공개글은 로그인하지 않은 회원도 볼 수 있습니다.
- 비공개글은 작성자, 관리자를 제외한 회원은 볼 수 없습니다.
<img width="1178" alt="스크린샷 2023-04-02 오후 4 44 54" src="https://user-images.githubusercontent.com/87421893/229339638-e9c47ece-0a58-491b-bd34-3855c9920917.png" style="width:70%">

### (3) 로그인한 회원은 게시글에 댓글을 작성할 수 있습니다.
- 자신이 작성한 댓글만 삭제가 가능합니다.
### member3
<img width="1163" alt="스크린샷 2023-04-02 오후 4 49 29" src="https://user-images.githubusercontent.com/87421893/229339845-75bf102a-a47e-462b-9c7a-13ed3a0e6f98.png" style="width:70%">

### (4) 로그인하지 않은 회원은 게시글과 댓글을 작성할 수 없습니다.
- 게시글 작성을 시도하는 경우 로그인 화면으로 이동합니다.
  - 로그인을 하면, 접근했던 페이지로 이동합니다.
  - ex) 자유 게시글 작성 -> 로그인 -> 자유 게시글 작성
<img width="1171" alt="스크린샷 2023-04-02 오후 4 48 07" src="https://user-images.githubusercontent.com/87421893/229339760-e185a05b-58c5-4f3a-85c8-569ec266e2b6.png" style="width:70%">

## 5.5 관리자 페이지
- 관리자 권한이 있는 아이디로 로그인 시 관리자 페이지로 접근할 수 있습니다.
- 관리자 페이지의 모든 기능은 REST API로 구현되었습니다.
<img width="937" alt="스크린샷 2023-04-02 오후 5 31 23" src="https://user-images.githubusercontent.com/87421893/229341637-9df481e1-3880-48b1-8587-7e6d2186bcb0.png">

### 5.5.1 도메인 관리
- 모든 도메인에 대해 조회, 수정, 삭제가 가능합니다.

### (1) 회원 관리
<div>
<img width="1178" alt="스크린샷 2023-04-01 오후 6 09 04" src="https://user-images.githubusercontent.com/87421893/229341779-dcee7181-ce5c-4a7d-bfb3-3cf65171a909.png" style="width:45%; height: 350px">
<img width="1129" alt="스크린샷 2023-04-01 오후 6 11 14" src="https://user-images.githubusercontent.com/87421893/229341785-b9c5e4b4-e89c-473b-a69f-8c1f0dd4fc81.png" style="width:45%; height: 350px">
</div>

### (2) 유기동물 관리
<div>
<img width="1178" alt="스크린샷 2023-04-01 오후 6 09 04" src="https://user-images.githubusercontent.com/87421893/229341816-6f050e84-a1a9-4f5d-b7b0-2ac945dd19a1.png" style="width:45%; height: 350px">
<img width="1129" alt="스크린샷 2023-04-01 오후 6 11 14" src="https://user-images.githubusercontent.com/87421893/229341819-197b4c18-283f-42ec-bd15-1c2768bce28b.png" style="width:45%; height: 350px">
</div>

### (3) 게시글 관리
<div>
<img width="1178" alt="스크린샷 2023-04-01 오후 6 09 04" src="https://user-images.githubusercontent.com/87421893/229341849-8416d1f0-3a84-49d8-99bb-b1d56a7f6175.png" style="width:45%; height: 350px">
<img width="1129" alt="스크린샷 2023-04-01 오후 6 11 14" src="https://user-images.githubusercontent.com/87421893/229341858-fa349f76-3e3b-4ea9-bc87-31765ce5e1ad.png" style="width:45%; height: 350px">
</div>

### (4) 입양/임보 관리
<div>
<img width="1178" alt="스크린샷 2023-04-01 오후 6 09 04" src="https://user-images.githubusercontent.com/87421893/229341882-378f2dc0-6eef-4e86-bc03-7bcaacd7c26a.png" style="width:45%; height: 350px">
<img width="1129" alt="스크린샷 2023-04-01 오후 6 11 14" src="https://user-images.githubusercontent.com/87421893/229341885-fa43144f-5967-404a-bd31-6fef3208b572.png" style="width:45%; height: 350px">
</div>

### (5) 후원 관리
<div>
<img width="1178" alt="스크린샷 2023-04-01 오후 6 09 04" src="https://user-images.githubusercontent.com/87421893/229341925-f9da18f5-d23e-4041-b94d-4308ee93d42e.png" style="width:45%; height: 350px">
<img width="1129" alt="스크린샷 2023-04-01 오후 6 11 14" src="https://user-images.githubusercontent.com/87421893/229341937-5c520336-56f4-4f51-8ce5-ea19a781fdf7.png" style="width:50%; height: 350px">
</div>

### 5.5.2 입양/임보 승인,거절 기능
- 회원이 입양 및 임시보호를 신청한 경우 관리자가 승인, 거절할 수 있습니다.

<div>
<img width="1178" alt="스크린샷 2023-04-01 오후 6 09 04" src="https://user-images.githubusercontent.com/87421893/229546707-fde6a851-7866-4891-b7cd-3ab4729119ab.png" style="width:45%; height: 350px">
<img width="1129" alt="스크린샷 2023-04-01 오후 6 11 14" src="https://user-images.githubusercontent.com/87421893/229546782-e05230a9-38f3-401a-a17d-7e49d3894050.png" style="width:50%; height: 350px">
</div>

<img width="1178" alt="스크린샷 2023-04-02 오후 4 44 54" src="https://user-images.githubusercontent.com/87421893/229546842-c56ae8a5-1ffc-4f21-8513-6001c7e554ec.png" style="width:70%">
