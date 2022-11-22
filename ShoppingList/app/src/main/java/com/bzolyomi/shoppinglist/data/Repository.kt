package com.bzolyomi.shoppinglist.data

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(
    private val dao: DAO) {
}