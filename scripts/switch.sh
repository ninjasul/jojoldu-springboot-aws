#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
  IDLE_PORT=$(find_idle_port)

  echo "> 전환할 Port: $IDLE_PORT"
  echo "> Port 전환"

  # echo 'set \$service_url http://127.0.0.1:${IDLE_PORT};': 하나의 문장을 만들어 파이프라인(|)으로 넘겨줌.
  # sudo tee /etc/nginx/conf.d/service-url.inc: 앞에서 넘겨준 문자열을 service-url.inc 에 덮어씀.
  echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc
  # restart는 서비스가 잠시 중단됨. reload는 중단 없이 재시작.
  # 중요한 설정들은 반영되지 않으므로 restart를 사용해야 함.
  # 이 예제에서는 service-url 설정 값을 외부 설정 파일로 다시 불러오는 것이므로 reload 로 가능.
  echo "> 엔진엑스 Reload"
  sudo service nginx reload
}