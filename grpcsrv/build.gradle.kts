import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm")
    idea
    id("com.google.protobuf") version "0.8.10"
}

group = "ltd.evilcorp"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // grpc/protobuf
    implementation("io.grpc:grpc-netty-shaded:1.25.0")
    implementation("io.grpc:grpc-stub:1.25.0")
    implementation("io.grpc:grpc-protobuf:1.25.0")

    // Generated code in grpc needs this.
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    // NATS
    implementation("io.nats:jnats:2.6.6")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
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