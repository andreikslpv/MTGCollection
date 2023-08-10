package com.andreikslpv.cards.domain.usecase

import androidx.paging.PagingData
import com.andreikslpv.cards.domain.entities.CardFeatureModel
import com.andreikslpv.cards.domain.repositories.CardsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCardsInSetUseCase @Inject constructor(
    private val cardsRepository: CardsRepository,
) {

    fun execute(codeOfSet: String?): Flow<PagingData<CardFeatureModel>> {
        println("AAA GetCardsInSetUseCase")
        return if (codeOfSet != null) {
             cardsRepository.getCardsInSet(codeOfSet)
        } else {
            cardsRepository.getCardsInSet("")
        }

    }
}