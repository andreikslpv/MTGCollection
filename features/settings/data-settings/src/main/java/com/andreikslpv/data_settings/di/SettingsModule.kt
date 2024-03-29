package com.andreikslpv.data_settings.di

import android.content.Context
import com.andreikslpv.data_settings.SettingsRepositoryImpl
import com.andreikslpv.data_settings.local.LocalSettingsDataSource
import com.andreikslpv.data_settings.local.SharedPreferencesDataSource
import com.andreikslpv.data_settings.remote.FirebaseRemoteConfigDataSource
import com.andreikslpv.data_settings.remote.RemoteSettingsDataSource
import com.andreikslpv.domain_settings.repositories.SettingsRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SettingsModule {

    @Provides
    @Singleton
    fun provideLocalSettingsDataSource(
        @ApplicationContext applicationContext: Context,
    ): LocalSettingsDataSource {
        return SharedPreferencesDataSource(applicationContext)
    }

    @Provides
    @Singleton
    fun provideRemoteSettingsDataSource(
        remoteSettingsDataSource: FirebaseRemoteConfigDataSource
    ): RemoteSettingsDataSource {
        return remoteSettingsDataSource
    }

    @Provides
    @Singleton
    fun providesSettingsRepository(settingsRepository: SettingsRepositoryImpl): SettingsRepository {
        return settingsRepository
    }

    @Provides
    @Singleton
    fun provideRemoteConfigInstance(): FirebaseRemoteConfig {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val settings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }
        remoteConfig.setConfigSettingsAsync(settings)
        return remoteConfig
    }
}
