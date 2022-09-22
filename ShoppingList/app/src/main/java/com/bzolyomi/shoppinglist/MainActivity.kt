package com.bzolyomi.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bzolyomi.shoppinglist.ui.screens.AddGroupScreen
import com.bzolyomi.shoppinglist.ui.screens.ItemGroupScreen
import com.bzolyomi.shoppinglist.ui.screens.ItemsScreen
import com.bzolyomi.shoppinglist.ui.theme.ShoppingListTheme
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity()
{

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    ItemsScreen(sharedViewModel = sharedViewModel)
//                    ItemGroupScreen(sharedViewModel = sharedViewModel)
                    AddGroupScreen(sharedViewModel = sharedViewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShoppingListTheme {

    }
}