package com.andreikslpv.data.cards

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andreikslpv.common.Response
import com.andreikslpv.data.cards.datasource.CardsApiPagingSource
import com.andreikslpv.data.cards.datasource.CardsCacheDataSource
import com.andreikslpv.data.cards.datasource.CardsFirebasePagingSource
import com.andreikslpv.data.cards.entities.CardDataModel
import com.andreikslpv.data.cards.services.CardsInSetService
import com.andreikslpv.data.constants.ApiConstants
import com.andreikslpv.data.constants.FirestoreConstants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Retrofit
import javax.inject.Inject

class CardsDataRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit,
    private val database: FirebaseFirestore,
    private val cacheDataSource: CardsCacheDataSource,
) : CardsDataRepository {
    private var isApiAvailable = true

    override fun getCardsInSet(codeOfSet: String): Flow<PagingData<CardDataModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = ApiConstants.DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = ApiConstants.DEFAULT_PAGE_SIZE
            ),
            pagingSourceFactory = {
                if (isApiAvailable) {
                    CardsApiPagingSource(
                        retrofit.create(CardsInSetService::class.java),
                        codeOfSet,
                        object : CardsApiCallback {
                            override fun onSuccess(items: List<CardDataModel>) {
                                if (isApiAvailable) CoroutineScope(Dispatchers.IO).launch {
                                    cacheDataSource.saveCardsToDb(items)
                                }
                            }

                            override fun onFailure() {}
                        })
                } else {
                    // загружаем данные из кэша и меняем статус доступности апи на true,
                    // чтобы в следующий раз снова сначала была попытка получить данные из апи
                    isApiAvailable = true
                    cacheDataSource.getCardsInSet(codeOfSet)
                }
            }
        ).flow
    }

    override fun getCardsInCollection(uid: String): Flow<PagingData<CardDataModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = ApiConstants.DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = ApiConstants.DEFAULT_PAGE_SIZE
            ),
            pagingSourceFactory = {
                val collection = database.collection(FirestoreConstants.PATH_CARDS)
                    .document(uid)
                    .collection(FirestoreConstants.PATH_COLLECTION)
                val query = collection
                    .orderBy("name")
                    .limit(ApiConstants.DEFAULT_PAGE_SIZE.toLong())
                CardsFirebasePagingSource(query)
            }).flow
    }

    override fun changeApiAvailability(newStatus: Boolean) {
        isApiAvailable = newStatus
    }

    override fun addToCollection(uid: String, card: CardDataModel) {
        CoroutineScope(Dispatchers.IO).launch {
            val collection = database.collection(FirestoreConstants.PATH_CARDS)
                .document(uid)
                .collection(FirestoreConstants.PATH_COLLECTION)
            val document = collection.document(card.id)
            document.set(card).await()
        }
    }

    override fun removeFromCollection(uid: String, card: CardDataModel) {
        CoroutineScope(Dispatchers.IO).launch {
            val collection = database.collection(FirestoreConstants.PATH_CARDS)
                .document(uid)
                .collection(FirestoreConstants.PATH_COLLECTION)
            val document = collection.document(card.id)
            document.delete().await()
        }
    }

    override fun getCardFromCollection(uid: String, cardId: String) = callbackFlow {
        val cardStateListener = database.collection(FirestoreConstants.PATH_CARDS)
            .document(uid)
            .collection(FirestoreConstants.PATH_COLLECTION)
            .whereEqualTo("id", cardId)
            .addSnapshotListener { value, error ->
                if (error == null && value != null) {
                    val tempList = value.documents.mapNotNull {
                        it.toObject(CardDataModel::class.java)
                    }
                    if (tempList.isNotEmpty()) trySend(tempList[0])
                    else trySend(null)
                } else trySend(null)
            }
        awaitClose {
            cardStateListener.remove()
        }
    }



//        try {
//            emit(Response.Loading)
//            val collection = database.collection(FirestoreConstants.PATH_CARDS)
//                .document(uid)
//                .collection(FirestoreConstants.PATH_COLLECTION)
//            val result = collection
//                .whereEqualTo("id", cardId)
//                .get()
//                .addOnCompleteListener {
//
//                }
//
//            val tempList = result.documents.mapNotNull {
//                it.toObject(CardDataModel::class.java)
//            }
//
//            if (tempList.isNotEmpty()) emit(Response.Success(tempList[0]))
//            else emit(Response.Success(null))
//        } catch (e: Exception) {
//            emit(Response.Failure(e.message ?: ApiConstants.ERROR_MESSAGE))
//        }

    override fun removeAllFromCollection(uid: String) = flow {
        try {
            emit(Response.Loading)
            database.collection(FirestoreConstants.PATH_CARDS).document(uid).set("deleted").await()
                .also { emit(Response.Success(true)) }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: ApiConstants.ERROR_MESSAGE))
        }
    }
}