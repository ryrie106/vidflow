# Vidflow [![Build Status](https://travis-ci.org/ryrie/vidflow.svg?branch=master)](https://travis-ci.org/ryrie/vidflow)

## 개요

이 프로젝트는 저의 대학 졸업 작품의 연장선상으로, 
Tiktok(www.tiktok.com)과 같은 기능을 하는 웹 애플리케이션을 제작 하는 것이 목표입니다.

각각에 대한 정보는 doc 폴더에 작성하였습니다.

## 스택
### Frontend
React.js를 사용.. antd-mobile의 컴포넌트를 사용하였습니다.

### Backend



ORM Framework : Spring JPA Data

### 프로젝트에서 사용한 인프라

server : Ubuntu 18.04(AWS EC2)

DB : MySQL(AWS RDS)

CI : travis.ci

CD : AWS Codedeploy

## Build 방법

./mvnw compile