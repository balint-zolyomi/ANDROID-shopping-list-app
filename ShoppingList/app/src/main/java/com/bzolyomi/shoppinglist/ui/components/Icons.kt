package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.size
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
import com.bzolyomi.shoppinglist.util.Constants.MORE_VERT_ICON_ROTATION_ANIMATION_END_DEGREES
import com.bzolyomi.shoppinglist.util.Constants.MORE_VERT_ICON_ROTATION_ANIMATION_START_DEGREES
import com.bzolyomi.shoppinglist.util.Constants.SIZE_ICONS_OFFICIAL

@Composable
fun MoreVertIcon(
    isExpanded: Boolean
) {
    val moreVertIconAngle: Float by animateFloatAsState(
        targetValue = if (isExpanded) {
            MORE_VERT_ICON_ROTATION_ANIMATION_END_DEGREES
        } else {
            MORE_VERT_ICON_ROTATION_ANIMATION_START_DEGREES
        }
    )

    Icon(
        imageVector = Icons.Filled.MoreVert,
        contentDescription = stringResource(R.string.content_description_icon_open_dropdown_menu),
        tint = MaterialTheme.colors.onBackground,
        modifier = Modifier.rotate(moreVertIconAngle)
    )
}

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

    IconButton(
        onClick = onExpandIconClicked
    ) {
        Icon(
            imageVector = Icons.Filled.ExpandMore,
            contentDescription = stringResource(
                R.string.content_description_icon_expand_group
            ),
            tint = MaterialTheme.colors.primary,
            modifier = modifier.rotate(expandIconAngle)
        )
    }
}

@Composable
fun OpenInNewIcon(
    onOpenGroupIconClicked: () -> Unit
) {
    IconButton(
        onClick = onOpenGroupIconClicked
    ) {
        Icon(
            imageVector = Icons.Filled.OpenInNew,
            contentDescription = stringResource(
                R.string.content_description_icon_open_in_new
            ),
            tint = MaterialTheme.colors.primary
        )
    }
}

@Composable
fun EraseTrailingIcon(onEraseTrailingIconClicked: () -> Unit) {
    IconButton(onClick = onEraseTrailingIconClicked) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.content_description_icon_erase_input_field),
            tint = MaterialTheme.colors.primary
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CheckboxIcon(
    isItemChecked: Boolean,
    onCheckboxClicked: () -> Unit,
    itemName: String,
    modifier: Modifier
) {
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        IconButton(
            onClick = onCheckboxClicked,
            modifier = modifier.size(SIZE_ICONS_OFFICIAL)
        ) {
            if (isItemChecked) {
                Icon(
                    imageVector = Icons.Filled.CheckBox,
                    contentDescription = stringResource(
                        R.string.content_description_icon_checkbox_item_done
                    ) + " (item: $itemName)"
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.CheckBoxOutlineBlank,
                    contentDescription = stringResource(
                        R.string.content_description_icon_checkbox_item_not_done
                    ) + " (item: $itemName)"
                )
            }
        }
    }
}

@Composable
fun DeleteItemIcon(
    onDeleteItemClicked: () -> Unit,
    itemName: String,
    modifier: Modifier
) {
        IconButton(
            onClick = onDeleteItemClicked,
            modifier = modifier
                .size(SIZE_ICONS_OFFICIAL)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.content_description_icon_delete_item) + " $itemName"
            )
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
                contentDescription = stringResource(R.string.content_description_icon_drag),
            )
        }
    }
}
