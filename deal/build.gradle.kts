
plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation("org.liquibase:liquibase-core")
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.0")

    // Для JSONB в PostgreSQL
//    implementation("io.hypersistence:hypersistence-utils-hibernate-60:3.7.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.mockito:mockito-core")
}

//
//plugins {
//    java
//    id("org.springframework.boot") version "3.2.4"
//    id("io.spring.dependency-management") version "1.1.4"
//}
//
//group = "com.example"
//version = "0.0.1-SNAPSHOT"
//
//java {
//    toolchain {
//        languageVersion = JavaLanguageVersion.of(17)
//    }
//}
//
//configurations {
//    compileOnly {
//        extendsFrom(configurations.annotationProcessor.get())
//    }
//}
//
//repositories {
//    mavenCentral()
//}
//
//dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-web:3.2.4")
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.4")
//    implementation("org.postgresql:postgresql:42.7.3")
//    implementation("org.liquibase:liquibase-core:4.26.0")
//    implementation("org.projectlombok:lombok:1.18.32")
//    annotationProcessor("org.projectlombok:lombok:1.18.32")
//
//    implementation("org.mapstruct:mapstruct:1.5.5.Final")
//    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
//
//    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
//
//    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.0")
//
//    implementation("com.vladmihalcea:hibernate-types-52:2.21.1")
//
//    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.4")
//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
//    testImplementation("org.mockito:mockito-core:5.11.0")
//}
//
//tasks.withType<Test> {
//    useJUnitPlatform()
//}