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
fun CreatureCard(creature: CreatureEntity) {

    val backgroundBrush = creature.type.color

    val borderModifier = Modifier.border(
        width = 4.dp,
        brush = creature.rarity.borderColor,
        shape = RoundedCornerShape(12.dp)
    )

    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(210.dp)
            .then(borderModifier),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundBrush, shape = RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                Image(
                    painter = painterResource(id = creature.imageResId),
                    contentDescription = creature.creatureName,
                    modifier = Modifier.size(96.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    creature.creatureName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(creature.hatchedAt)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))
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
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
