plugins {
    id("io.spring.dependency-management") version "1.1.7"
    id("org.springframework.boot") version "3.5.0"
    id("java")
    application
}

group = "com.datadoghq.pej"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencyManagement {
    overriddenByDependencies(false)
    imports {
        mavenBom("io.netty:netty-bom:4.1.121.Final")
    }
}

dependencies {
    implementation("io.vertx:vertx-core:4.5.10")
    implementation("io.vertx:vertx-hazelcast:4.5.10")
    implementation("com.hazelcast:hazelcast:5.5.0")
    implementation("org.springframework.boot:spring-boot-starter:3.5.0")
}

application {
    mainClass.set("com.datadoghq.pej.consumer.ConsumerApplication")
}
