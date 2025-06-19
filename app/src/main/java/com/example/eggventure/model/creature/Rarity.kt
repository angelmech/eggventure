package com.example.eggventure.model.creature

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

enum class Rarity(
    val displayName: String,
    val borderColor: Brush,
    val textColor: Color
) {
    COMMON(
        "COMMON",
        Brush.linearGradient(
            colors = listOf(Color(0xFFBABABA), Color(0xFFD6D6D6)),
            tileMode = TileMode.Clamp
        ),
        Color(0xFFBABABA)
    ),
    RARE(
        "RARE",
        Brush.linearGradient(
            colors = listOf(Color(0xFF0093FF), Color(0xFF66CCFF)),
            tileMode = TileMode.Clamp
        ),
        Color(0xFF0093FF)
    ),
    EPIC(
        "EPIC",
        Brush.linearGradient(
            colors = listOf(Color(0xFF7100B8), Color(0xFFD287FF)),
            tileMode = TileMode.Clamp
        ),
        Color(0xFF7100B8)
    ),
    LEGENDARY(
        "LEGENDARY",
        Brush.linearGradient(
            colors = listOf(Color(0xFFFFA000), Color(0xFFFFD54F)),
            tileMode = TileMode.Clamp
        ),
        Color(0xFFFFA000)
    ),
    MYTHICAL(
        "MYTHICAL",
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFE600FF),
                Color(0xFF224CE2),
                Color(0xFF42DA20),
                Color(0xFFFF355D)),
            tileMode = TileMode.Clamp
        ),
        Color(0xFFFF0000)
    )
}