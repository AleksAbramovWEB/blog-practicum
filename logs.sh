#!/bin/bash

 # shellcheck disable=SC2046
export $(grep -v '^#' .env | xargs)

echo "Tomcat home: $TOMCAT_HOME"
LOG_FILE="$TOMCAT_HOME/logs/localhost.$(date +"%Y-%m-%d").log"

echo "Читаем лог: $LOG_FILE"
tail -n 200 -f "$LOG_FILE"