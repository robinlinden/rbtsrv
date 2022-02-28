import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10" apply false
    id("com.github.ben-manes.versions") version "0.27.0"
}

buildscript {
    dependencies {
        classpath("com.github.ben-manes:gradle-versions-plugin:0.27.0")
    }
}

subprojects {
    group = "ltd.evilcorp"

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}
