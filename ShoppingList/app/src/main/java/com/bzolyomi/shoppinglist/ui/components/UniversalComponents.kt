package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.data.ShoppingItemEntity
import com.bzolyomi.shoppinglist.util.Constants.ELEVATION_MEDIUM
import com.bzolyomi.shoppinglist.util.Constants.ELEVATION_SMALL
import com.bzolyomi.shoppinglist.util.Constants.GROUP_CARD_FADE_IN_DURATION
import com.bzolyomi.shoppinglist.util.Constants.GROUP_CARD_FADE_IN_INITIAL_ALPHA
import com.bzolyomi.shoppinglist.util.Constants.GROUP_CARD_FADE_OUT_DURATION
import com.bzolyomi.shoppinglist.util.Constants.PADDING_LARGE
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM
import com.bzolyomi.shoppinglist.util.Constants.PADDING_SMALL
import com.bzolyomi.shoppinglist.util.Constants.PADDING_X_LARGE
import com.bzolyomi.shoppinglist.util.Constants.PADDING_ZERO
import com.bzolyomi.shoppinglist.util.Constants.SIZE_MEDIUM

@Composable
fun SubmitButton(onSubmitButtonClicked: () -> Unit) {
    Button(onClick = onSubmitButtonClicked) {
        Text(text = stringResource(R.string.submit_button_text))
    }
}

@Composable
fun GroupCard(
    titleGroupName: String,
    shoppingList: List<ShoppingItemEntity>,
    onOpenGroupIconClicked: () -> Unit,
    modifier: Modifier
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val titleBottomPaddingDp by animateDpAsState(if (isExpanded) PADDING_ZERO else PADDING_MEDIUM)
    val cardElevation = if (isExpanded) ELEVATION_MEDIUM else ELEVATION_SMALL

    Card(
        elevation = cardElevation,
        modifier = modifier.padding(PADDING_SMALL),
        shape = MaterialTheme.shapes.large
    ) {
        Column {
            Row(
                modifier = modifier
                    .padding(
                        start = PADDING_LARGE,
                        top = PADDING_MEDIUM,
                        end = PADDING_MEDIUM,
                        bottom = titleBottomPaddingDp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CardTitle(titleGroupName, modifier.weight(1f))
                ExpandIcon(
                    isExpanded = isExpanded,
                    onExpandIconClicked = { isExpanded = !isExpanded },
                    modifier = modifier.weight(1f)
                )
            }
            CardContent(
                isExpanded = isExpanded,
                shoppingList = shoppingList,
                onOpenGroupIconClicked = onOpenGroupIconClicked,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun ColumnScope.CardContent(
    isExpanded: Boolean,
    shoppingList: List<ShoppingItemEntity>,
    onOpenGroupIconClicked: () -> Unit,
    modifier: Modifier
) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(
            initialAlpha = GROUP_CARD_FADE_IN_INITIAL_ALPHA,
            animationSpec = tween(durationMillis = GROUP_CARD_FADE_IN_DURATION)
        ),
        exit = shrinkVertically() + fadeOut(
            animationSpec = tween(durationMillis = GROUP_CARD_FADE_OUT_DURATION))
    ) {
        Row(
            modifier = modifier.padding(
                start = PADDING_X_LARGE,
                top = PADDING_ZERO,
                end = PADDING_LARGE,
                bottom = PADDING_LARGE
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                for (item in shoppingList) {
                    Text(
                        text = item.itemName + " -- " + item.itemQuantity + " " + item.itemUnit,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }
            Spacer(modifier = modifier.weight(1f))
            IconButton(onClick = onOpenGroupIconClicked) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary
                )
            }
        }
    }
}

@Composable
private fun ExpandIcon(
    isExpanded: Boolean,
    onExpandIconClicked: () -> Unit,
    modifier: Modifier
) {
    Column {
        IconButton(
            onClick = onExpandIconClicked
        ) {
            Surface(shape = CircleShape, modifier = modifier.size(SIZE_MEDIUM)) {
                Icon(
                    imageVector = if (isExpanded) {
                        Icons.Filled.ExpandLess
                    } else Icons.Filled.ExpandMore,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary
                )
            }
        }
    }
}

@Composable
private fun CardTitle(titleGroupName: String, modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = titleGroupName,
            style = MaterialTheme.typography.h5.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}