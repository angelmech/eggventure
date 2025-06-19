package com.example.eggventure.model.creature

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode

enum class CreatureType(
    val displayName: String,
    val color: Brush,
    val textColor: Color
) {
    RADIANT(
        "Radiant",
        Brush.linearGradient(
            colors = listOf(Color(0xFFFF0000), Color(0xFF000000), Color(0xFFFFE89C)),
            tileMode = TileMode.Clamp
        ),
        Color(0xFFFFD500)
    ),
    REGULAR(
        "Regular",
        Brush.linearGradient(
            colors = listOf(Color(0xFFFFA636), Color(0xFF633F00), Color(0xFF282828)),
            tileMode = TileMode.Clamp
        ),
        Color(0xFFFFA37E)
    ),
    SHADOW(
        "Shadow",
        Brush.linearGradient(
            colors = listOf(Color(0xFF000000), Color(0xFF1F1F1F), Color(0xFF505050)),
            tileMode = TileMode.Clamp
        ),
        Color(0xFF585858)
    ),
}


