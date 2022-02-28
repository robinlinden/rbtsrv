plugins {
    kotlin("jvm")
}

version = "0.1.0"

dependencies {
    implementation(project(":common"))

    // OPC
    implementation("org.eclipse.milo:sdk-server:0.3.6")
}
