package com.example.service

import com.example.config.AwsConfig
import com.example.utils.PanCardDetails
import com.example.utils.extractDataFromTextractResponse
import kotlinx.coroutines.delay
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.textract.TextractClient
import software.amazon.awssdk.services.textract.model.*
import java.io.File

class TextractService(private val awsConfig: AwsConfig) {

    private val s3Client: S3Client by lazy {
        val awsCreds = AwsBasicCredentials.create(awsConfig.accessKeyId, awsConfig.secretAccessKey)
        S3Client.builder()
            .region(Region.of(awsConfig.region))
            .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
            .build()
    }

    private val textractClient: TextractClient by lazy {
        val awsCreds = AwsBasicCredentials.create(awsConfig.accessKeyId, awsConfig.secretAccessKey)
        TextractClient.builder()
            .region(Region.of(awsConfig.region))
            .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
            .build()
    }

    fun uploadFileToS3(bucket: String, key: String, file: File) {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()
        s3Client.putObject(putObjectRequest, file.toPath())
    }

    suspend fun detectText(bucketKey: String, fileName: String): String {

        // Upload the file to S3
        val file = File("uploads/$fileName")
        uploadFileToS3(awsConfig.bucketName, bucketKey, file)

        // Create a request to start document text detection
        val startDocumentTextDetectionRequest = StartDocumentTextDetectionRequest.builder()
            .documentLocation {
                it.s3Object {
                    it.bucket(awsConfig.bucketName)
                    it.name(bucketKey)
                }
            }
            .build()

        // Start text detection
        val textDetectionResponse = textractClient.startDocumentTextDetection(startDocumentTextDetectionRequest)
        val jobId = textDetectionResponse.jobId()

        // Poll for the job status
        var jobStatus = ""
        var maxTries = 20
        while (maxTries > 0 && jobStatus != "SUCCEEDED") {
            delay(1000) // Wait for 1 seconds before polling again
            val getDocumentTextDetectionRequest = GetDocumentTextDetectionRequest.builder()
                .jobId(jobId)
                .build()

            val documentTextDetectionResponse = try {
                textractClient.getDocumentTextDetection(getDocumentTextDetectionRequest)
            } catch (e: TextractException) {
                e.printStackTrace()
                throw IllegalStateException("Error getting document text detection: ${e.message}")
            }

            jobStatus = documentTextDetectionResponse.jobStatus().toString()
            maxTries--
        }

        if (jobStatus == "SUCCEEDED") {
            // Fetch the results
            val getDocumentTextDetectionRequest = GetDocumentTextDetectionRequest.builder()
                .jobId(jobId)
                .build()

            val documentTextDetectionResponse = textractClient.getDocumentTextDetection(getDocumentTextDetectionRequest)
            return documentTextDetectionResponse.blocks().joinToString("\n") { it.text() ?: "" }
        } else {
            throw IllegalStateException("Textract job did not succeed")
        }
    }

    fun extractData(detectedText: String): PanCardDetails {
        return extractDataFromTextractResponse(detectedText)
    }
}
