package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class GithubResponse(

    @field:SerializedName("items")
    val items: List<UserResponse>,
)