package com.bzolyomi.shoppinglist.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM

fun showShortToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun ErrorText(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.error,
        modifier = Modifier.padding(start = PADDING_MEDIUM)
    )
}
