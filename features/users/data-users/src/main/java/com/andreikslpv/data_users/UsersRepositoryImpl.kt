package com.andreikslpv.data_users

import com.andreikslpv.common.Core
import com.andreikslpv.common.Response
import com.andreikslpv.data_users.entities.UserModel
import com.andreikslpv.domain.entities.CardEntity
import com.andreikslpv.domain_users.UsersRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
) : UsersRepository {

    private val collection = MutableStateFlow(emptyList<String>())
    private val history = MutableStateFlow(emptyList<CardEntity>())
    private lateinit var userListener: ListenerRegistration

    private companion object {
        private const val WRITE_TIMEOUT_MS = 30_000L
        private const val WRITE_ATTEMPTS = 3
    }

    override suspend fun createUserInDb(uid: String) = flow {
        emit(Response.Loading)
        val user = UserModel(uid = uid)
        database.collection(FirestoreConstants.PATH_USERS).document(user.uid).set(user).await()
            .also { emit(Response.Success(true)) }
    }.flowOn(Dispatchers.IO)

    override fun startObserveUserInDb(uid: String) {
        userListener = database.collection(FirestoreConstants.PATH_USERS).document(uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Core.errorHandler.handleError(error)
                    return@addSnapshotListener
                }
                if (value != null) {
                    val user = value.toObject(UserModel::class.java) ?: UserModel()
                    collection.value = user.collection
                    history.value = user.history
                }
            }
    }

    override fun stopObserveUserInDb() {
        if (::userListener.isInitialized)
            userListener.remove()
    }

    override suspend fun deleteUserInDb(uid: String): Unit =
        withContext(Dispatchers.IO) {
            database.collection(FirestoreConstants.PATH_USERS).document(uid).delete().await()
        }

    override fun getCollection() = collection

    override suspend fun addToCollection(uid: String, cardId: String): Unit =
        withContext(Dispatchers.IO) {
            if (!collection.value.contains(cardId)) {
                collection.value = collection.value + cardId
            }
            val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
            for (attempt in 1..WRITE_ATTEMPTS) {
                try {
                    val done = withTimeoutOrNull(WRITE_TIMEOUT_MS) {
                        user.update(FirestoreConstants.FIELD_COLLECTION, FieldValue.arrayUnion(cardId)).await()
                        true
                    }
                    if (done == true) return@withContext
                } catch (e: Exception) {
                    if (attempt == WRITE_ATTEMPTS) {
                        collection.value = collection.value.filter { it != cardId }
                    }
                }
            }
        }

    override suspend fun removeFromCollection(uid: String, cardId: String): Unit =
        withContext(Dispatchers.IO) {
            if (collection.value.contains(cardId)) {
                collection.value = collection.value.filter { it != cardId }
            }
            val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
            for (attempt in 1..WRITE_ATTEMPTS) {
                try {
                    val done = withTimeoutOrNull(WRITE_TIMEOUT_MS) {
                        user.update(FirestoreConstants.FIELD_COLLECTION, FieldValue.arrayRemove(cardId)).await()
                        true
                    }
                    if (done == true) return@withContext
                } catch (e: Exception) {
                    if (attempt == WRITE_ATTEMPTS) {
                        if (!collection.value.contains(cardId)) {
                            collection.value = collection.value + cardId
                        }
                    }
                }
            }
        }

    override suspend fun removeAllFromCollection(uid: String): Unit =
        withContext(Dispatchers.IO) {
            collection.value = emptyList()
            val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
            for (attempt in 1..WRITE_ATTEMPTS) {
                try {
                    val done = withTimeoutOrNull(WRITE_TIMEOUT_MS) {
                        user.update(FirestoreConstants.FIELD_COLLECTION, emptyList<String>()).await()
                        true
                    }
                    if (done == true) return@withContext
                } catch (e: Exception) {
                    // keep the local state, a later observer will re-sync
                }
            }
        }

    override fun getHistory() = history

    override suspend fun setHistory(uid: String, newHistory: List<CardEntity>): Unit =
        withContext(Dispatchers.IO) {
            val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
            user.update(FirestoreConstants.FIELD_HISTORY, newHistory).await()
        }

}