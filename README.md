## Сдача проектной работы третьего спринта (Создание блога) 

## ✅  Настройка окружения

🔧 1. Устанавливаем Tomcat
```bash
brew install tomcat
```

🔧 2. Проверяем установку
```bash
catalina version
```

🔧 3. Определяем путь к Tomcat для настройки переменных окружений
```bash
brew info tomcat
```

🔧 4. Устанавливаем переменное окружение в .env
```
TOMCAT_HOME=
TOMCAT_PORT=
TOMCAT_WAR_NAME=blog.war
UPLOAD_IMAGE_DIR=

DB_URL=jdbc:postgresql:
DB_USERNAME=
DB_PASSWORD=

TEST_UPLOAD_IMAGE_DIR=

TEST_DB_URL=jdbc:postgresql:
TEST_DB_USERNAME=
TEST_DB_PASSWORD=
```

## ✅ Сборка/деплой в Tomcat
```bash
chmod +x deploy.sh
./deploy.sh
```

## ✅ Вывод логов из Tomcat
```bash
chmod +x logs.sh
./logs.sh
```

## ✅ Запуск тестов
```bash
chmod +x run-test.sh
./run-test.sh
```