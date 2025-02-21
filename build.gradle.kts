plugins {
    java
    id("war")
}

group = "ru.abramov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-webmvc:6.2.2")
    implementation("jakarta.servlet:jakarta.servlet-api:6.1.0")

    implementation("org.thymeleaf:thymeleaf-spring6:3.1.3.RELEASE")

    implementation("org.springframework.data:spring-data-jdbc:3.4.3")
    runtimeOnly("org.postgresql:postgresql:42.7.5")

    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    runtimeOnly("org.glassfish:jakarta.el:4.0.2")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    testImplementation("org.springframework:spring-test:6.1.3")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks.war {
    archiveFileName.set(
        System.getenv("TOMCAT_WAR_NAME") ?: "app.war"
    )
}