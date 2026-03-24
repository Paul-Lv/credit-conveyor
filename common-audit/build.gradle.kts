plugins {
    java
    id("java-library")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":common-dto"))  // если AuditEvent использует что-то из common-dto


    implementation("org.springframework.kafka:spring-kafka:3.1.4")
    implementation("org.springframework:spring-context:6.1.5")
    implementation("org.springframework:spring-aop:6.1.5")
    implementation("org.aspectj:aspectjweaver:1.9.22")

    implementation("org.slf4j:slf4j-api:2.0.12")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

java {
    withSourcesJar()
}