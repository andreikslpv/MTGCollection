package com.andreikslpv.data.auth

import android.net.Uri
import com.andreikslpv.common.Response
import com.andreikslpv.data.auth.entities.AccountDataEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthDataRepository {

    suspend fun signIn(idToken: String): Flow<Response<Boolean>>

    suspend fun signInAnonymously(): Flow<Response<Boolean>>

    fun signOut(): Flow<Response<Void>>

    fun getAuthState(): Flow<Boolean>

    fun getCurrentUser(): MutableStateFlow<AccountDataEntity?>

    suspend fun deleteUserInAuth(idToken: String): Flow<Response<Boolean>>

    suspend fun deleteUsersPhotoInDb(uid: String): Flow<Response<Boolean>>

    suspend fun editUserName(newName: String): Flow<Response<Boolean>>

    suspend fun changeUserPhoto(localUri: Uri): Flow<Response<Boolean>>

    suspend fun getPrivacyPolicy(): Flow<Response<String>>

}