package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import com.example.config.getAwsConfig
import com.example.routes.uploadRoute
import io.ktor.server.routing.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val awsConfig = getAwsConfig()

    configureSerialization()
//    configureRouting(awsConfig)
    routing {
        uploadRoute(awsConfig)
    }
}
