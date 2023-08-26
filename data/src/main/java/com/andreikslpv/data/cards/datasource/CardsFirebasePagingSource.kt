package com.andreikslpv.data.cards.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andreikslpv.data.cards.entities.CardDataModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class CardsFirebasePagingSource(private val query: Query) :
    PagingSource<QuerySnapshot, CardDataModel>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, CardDataModel>) = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, CardDataModel> {
        return try {
            println("AAA load key = ${params.key}")
            val currentPage = params.key ?: query.get().await()
            println("AAA load currentPage = ${currentPage.size()}")
            val lastVisibleCard =
                if (currentPage.size() > 0) currentPage.documents[currentPage.size() - 1] else null
            println("AAA load lastVisibleCard = $lastVisibleCard")
            val nextPage =
                if (lastVisibleCard != null) query.startAfter(lastVisibleCard).get()
                    .await() else null
            LoadResult.Page(
                data = currentPage.toObjects(CardDataModel::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            println("AAA error: ${e.message}")
            return LoadResult.Error(e)
        }
    }
}