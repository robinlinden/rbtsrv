plugins {
    kotlin("jvm")
}

version = "0.1.0"

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // NATS
    api("io.nats:jnats:2.6.6")
}
