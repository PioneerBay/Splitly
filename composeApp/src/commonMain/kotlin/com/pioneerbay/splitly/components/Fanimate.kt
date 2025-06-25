package com.pioneerbay.splitly.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Fanimate(
    visible: Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    label: String = "AnimatedVisibility",
    onAnimationEnd: () -> Unit = {},
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    val transitionState = remember { MutableTransitionState(visible) }
    transitionState.targetState = visible

    AnimatedVisibility(
        visibleState = transitionState,
        modifier = modifier,
        enter = enter,
        exit = exit,
        label = label,
        content = content,
    )

    LaunchedEffect(transitionState.currentState, transitionState.targetState) {
        if (!transitionState.currentState && !transitionState.targetState) {
            onAnimationEnd.invoke()
        }
    }
}
