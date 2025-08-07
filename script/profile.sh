#!/usr/bin/env bash

# 쉬고있는 profile 찾기 : real1이 사용 중이면 real2가 쉬고있고, 반대면 real1이 쉬고있음

function find_idle_profile()
{
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile) #현재 엔진엑스가 바라보고 있는 스프링 부트가 정상적으로 수행 중인지 확인

  if [ ${RESPONSE_CODE} -ge 400 ] #400 보다 크면(즉, 40x,50x에러 모두포함)

  then
      CURRENT_PROFILE=real2
  else
      CURRENT_PROFILE=$(curl -s http://localhost/profile)
  fi

  if [ ${CURRENT_PROFILE} == real1 ]
  then
    IDLE_PROFILE=real2 #엔진엑스와 연결되지 않은 profile
  else
    IDLE_PROFILE=real1
  fi

  echo "${IDLE_PROFILE}" #값을 반환하는 기능이 없어 echo로 출력후 값을 잡아서 사용, 중간에 echo사용하면 안됨
}

#쉬고있는 profile의 port 찾기
function find_idle_port()
{
  IDLE_PROFILE=$(find_idle_profile)

  if [ ${IDLE_PROFILE} == real1 ]
  then
      echo "8081"
  else
      echo "8082"
  fi
}