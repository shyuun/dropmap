#!/usr/bin/env bash

ABSPATH=$(readlink -f $0) # 심볼릭 링크를 따라가면서 스크립트의 절대경로를 찾음, $0 : 현재 실행중인 스크립트의 경로를 의미
echo ">ABSPATH : $IDLE_PORT"
ABSDIR=$(dirname $ABSPATH) #현재 stop.sh가 속해있는 경로 찾음
echo ">ABSDIR : $ABSDIR"
source ${ABSDIR}/profile.sh #import구문, 해당코드로 profile.sh의 여러 function 사용가능

IDLE_PORT=$(find_idle_port)

echo ">$IDLE_PORT 에서 구동 중인 애플리케이션 pid 확인"
IDLE_PID=$(sudo lsof -ti tcp:${IDLE_PORT}) # lsof (List Open Files) -t: 해당 포트를 사용 중인 프로세스의 PID만 출력., -i tcp:${IDLE_PORT}: 특정 TCP 포트(IDLE_PORT)에서 실행 중인 프로세스를 찾음

if [ -z ${IDLE_PID} ]
then
    echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $IDLE_PID"
    kill -15 ${IDLE_PID}
    sleep 5
fi