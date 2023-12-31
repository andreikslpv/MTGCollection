package com.andreikslpv.data_users.entities

import com.andreikslpv.data.CardFirebaseEntity

data class UserModel(
    var uid: String = "",
    val collection: ArrayList<String> = arrayListOf(),
    val history: ArrayList<CardFirebaseEntity> = arrayListOf(),
)
