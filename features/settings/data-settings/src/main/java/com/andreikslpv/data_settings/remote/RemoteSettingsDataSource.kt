package com.andreikslpv.data_settings.remote

import com.andreikslpv.common.Response
import kotlinx.coroutines.flow.Flow

interface RemoteSettingsDataSource {

    suspend fun getVersionSetsType(): Flow<Int>

    suspend fun getPrivacyPolicy(): Flow<Response<String>>

}