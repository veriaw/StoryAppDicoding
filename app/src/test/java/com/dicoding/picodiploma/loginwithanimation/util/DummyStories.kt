package com.dicoding.picodiploma.loginwithanimation.util

import androidx.annotation.VisibleForTesting
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem

object DummyStories {
    @VisibleForTesting
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                id = i.toString(),
                createdAt = "createdAt + $i",
                description = "description $i",
                lat = 0.0,
                lon = 0.0,
                name = "name $i",
                photoUrl = "photoUrl $i"
            )
            items.add(story)
        }
        return items
    }
}