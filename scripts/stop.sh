#!/usr/bin/env bash

# stop.sh가 속해 있는 경로를 찾음.
ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

# 다른 쉘 파일을 import
source ${ABSDIR}/profile.sh

IDLE_PORT=$(find_idle_port)

# lsof: 시스템에 열린 파일 목록을 알려주고 사용하는 프로세스, 디바이스 정보, 파일의 종류등 상세한 정보를 출력.
#       -t: pid 정보만 출력
#       -i: 네트워크 소켓 정보 출력. 프로토콜, 서비스, 호스트, IP 정보 출력
echo "> $IDLE_PORT 에서 구동 중인 애플리케이션 pid 확인"
IDLE_PID=$(lsof -ti tcp:${IDLE_PORT})

# -z ${IDLE_PID}: IDLE_PID의 길이가 0인 경우. 즉, 존재하지 않는 경우.
if [ -z ${IDLE_PID} ]
then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $IDLE_PID"
  kill -15 ${IDLE_PID}
  sleep 5
fi
