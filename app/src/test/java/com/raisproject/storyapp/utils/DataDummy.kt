package com.raisproject.storyapp.utils

import com.raisproject.storyapp.data.response.ErrorResponse
import com.raisproject.storyapp.data.response.ListStoryItem
import com.raisproject.storyapp.data.response.LoginResponse
import com.raisproject.storyapp.data.response.LoginResult

object DataDummy {

    fun generateDummyLoginResponse(): LoginResponse {
        return LoginResponse(
            false,
            "message",
            LoginResult(
                "id",
                "name",
                "token"
            )
        )
    }

    fun generateDummyRegisterResponse(): ErrorResponse {
        return ErrorResponse(
            false,
            "Success"
        )
    }

    fun generateDummyStoryResponse() : List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "id + $i",
                "name + $i",
                "description + $i",
                i.toString(),
                "Created + $i",
                i.toDouble(),
                i.toDouble(),
            )
            items.add(story)
        }
        return items
    }
}