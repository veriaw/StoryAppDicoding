package com.dicoding.picodiploma.loginwithanimation.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.picodiploma.loginwithanimation.data.remote.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem

class StoryPagingSource(private val authToken: String): PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        val page = params.key ?: 1 // Halaman pertama
        return try {
            val response = ApiConfig.getApiService().getAllStories(authToken, page, 20)
            Log.d("PlacePagingSource", "Response for page $page: $response")
            val data = response.listStory

            LoadResult.Page(
                data = data?.filterNotNull() ?: emptyList(), // Pastikan data tidak null
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isNullOrEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.d("LOAD RESULT","$e")
            LoadResult.Error(e)
        }
    }

}