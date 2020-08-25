#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=jojoldu-springboot-aws

echo "> Build 파일 복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY/

# pgrep 은 process id를 추출하는 명령어, -f 는 full process name 을 가져옴.
echo "> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -fl jojoldu-springboot-aws*.jar | grep jar | awk '{print $1}')

# -z "$CURRENT_PID": 현재 구동 중인 애플리케이션 pid의 길이가 0인 경우. 즉, 존재하지 않는 경우.
echo "현재 구동 증인 애플리케이션 pid : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동증인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# ls -tr: 수정시간 역순으로 파일과 디렉토리 목록 조회
# grep *.jar | tail -n 1: jar 파일 중 가장 마지막에 있는 항목 조회
echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

# java -jar jar파일: 일반적인 자바프로그램 실행, 사용자가 터미널 접속을 끊을 때 애플리케이션도 같이 종료됨.
# nohup java -jar: 애플리케이션 실행자가 터미널을 종료해도 애플리케이션은 계속 구동.
# 2>&1: 표준 오류가 표준 output stream 을 통해 출력됨.
echo "> JAR Name: $JAR_NAME"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

nohup java -jar \
  -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real.properties,/home/ec2-user/app/application-real-db.properties \
  -Dspring.profiles.active=real \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &