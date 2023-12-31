package com.andreikslpv.data_auth.di

import com.andreikslpv.data_auth.AuthFirebaseRepository
import com.andreikslpv.domain_auth.repositories.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun providesAuthRepository(authRepository: AuthFirebaseRepository): AuthRepository {
        return authRepository
    }
}