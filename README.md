---

# Ktor Scanner POC

This project is a Proof of Concept (POC) for a Ktor-based scanner application. It demonstrates the basic structure and functionalities of a web service built using Ktor, a Kotlin framework for building asynchronous servers and clients in connected systems.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Setup](#setup)
- [Usage](#usage)
- [Configuration](#configuration)
- [Contributing](#contributing)

## Features

- Ktor framework setup
- Basic routing and request handling
- Simple scanning functionality
- JSON response support
- Logging configuration
- AWS S3 integration for uploading and processing Pan Card images

## Prerequisites

Before you begin, ensure you have met the following requirements:

- You have installed [Kotlin](https://kotlinlang.org/)
- You have installed [Gradle](https://gradle.org/)
- You have a compatible JVM installed (Java 8 or higher)
- You have an AWS account and necessary credentials

## Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/adhilabu/com.example.ktor-scanner-poc.git
    cd com.example.ktor-scanner-poc
    ```

2. Install dependencies:

    ```bash
    ./gradlew dependencies
    ```

## Setup

1. Update the `application.yaml` file with your AWS and Ktor server configurations (refer `application.yaml_backup`):

    ```yaml
    ktor:
        access_key_id: <your_access_key_id>
        secret_access_key: <your_secret_access_key>
        region: <your_region>
        bucket_name: <your_s3_bucket_name>
        application:
            modules:
                - com.example.ApplicationKt.module
        deployment:
            port: 8080
    ```

## Usage

1. Build the application:

    ```bash
    ./gradlew build
    ```

2. Start the server:

    ```bash
    ./gradlew run
    ```

3. Send a POST request to `http://localhost:8080/aws_upload` with the following form data:

    - `image`: `<pan_card_image_file>`

    > Note: Replace `<pan_card_image_file>` with the path to your Pan Card image file.

    The server will respond with a JSON object containing the extracted Pan Card data, such as name, father's name, date of birth, etc.

## API Endpoint

- **POST** `/aws_upload` - Accepts Pan Card image data and returns extracted information.

## Configuration

Configuration settings for the Ktor server can be found and modified in the `application.yaml` file. Here you can change the server port, configure logging, and adjust other server settings as needed.

## Contributing

Contributions are welcome! Please follow these steps to contribute:

1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature-name`)
3. Make your changes
4. Commit your changes (`git commit -m 'Add some feature'`)
5. Push to the branch (`git push origin feature/your-feature-name`)
6. Create a new Pull Request

---
