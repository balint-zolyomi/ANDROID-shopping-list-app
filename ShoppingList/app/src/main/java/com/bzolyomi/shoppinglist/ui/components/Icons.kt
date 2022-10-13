package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.util.Constants.EXPAND_ICON_ROTATION_ANIMATION_END_DEGREES
import com.bzolyomi.shoppinglist.util.Constants.EXPAND_ICON_ROTATION_ANIMATION_START_DEGREES
import com.bzolyomi.shoppinglist.util.Constants.SIZE_ICONS_OFFICIAL
import com.bzolyomi.shoppinglist.util.Constants.SIZE_MEDIUM

@Composable
fun ExpandIcon(
    isExpanded: Boolean,
    onExpandIconClicked: () -> Unit,
    modifier: Modifier
) {
    val expandIconAngle: Float by animateFloatAsState(
        targetValue = if (isExpanded) EXPAND_ICON_ROTATION_ANIMATION_END_DEGREES
        else EXPAND_ICON_ROTATION_ANIMATION_START_DEGREES
    )

    Column {
        IconButton(
            onClick = onExpandIconClicked
        ) {
            Surface(shape = CircleShape, modifier = modifier.size(SIZE_MEDIUM)) {
                Icon(
                    imageVector = Icons.Filled.ExpandMore,
                    contentDescription = stringResource(
                        R.string.content_description_expand_group_icon
                    ),
                    tint = MaterialTheme.colors.primary,
                    modifier = modifier.rotate(expandIconAngle)
                )
            }
        }
    }
}

@Composable
fun OpenInNewIcon(
    onOpenGroupIconClicked: () -> Unit
) {
    IconButton(onClick = onOpenGroupIconClicked) {
        Icon(
            imageVector = Icons.Filled.OpenInNew, contentDescription = stringResource(
                R.string.content_description_go_to_items_of_group_screen_icon
            ), tint = MaterialTheme.colors.primary
        )
    }
}

@Composable
fun EraseTrailingIcon(callback: () -> Unit) {
    IconButton(onClick = callback) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.content_description_erase_input_field),
            tint = MaterialTheme.colors.primary
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CheckboxIcon(
    isItemChecked: Boolean,
    onCheckboxClicked: () -> Unit,
    modifier: Modifier
) {
//    TODO
//    val (isChecked, setChecked) = remember { mutableStateOf(false) }
//    MaterialTheme {
//        Surface {
//            FavoriteButton(
//                isChecked = isChecked,
//                onClick = { setChecked(!isChecked) }
//            )
//        }
//    }
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        IconButton(
            onClick = onCheckboxClicked,
            modifier = modifier.size(SIZE_ICONS_OFFICIAL)
        ) {
            if (isItemChecked) {
                Icon(
                    imageVector = Icons.Filled.CheckBox,
                    contentDescription = stringResource(
                        R.string.content_description_checkbox_done_item
                    )
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.CheckBoxOutlineBlank,
                    contentDescription = stringResource(
                        R.string.content_description_checkbox_item_not_done
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeleteItemIcon(
    onDeleteItemClicked: () -> Unit,
    modifier: Modifier
) {
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        IconButton(
            onClick = onDeleteItemClicked,
            modifier = modifier.size(SIZE_ICONS_OFFICIAL)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.delete_item_icon)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DragIcon(
    modifier: Modifier
) {
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        IconButton(
            onClick = {},
            enabled = false,
            modifier = modifier.size(SIZE_ICONS_OFFICIAL)
        ) {
            Icon(
                imageVector = Icons.Filled.DragIndicator,
                contentDescription = stringResource(R.string.drag_icon_content_description),
            )
        }
    }
}
