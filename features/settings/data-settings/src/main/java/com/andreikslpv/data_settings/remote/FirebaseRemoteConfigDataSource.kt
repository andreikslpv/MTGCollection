package com.andreikslpv.data_settings.remote

import com.andreikslpv.common.Response
import com.andreikslpv.domain_settings.entities.SettingPrivacyPolicy
import com.andreikslpv.domain_settings.entities.SettingVersionSetsType
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRemoteConfigDataSource @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
) : RemoteSettingsDataSource {

    override suspend fun getVersionSetsType() = flow {
        remoteConfig.fetchAndActivate().await().also {
            emit(remoteConfig.getLong(SettingVersionSetsType().key).toInt())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getPrivacyPolicy() = flow {
        emit(Response.Loading)
        remoteConfig.fetchAndActivate().await().also {
            emit(Response.Success(remoteConfig.getString(SettingPrivacyPolicy().key)))
        }
    }.flowOn(Dispatchers.IO)

}