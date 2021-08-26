package me.obsilabor.noriskclientbot.data

import kotlinx.serialization.Serializable

@Serializable
data class ToxicityResult(
    val attributeScores: AttributeScores,
    val languages: ArrayList<String>,
    val detectedLanguages: ArrayList<String>
)

@Serializable
data class AttributeScores(
    val TOXICITY: Toxicity
)

@Serializable
data class Toxicity(
    val spanScores: ArrayList<SpanScore>?,
    val summaryScore: SummaryScore?
)

@Serializable
data class SpanScore(
    val begin: Int,
    val end: Int,
    val score: SummaryScore
)

@Serializable
data class SummaryScore(
    val value: Double,
    val type: String
)

@Serializable
data class Request(
    val comment: RequestComponent
)

@Serializable
data class RequestComponent(
    val text: TextComponent,
    val languages: ArrayList<String>,
    val requestedAttributeScores: AttributeScores
)

@Serializable
data class TextComponent(
    val text: String
)





