@file:OptIn(ExperimentalMaterial3Api::class)

package com.petal.browser.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val TrackHeight = 40.dp
private val ThumbSize = 28.dp

/**
 * Material 3 Expressive Fancy Slider with smooth pill progress track,
 * dynamic thumb elevation, and responsive gesture feedback.
 */
@Composable
fun StrideSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    enabled: Boolean = true,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    val fraction = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f)
    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = 800f
        ),
        label = "fancySliderFraction"
    )

    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        enabled = enabled,
        onValueChangeFinished = onValueChangeFinished,
        modifier = modifier.height(TrackHeight),
        thumb = {
            Box(
                modifier = Modifier
                    .size(ThumbSize)
                    .shadow(elevation = 6.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .background(
                        if (enabled) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onPrimary)
                )
            }
        },
        track = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TrackHeight)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = animatedFraction)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (enabled) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.outlineVariant
                        )
                )
            }
        }
    )
}
