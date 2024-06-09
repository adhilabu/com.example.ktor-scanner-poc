
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.11"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
// https://mvnrepository.com/artifact/aws.sdk.kotlin/aws-http-jvm
    runtimeOnly("aws.sdk.kotlin:aws-http-jvm:1.2.28")
// https://mvnrepository.com/artifact/aws.sdk.kotlin/aws-core-jvm
    implementation("aws.sdk.kotlin:aws-core-jvm:1.2.28")
// https://mvnrepository.com/artifact/aws.sdk.kotlin/auth-jvm
    implementation("aws.sdk.kotlin:auth-jvm:0.6.0-alpha")
// https://mvnrepository.com/artifact/aws.smithy.kotlin/aws-signing-common-jvm
    runtimeOnly("aws.smithy.kotlin:aws-signing-common-jvm:1.2.6")
    implementation("aws.sdk.kotlin:s3-jvm:1.2.28")
    implementation("software.amazon.awssdk:s3:2.17.106")
    implementation("software.amazon.awssdk:textract:2.17.106")
}
