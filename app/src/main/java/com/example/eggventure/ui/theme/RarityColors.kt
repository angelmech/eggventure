package com.example.eggventure.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object RarityColors {

    private val MYTHICAL = listOf(
        Color(0xFFE600FF),
        Color(0xFF224CE2),
        Color(0xFF42DA20) ,
        Color(0xFFFF355D))
    val MYTHICAL_BRUSH = Brush.linearGradient(MYTHICAL)

    val MYTHICAL_TEXT = Color(0xFFFF0000)

    val COMMON = Color(0xFFBABABA)
    val RARE = Color(0xFF0093FF)
    val EPIC = Color(0xFF7100B8)
    val LEGENDARY = Color(0xFFFFA000)
}