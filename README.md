# resume-rsupport

## 기능 요구사항

* 공지사항 등록, 수정, 삭제, 조회 API를 구현한다.
* 공지사항 등록시 입력 항목은 다음과 같다 - 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일 (여러개)
* 공지사항 조회시 응답은 다음과 같다. - 제목, 내용, 등록일시, 조회수, 작성자 

## 비기능 요구사항 및 평가 항목

* REST API로 구현.
* 개발 언어는 Java, Kotlin 중 익숙한 개발 언어로 한다.
* 웹 프레임 워크는 Spring Boot 을 사용한다.
* Persistence 프레임 워크는 Hibernate 사용시 가산점
* 데이터 베이스는 제약 없음
* 기능 및 제약사항에 대한 단위/통합테스트 작성
* 대용량 트래픽을 고려하여 구현할 것
* 핵심 문제해결 전략 및 실행 방법등을 README 파일에 명시

# 개발환경

## SKILL

* Java
* Spring Boot
* H2
* Hibernate
* cache

## Tool

* VSCode
* gradle
* git

# 설계

## API

* 등록
    * 작성자를 request header: {X-WRITER: 'writer'} 로 받음
    * 필수: 작성자, 제목, 내용, 공지 시작일시
    * 공지 종료일시 가 null 일 경우 계속 공지
* 수정
    * 변경 작성자를 request header: {X-WRITER: 'writer'} 로 받음
    * 필수: 변경 작성자, 제목, 내용, 공지 시작일시
* 조회
    * caching
    * 단일
        * {제목, 내용, 작성자, 등록일시, 조회수, 공지 시작일시, 공지 종료일시}
    * 복수
        * 공지 시작일시와 공지 종료일시 filter 하여 조회
        * [{제목, 작성자, 공지 종료일시, 조회수}]
* 삭제

## 명세서

## 클래스 다이어그램

## ERD

# 제약사항

# 실행방법

    * >gradle clean
    * >gradle build
    * >gradle bootrun