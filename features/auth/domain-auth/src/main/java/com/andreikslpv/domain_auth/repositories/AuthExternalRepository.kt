package com.andreikslpv.domain_auth.repositories

import com.andreikslpv.common.Response
import com.andreikslpv.domain.entities.CardEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthExternalRepository {

    suspend fun createUser(uid: String): Flow<Response<Boolean>>

    suspend fun deleteUserInDb(uid: String): Flow<Response<Boolean>>

    fun removeAllFromCollection(uid: String): Flow<Response<Boolean>>

    fun getCollection(): MutableStateFlow<List<String>>

    fun getHistory(): MutableStateFlow<List<CardEntity>>

    fun addToUsersCollection(uid: String, cardId: String)

    fun removeFromUsersCollection(uid: String, cardId: String)

    fun addToCardsCollection(uid: String, card: CardEntity)

    fun removeFromCardsCollection(uid: String, card: CardEntity)

    suspend fun getPrivacyPolicy(): Flow<Response<String>>

}