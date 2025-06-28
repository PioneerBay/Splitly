package com.pioneerbay.splitly.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch

@Composable
fun SendSwipe(
    onSend: () -> Unit,
    disabled: Boolean = false,
) {
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val containerWidth = remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    // Calculate max offset leaving 60dp for the child
    val maxOffset = (containerWidth.value - 60.dp).coerceAtLeast(0.dp)

    // Calculate alpha based on swipe progress (fade out as swiping)
    val swipeProgress =
        if (maxOffset.value > 0) {
            (offsetX.value / with(density) { maxOffset.toPx() }).coerceIn(0f, 1f)
        } else {
            0f
        }
    val textAlpha = 1f - swipeProgress

    Box(
        Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                containerWidth.value = with(density) { coordinates.size.width.toDp() }
            }.padding(
                start =
                    if (disabled) {
                        0.dp
                    } else {
                        (offsetX.value.dp / density.density)
                            .coerceAtLeast(0.dp)
                            .coerceAtMost(maxOffset)
                    },
            ),
    ) {
        Box(
            Modifier
                .height(60.dp)
                .requiredWidthIn(min = 60.dp)
                .fillMaxWidth()
                .clip(CircleShape)
                .background(
                    if (disabled) Color.Gray else MaterialTheme.colorScheme.primary,
                ).padding(5.dp),
        ) {
            // The draggable circle
            Box(
                modifier =
                    Modifier
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .let { modifier ->
                            if (disabled) {
                                modifier
                            } else {
                                modifier.draggable(
                                    orientation = Orientation.Horizontal,
                                    state =
                                        rememberDraggableState { delta ->
                                            coroutineScope.launch {
                                                val newValue = offsetX.value + delta
                                                val maxOffsetPx = with(density) { maxOffset.toPx() }
                                                offsetX.snapTo(newValue.coerceIn(0f, maxOffsetPx))
                                            }
                                        },
                                    onDragStopped = {
                                        coroutineScope.launch {
                                            if (offsetX.value >= maxOffset.value * density.density) {
                                                // If swiped far enough, trigger send action
                                                onSend()
                                                Logger.d { "Drag stopped, sending money" }
                                            } else {
                                                // Otherwise, animate back to start

                                                offsetX.animateTo(
                                                    targetValue = 0f,
                                                    animationSpec =
                                                        spring(
                                                            dampingRatio = 0.8f,
                                                            stiffness = 200f,
                                                        ),
                                                )
                                                Logger.d { "Drag stopped and animated" }
                                            }
                                        }
                                    },
                                )
                            }
                        },
            )
        }

        // "Send money" text positioned in the center, outside the moving container
        Text(
            text = "Send money",
            color = if (disabled) Color.Gray.copy(alpha = 0.6f) else Color.White.copy(alpha = textAlpha),
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .align(Alignment.Center),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}
