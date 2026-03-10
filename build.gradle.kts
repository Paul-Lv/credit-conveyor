plugins {
    java
    id("org.springframework.boot") version "3.2.3" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

//
allprojects {
    group = "com.example"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/snapshot") }
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")


    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
//        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<JavaExec> {
        val port = project.findProperty("jmxPort")?.toString()

        if (port != null) {
            jvmArgs = listOf(
                "-Dcom.sun.management.jmxremote",
                "-Dcom.sun.management.jmxremote.port=$port",
                "-Dcom.sun.management.jmxremote.rmi.port=$port",
                "-Dcom.sun.management.jmxremote.authenticate=false",
                "-Dcom.sun.management.jmxremote.ssl=false",
                "-Djava.rmi.server.hostname=localhost"
            )
        }
    }
}

//repositories {
//	mavenCentral()
//	maven { url = uri("https://repo.spring.io/snapshot") }
//}
//
//dependencies {
//	implementation("org.springframework.boot:spring-boot-starter")
//	testImplementation("org.springframework.boot:spring-boot-starter-test")
//	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//}
//
//tasks.withType<Test> {
//	useJUnitPlatform()
//}
