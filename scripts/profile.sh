#!/usr/bin/env bash

# idle 상태인 profile 탐색
function find_idle_profile() {
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

  # 응답코드가 400 이상이면
  if [ ${RESPONSE_CODE} -ge 400 ]
  then
    CURRENT_PROFILE=real2
  else
    CURRENT_PROFILE=$(curl -s http://localhost/profile)
  fi

  if [ "${CURRENT_PROFILE}" == "real1" ]
  then
    IDLE_PROFILE=real2
  else
    IDLE_PROFILE=real1
  fi

  # bash 스크립트에서는 리턴 값이 없음.
  # 따라서 echo 출력 값을 ($(find_idle_profile)) 과 같이 사용해서 리턴 값을 획득
  echo "${IDLE_PROFILE}"
}

# idle 상태인 profile의 port 찾기
function find_idle_port() {
  IDLE_PROFILE=$(find_idle_profile)

  if [ ${IDLE_PROFILE} == real1 ]
  then
    echo "8081"
  else
    echo "8082"
  fi
}