package com.example.config

import io.ktor.server.application.*

data class AwsConfig(
    val accessKeyId: String,
    val secretAccessKey: String,
    val region: String,
    val bucketName: String
)

fun Application.getAwsConfig(): AwsConfig {
    val accessKeyId = environment.config.propertyOrNull("ktor.access_key_id")?.getString()
    val secretAccessKey = environment.config.propertyOrNull("ktor.secret_access_key")?.getString()
    val region = environment.config.propertyOrNull("ktor.region")?.getString()
    val bucketName = environment.config.propertyOrNull("ktor.bucket_name")?.getString()

    return AwsConfig(
        accessKeyId = accessKeyId ?: throw IllegalArgumentException("AWS Access Key ID not configured"),
        secretAccessKey = secretAccessKey ?: throw IllegalArgumentException("AWS Secret Access Key not configured"),
        region = region ?: throw IllegalArgumentException("AWS Region not configured"),
        bucketName = bucketName ?: throw IllegalArgumentException("AWS Bucket Name not configured")
    )
}
