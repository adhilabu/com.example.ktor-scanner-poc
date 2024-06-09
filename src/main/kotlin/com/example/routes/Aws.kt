package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import java.io.File
import com.example.config.AwsConfig
import com.example.service.TextractService
import io.ktor.http.HttpStatusCode

fun Route.uploadRoute(awsConfig: AwsConfig) {
    post("/aws_upload") {
        val multipart = call.receiveMultipart()
        var fileDescription = ""
        var fileName = ""

        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    if (part.name == "description") {
                        fileDescription = part.value
                    }
                }
                is PartData.FileItem -> {
                    fileName = part.originalFileName as String
                    val uploadDir = File("uploads")
                    if (!uploadDir.exists()) {
                        uploadDir.mkdir()
                    }
                    val file = File(uploadDir, fileName)
                    part.streamProvider().use { input ->
                        file.outputStream().buffered().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
                else -> {}
            }
            part.dispose()
        }

        if (fileName.isNotEmpty()) {
            val textractService = TextractService(awsConfig)
            val detectedText = textractService.detectText("pancardfiles/$fileName", fileName)
            val dataset = textractService.extractData(detectedText)

            call.respond(HttpStatusCode.OK, dataset)
        } else {
            call.respond(HttpStatusCode.BadRequest, "Missing file")
        }
    }
}