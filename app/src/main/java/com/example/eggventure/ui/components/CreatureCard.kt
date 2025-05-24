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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.eggventure.model.creature.Creature
import com.example.eggventure.model.creature.Rarity
import com.example.eggventure.ui.theme.RarityColors

@Composable
fun CreatureCard(creature: Creature) {

    val borderModifier = when (creature.rarity) {
        Rarity.MYTHICAL -> Modifier.border(
            width = 3.dp,
            RarityColors.MYTHICAL_BRUSH,
            shape = RoundedCornerShape(12.dp)
        )
        Rarity.COMMON -> Modifier.border(3.dp, RarityColors.COMMON, RoundedCornerShape(12.dp))
        Rarity.RARE -> Modifier.border(3.dp, RarityColors.RARE, RoundedCornerShape(12.dp))
        Rarity.EPIC -> Modifier.border(3.dp, RarityColors.EPIC, RoundedCornerShape(12.dp))
        Rarity.LEGENDARY -> Modifier.border(3.dp, RarityColors.LEGENDARY, RoundedCornerShape(12.dp))
    }

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
                contentDescription = creature.name,
                modifier = Modifier.size(96.dp)
            )
            Text(
                creature.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = creature.rarity.name,
                style = MaterialTheme.typography.labelSmall,
                color = when (creature.rarity) {
                    Rarity.MYTHICAL -> RarityColors.MYTHICAL_TEXT
                    Rarity.COMMON -> RarityColors.COMMON
                    Rarity.RARE -> RarityColors.RARE
                    Rarity.EPIC -> RarityColors.EPIC
                    Rarity.LEGENDARY -> RarityColors.LEGENDARY
                }
            )
        }
    }
}
