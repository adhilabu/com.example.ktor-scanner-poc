package com.example.utils

import kotlinx.serialization.Serializable

@Serializable
data class PanCardDetails(
    val fullName: String,
    val panCard: String,
    val dateOfBirth: String,
    val fathersName: String
)

fun extractDataFromTextractResponse(detectedText: String): PanCardDetails {
    val fullNameRegex = Regex("Name\\s+([A-Z\\s]+)")
    val panCardRegex = Regex("Permanent Account Number Card\\s+([A-Z0-9]+)")
    val dateOfBirthRegex = Regex("\\d{2}/\\d{2}/\\d{4}")
    val fathersNameRegex = Regex("Father's Name\\s+([A-Z\\s]+)")

    val fullName = fullNameRegex.find(detectedText)?.groups?.get(1)?.value?.trim()?.split("\n")?.first() ?: ""
    val panCard = panCardRegex.find(detectedText)?.groups?.get(1)?.value?.trim() ?: ""
    val fathersName = fathersNameRegex.find(detectedText)?.groups?.get(1)?.value?.trim()?.split("\n")?.first() ?: ""

    // Extract last five lines to search for date of birth
    val lastFiveLines = detectedText.lines().takeLast(5).joinToString(" ")
    val dateOfBirth = dateOfBirthRegex.find(lastFiveLines)?.value ?: ""

    return PanCardDetails(
        fullName = fullName,
        panCard = panCard,
        dateOfBirth = dateOfBirth,
        fathersName = fathersName
    )
}
