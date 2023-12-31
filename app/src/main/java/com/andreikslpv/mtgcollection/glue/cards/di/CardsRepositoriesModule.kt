package com.andreikslpv.mtgcollection.glue.cards.di

import com.andreikslpv.domain_cards.repositories.CardsExternalRepository
import com.andreikslpv.mtgcollection.glue.cards.CardsExternalRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CardsRepositoriesModule {

    @Binds
    @Singleton
    fun bindCardsExternalRepository(cardsExternalRepository: CardsExternalRepositoryImpl): CardsExternalRepository
}