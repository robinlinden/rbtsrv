import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm")
    idea
    id("com.google.protobuf") version "0.8.11"
}

version = "0.1.0"

dependencies {
    implementation(project(":common"))

    implementation(kotlin("stdlib-jdk8"))

    // grpc/protobuf
    implementation("io.grpc:grpc-netty-shaded:1.25.0")
    implementation("io.grpc:grpc-stub:1.25.0")
    implementation("io.grpc:grpc-protobuf:1.25.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.5.2")
    testImplementation("io.mockk:mockk:1.9.3")
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

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
