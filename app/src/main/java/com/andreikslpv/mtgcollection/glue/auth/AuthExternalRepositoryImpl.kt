package com.andreikslpv.mtgcollection.glue.auth

import com.andreikslpv.domain_auth.repositories.AuthExternalRepository
import com.andreikslpv.domain_cards.repositories.CardsRepository
import com.andreikslpv.domain_settings.repositories.SettingsRepository
import com.andreikslpv.domain_users.UsersRepository
import javax.inject.Inject

class AuthExternalRepositoryImpl @Inject constructor(
    private val usersRepository: UsersRepository,
    private val cardsRepository: CardsRepository,
    private val settingsRepository: SettingsRepository,
) : AuthExternalRepository {

    override suspend fun createUser(uid: String) = usersRepository.createUserInDb(uid)

    override suspend fun deleteUserInDb(uid: String) = usersRepository.deleteUserInDb(uid)

    override suspend fun removeAllFromCollection(uid: String) =
        cardsRepository.removeAllFromCollection(uid)

    override fun getCollection() = usersRepository.getCollection()

    override fun getHistory() = usersRepository.getHistory()

    override suspend fun addToUsersCollection(uid: String, cardId: String) =
        usersRepository.addToCollection(uid, cardId)

    override suspend fun removeFromUsersCollection(uid: String, cardId: String) =
        usersRepository.removeFromCollection(uid, cardId)

    override suspend fun getPrivacyPolicy() = settingsRepository.getPrivacyPolicy()

}