 # shellcheck disable=SC2046
export $(grep -v '^#' .env | xargs)

mvn clean package spring-boot:repackage

./target/blog-0.0.1-SNAPSHOT.jar