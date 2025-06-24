package com.example.eggventure.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
fun CreatureCard(creature: CreatureEntity, expanded: Boolean = false) {
    val imageSize = if (expanded) 140.dp else 96.dp
    val cardHeight = if (expanded) 280.dp else 210.dp
    val backgroundBrush = creature.type.color
    val cornerRadius = if (expanded) 20.dp else 12.dp
    val padding = if (expanded) 16.dp else 8.dp
    val spacing = if (expanded) 12.dp else 6.dp
    val shape = RoundedCornerShape(cornerRadius)

    val borderModifier = Modifier.border(
        width = if (expanded) 6.dp else 4.dp,
        brush = creature.rarity.borderColor,
        shape = shape
    )


    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(cardHeight)
            .then(borderModifier),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundBrush, shape = shape)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(id = creature.imageResId),
                    contentDescription = creature.creatureName,
                    modifier = Modifier.size(imageSize)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = creature.creatureName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                            .format(Date(creature.hatchedAt)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(spacing))

                    Text(
                        text = creature.type.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = creature.rarity.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = creature.rarity.textColor
                    )
                }
            }
        }
    }
}

