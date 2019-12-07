plugins {
    kotlin("jvm")
}

version = "0.1.0"

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // OPC
    implementation("org.eclipse.milo:sdk-server:0.3.6")
}
