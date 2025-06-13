package com.example.eggventure.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.eggventure.model.creature.CreatureEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CreatureCard(creature: CreatureEntity) {

    val borderModifier = Modifier.border(
        width = 3.dp,
        brush = creature.rarity.borderColor,
        shape = RoundedCornerShape(12.dp)
    )

    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(180.dp)
            .then(borderModifier),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painter = painterResource(id = creature.imageResId),
                contentDescription = creature.creatureName,
                modifier = Modifier.size(96.dp)
            )

            Text(
                creature.creatureName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = creature.rarity.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = creature.rarity.textColor
            )

            Text(
                text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(creature.hatchedAt)),
                style = MaterialTheme.typography.bodySmall
            )

        }
    }
}
