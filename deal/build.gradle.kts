plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(project(":common-dto"))
    implementation(project(":common-validation"))
    implementation(project(":common-audit"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springframework.boot:spring-boot-starter-aop" )
    implementation("org.projectlombok:lombok")
    // Actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // Prometheus
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.0")
    implementation("org.springframework.kafka:spring-kafka")
    // MapStruct
    implementation("org.mapstruct:mapstruct:1.5.5.Final")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    // Lombok + MapStruct (чтобы они не конфликтовали)
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.mockito:mockito-core")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

