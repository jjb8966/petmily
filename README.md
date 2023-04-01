# 😺사지말고 입양하세요! [Petmily](http://petmily.cf/)🐶
### Petmily는 유기동물 입양을 장려하는 웹 서비스입니다.

## 목차
### [1. 프로젝트 구성도](#1-프로젝트-구성도-1)
### [2. 개발 환경](#2-개발-환경-1)
### [3. Class Diagram](#3-class-diagram-1)
### [4. ERD](#4-erd-1)
### [5. 주요 기능](#5-주요-기능)

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
  <img width="794" alt="스크린샷 2023-04-01 오후 5 38 42" src="https://user-images.githubusercontent.com/87421893/229275607-2150567f-b009-4726-8a8c-8d2620499b49.png" style="width:50%; margin-right: 10px;">
  
  <img width="792" alt="스크린샷 2023-04-01 오후 5 41 20" src="https://user-images.githubusercontent.com/87421893/229275710-6c6e7490-3a1b-4e33-9fd2-62f4ece38675.png" style="width:50%;">
</div>

## 5.2 유기동물 조회
<div>
<img width="1178" alt="스크린샷 2023-04-01 오후 6 09 04" src="https://user-images.githubusercontent.com/87421893/229277022-a5fcfc2a-d8dc-4d94-a3f3-7996b64b408d.png" style="width:45%; height: 350px">
<img width="1129" alt="스크린샷 2023-04-01 오후 6 11 14" src="https://user-images.githubusercontent.com/87421893/229277144-e498eac1-0e4b-4068-8fee-cc9ee2d37631.png" style="width:45%; height: 350px">
</div>

### 유기동물 후원, 임시보호, 입양 신청은 로그인한 회원만 가능합니다.

### 5.2.1 후원하기
<img width="1005" alt="스크린샷 2023-04-01 오후 6 14 26" src="https://user-images.githubusercontent.com/87421893/229277317-46a43d82-85d8-4b2a-b8f0-2ab1ccbffb95.png" style="width:70%">

### 5.2.2 임시보호하기
<img width="1000" alt="스크린샷 2023-04-01 오후 6 16 55" src="https://user-images.githubusercontent.com/87421893/229277446-ca16b267-5a6a-4f84-a628-bf46879d8b9a.png" style="width:70%">

### 5.2.3 입양하기
<img width="990" alt="스크린샷 2023-04-01 오후 6 20 17" src="https://user-images.githubusercontent.com/87421893/229277606-a15d003e-665b-4b70-848b-db3596f3d00d.png" style="width:70%">

## 5.3 반려동물 찾아요 & 유기동물 봤어요
### 5.3.1 검색 기능
### 5.3.2 매칭 기능

## 5.4 게시판
### 5.4.1 자유 게시판
### 5.4.2 문의 게시판
### 5.4.3 입양 후기 게시판

## 5.5 관리자 페이지
### 5.5.1 CRUD
### 5.5.2 입양/임보 승인,거절 기능
