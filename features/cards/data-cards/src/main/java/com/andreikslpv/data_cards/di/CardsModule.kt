package com.andreikslpv.data_cards.di

import com.andreikslpv.data_cards.CardsRepositoryImpl
import com.andreikslpv.data_cards.services.CardsApi
import com.andreikslpv.domain_cards.repositories.CardsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CardsModule {

    @Provides
    @Singleton
    fun providesCardsRepository(cardsRepository: CardsRepositoryImpl): CardsRepository {
        return cardsRepository
    }

    @Provides
    fun provideCardsApi(retrofit: Retrofit): CardsApi {
        return retrofit.create(CardsApi::class.java)
    }

}