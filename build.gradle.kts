import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm") version "1.3.61"
    idea
    id("com.google.protobuf") version "0.8.10"
    id("com.github.ben-manes.versions") version "0.27.0"
}

group = "ltd.evilcorp"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("io.grpc:grpc-netty-shaded:1.25.0")
    implementation("io.grpc:grpc-stub:1.25.0")
    implementation("io.grpc:grpc-protobuf:1.25.0")

    // Generated code in grpc needs this.
    implementation("javax.annotation:javax.annotation-api:1.3.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

buildscript {
    dependencies {
        classpath("com.github.ben-manes:gradle-versions-plugin:0.27.0")
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.11.1"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.25.0"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}
