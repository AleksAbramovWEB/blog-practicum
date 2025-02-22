#!/bin/bash

 # shellcheck disable=SC2046
export $(grep -v '^#' .env | xargs)

if [ ! -d "$TOMCAT_HOME" ]; then
  echo "Ошибка: Папка Tomcat ($TOMCAT_HOME) не найдена!"
  exit 1
fi

echo "Деплой проекта в Tomcat"

echo "Сборка проекта..."
./gradlew war || {
  echo "Ошибка сборки";
  exit 1;
}

echo "🛑 Остановка Tomcat..."
brew services stop tomcat

echo "📦 Копирование $TOMCAT_WAR_NAME в Tomcat..."
cp "build/libs/$TOMCAT_WAR_NAME" "$TOMCAT_HOME/webapps/"

rm -rf "$TOMCAT_HOME/webapps/ROOT"

mv "$TOMCAT_HOME/webapps/$TOMCAT_WAR_NAME" "$TOMCAT_HOME/webapps/ROOT.war"

echo "📦 Копирование статических файлов в root static..."
cp -R src/main/resources/static/* "$TOMCAT_HOME/webapps/static/"

echo "Создаём setenv.sh для передачи переменных в Tomcat..."

cat <<EOF > "$TOMCAT_HOME/bin/setenv.sh"
#!/bin/bash
export DB_URL="$DB_URL"
export DB_USERNAME="$DB_USERNAME"
export DB_PASSWORD="$DB_PASSWORD"
export UPLOAD_IMAGE_DIR="$UPLOAD_IMAGE_DIR"
EOF

chmod +x "$TOMCAT_HOME/bin/setenv.sh"

echo "Запуск Tomcat..."
CATALINA_OPTS="-Dserver.port=$TOMCAT_PORT" brew services start tomcat

echo "Деплой завершён! Доступно по адресу: http://localhost:$TOMCAT_PORT"