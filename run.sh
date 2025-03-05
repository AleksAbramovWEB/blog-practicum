#!/bin/bash

set -e

# shellcheck disable=SC2046
export $(grep -v '^#' .env | xargs)

./mvnw clean package spring-boot:repackage

# shellcheck disable=SC2010
JAR_FILE=$(ls target/*.jar | grep -v 'original' | head -n 1)

if [[ -z "$JAR_FILE" ]]; then
  echo "JAR-файл не найден в target/."
  exit 1
fi

java -jar "$JAR_FILE"
