#!/usr/bin/env bash
APP_NAME=chatty-api
cd /home/ubuntu/chatty
echo "> 실행 중인 Spring Boot 서비스 중지"
docker compose down springBoot
echo "> 도커 이미지 빌드"
docker build -t "${APP_NAME}:latest" .
echo "> 도커 컴포즈 실행"
docker compose up -d
echo "> 사용되지 않는 도커 이미지 삭제"
docker image prune -f
exit 0
