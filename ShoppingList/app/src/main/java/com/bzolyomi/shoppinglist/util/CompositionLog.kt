package com.bzolyomi.shoppinglist.util

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import com.bzolyomi.shoppinglist.BuildConfig
import dagger.hilt.android.scopes.ViewModelScoped

@ViewModelScoped
class CompositionLog(var value: Int) {

    @Composable
    fun LogCompositions(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            val ref = remember { CompositionLog(0) }
            SideEffect { ref.value++ }
            Log.d(tag, "Compositions: $msg ${ref.value}")
        }
    }
}