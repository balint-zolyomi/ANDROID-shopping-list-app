package com.bzolyomi.shoppinglist

import androidx.lifecycle.ViewModel
import com.bzolyomi.shoppinglist.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {
    val secretNumber = 5
}