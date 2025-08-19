#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app/build/libs
PROJECT_NAME=dropmap

#echo ">Build 파일 복사"
#echo "> cp $REPOSITORY/zip/build/libs/*.war $REPOSITORY/"

#cp $REPOSITORY/zip/build/libs/*.war $REPOSITORY/

echo "> 새 애플리케이션 배포"
WAR_NAME=$(ls -tr $REPOSITORY/*.war | tail -n 1) #-t : 수정시간 순서로 , -r 거꾸로, tail -n 1 마지막 한줄을 출력

echo "> WAR Name: $WAR_NAME"

echo "> $WAR_NAME 에 실행권한 추가"

chmod +x $WAR_NAME

echo "> $WAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

echo "> $WAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다"
nohup java \
-Dspring.config.location=classpath:/application.properties,classpath:/application-common.properties,/home/ec2-user/app/application-$IDLE_PROFILE.properties,/home/ec2-user/app/application-variable.properties \
-Dspring.profiles.active=$IDLE_PROFILE \
-jar  $WAR_NAME > $REPOSITORY/../../nohup.out 2>&1 &