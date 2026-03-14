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
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
}

java {
    withSourcesJar()
}