package com.bzolyomi.shoppinglist.di

import android.app.Application
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.bzolyomi.shoppinglist.SharedViewModel
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ShoppingListApplication : Application() {
}