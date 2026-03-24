plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
//    java
}


dependencies {
    implementation(project(":common-audit"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.kafka:spring-kafka")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.bootJar {
    mainClass.set("com.example.audit.AuditApplication")
}

tasks.jar {
    enabled = false
}

