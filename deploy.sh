#!/usr/bin/env bash
APP_NAME=chatty-api
echo "> 이전 버전의 도커 이미지 삭제"
docker rmi "${APP_NAME}:latest"
cd /home/ubuntu/
echo "> 도커 이미지 빌드"
docker build -t "${APP_NAME}" .
echo "> 도커 컴포즈 실행"
docker compose up -d
