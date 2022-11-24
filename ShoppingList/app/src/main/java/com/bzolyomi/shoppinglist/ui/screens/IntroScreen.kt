package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.util.Constants.INTRO_SCREEN_BETWEEN_ANIMATIONS_DELAY
import com.bzolyomi.shoppinglist.util.Constants.INTRO_SCREEN_FINISH_DELAY
import com.bzolyomi.shoppinglist.util.Constants.INTRO_SCREEN_INITIAL_DELAY
import com.bzolyomi.shoppinglist.util.Constants.INTRO_SCREEN_SPRING_ANIMATION_FINISH_POSITION
import com.bzolyomi.shoppinglist.util.Constants.INTRO_SCREEN_SPRING_ANIMATION_START_POSITION
import com.bzolyomi.shoppinglist.util.Constants.INTRO_SCREEN_ZOOM_ANIMATION_DURATION
import com.bzolyomi.shoppinglist.util.Constants.INTRO_SCREEN_ZOOM_ANIMATION_FINISH_SIZE
import com.bzolyomi.shoppinglist.util.Constants.INTRO_SCREEN_ZOOM_ANIMATION_START_SIZE
import kotlinx.coroutines.delay

@Composable
fun IntroScreen(
    isInDarkMode: Boolean,
    onAnimationEnded: () -> Unit,
    modifier: Modifier
) {
    var startZoomAnimation by remember { mutableStateOf(false) }
    var startSpringAnimation by remember { mutableStateOf(false) }

    val imageSize by animateDpAsState(
        targetValue = if (startZoomAnimation)
            INTRO_SCREEN_ZOOM_ANIMATION_FINISH_SIZE else INTRO_SCREEN_ZOOM_ANIMATION_START_SIZE,
        animationSpec = tween(
            durationMillis = INTRO_SCREEN_ZOOM_ANIMATION_DURATION,
            easing = FastOutLinearInEasing
        )
    )
    val imagePositionY by animateDpAsState(
        targetValue = if (startSpringAnimation)
            INTRO_SCREEN_SPRING_ANIMATION_FINISH_POSITION
        else INTRO_SCREEN_SPRING_ANIMATION_START_POSITION,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isInDarkMode) Color.Black else Color.White)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(
                id = if (isInDarkMode) R.drawable.logo_dark else R.drawable.logo
            ),
            contentDescription = stringResource(R.string.content_description_application_logo),
            modifier = Modifier
                .size(imageSize)
                .offset(x = 0.dp, y = imagePositionY)
        )
    }

    LaunchedEffect(true) {
        delay(INTRO_SCREEN_INITIAL_DELAY)
        startZoomAnimation = true
        delay(INTRO_SCREEN_BETWEEN_ANIMATIONS_DELAY)
        startSpringAnimation = true
        delay(INTRO_SCREEN_FINISH_DELAY)
        onAnimationEnded()
    }
}