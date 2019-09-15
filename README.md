# Vidflow [![Build Status](https://travis-ci.org/ryrie/vidflow.svg?branch=master)](https://travis-ci.org/ryrie/vidflow)


Tiktok(www.tiktok.com)과 같은 기능을 하는 것을 목표로 하는 mobile web application 입니다.

저의 개발 공부의 목적으로만 사용됩니다. 

### 사용한 프레임워크/라이브러리

Frontend : create-react-app을 이용하여 react.js 기반 SPA를 제작하였습니다. www.github.com/ryrie/vidflow-web

Backend : Spring Boot을 비롯한 Spring Framework : WebMVC, Security, Data JPA


### 현재 프로젝트에서 사용하고 있는 환경

Web Frontend HOST : Github Page

Web API server HOST : AWS EC2 Ubuntu 18.04

Video Content Server : apache2

DB : AWS RDS(MySQL)

CI : travis.ci

CD : AWS Codedeploy


### 빌드 방법

`docker-compose build && docker-compose up`

### 프로젝트 관련 문서

공부하면서 개발을 진행하였기 때문에 공부한 내용을 정리하고, 프로젝트와 관련지어서 설명하는 방식으로
문서를 작성하였습니다.
[문서 바로가기](docs/Preface.adoc)

