package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.util.Constants

@Composable
fun MoreVertIcon(
    isExpanded: Boolean
) {
    val moreVertIconAngle: Float by animateFloatAsState(
        targetValue = if (isExpanded) {
            Constants.MORE_VERT_ICON_ROTATION_ANIMATION_END_DEGREES
        } else {
            Constants.MORE_VERT_ICON_ROTATION_ANIMATION_START_DEGREES
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
        targetValue = if (isExpanded) Constants.EXPAND_ICON_ROTATION_ANIMATION_END_DEGREES
        else Constants.EXPAND_ICON_ROTATION_ANIMATION_START_DEGREES
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
