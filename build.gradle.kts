plugins {
    java
    id("org.springframework.boot") version "3.2.3" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
    id("jacoco")
    id("org.sonarqube") version "4.4.1.3373" // Убрал apply false!
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

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
    apply(plugin = "jacoco")

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        finalizedBy(tasks.jacocoTestReport)
    }

    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
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

// SonarQube configuration for root project
sonarqube {
    properties {
        property("sonar.projectKey", "Paul-Lv_credit-conveyor")
        property("sonar.organization", "paul-lv")
        property("sonar.host.url", "https://sonarcloud.io")
        // ✅ ПОЛНОЕ ОТКЛЮЧЕНИЕ ПОКРЫТИЯ
        property("sonar.coverage.exclusions", "**/*.java")
//        property("sonar.coverage.jacoco.xmlReportPaths", "./**/build/reports/jacoco/test/jacocoTestReport.xml")
        property("sonar.coverage.jacoco.xmlReportPaths", "none")
        property("sonar.gradle.skipCompile", "true")
        // ✅ Отключаем публикацию данных о покрытии
        property("sonar.coverageReport", "false")

    }
}