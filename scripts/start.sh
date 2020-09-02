#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app/step3
PROJECT_NAME=jojoldu-springboot-aws

echo "> Build 파일 복사"
echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY/"
cp $REPOSITORY/zip/*.jar $REPOSITORY/

# ls -tr: 수정시간 역순으로 파일과 디렉토리 목록 조회
# grep *.jar | tail -n 1: jar 파일 중 가장 마지막에 있는 항목 조회
echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

# java -jar jar파일: 일반적인 자바프로그램 실행, 사용자가 터미널 접속을 끊을 때 애플리케이션도 같이 종료됨.
# nohup java -jar: 애플리케이션 실행자가 터미널을 종료해도 애플리케이션은 계속 구동.
# 2>&1: 표준 오류가 표준 output stream 을 통해 출력됨.
echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"
IDLE_PROFILE=$(find_idle_profile)

nohup java -jar \
  -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-$IDLE_PROFILE.properties,/home/ec2-user/app/application-real-db.properties \
  -Dspring.profiles.active=$IDLE_PROFILE \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &