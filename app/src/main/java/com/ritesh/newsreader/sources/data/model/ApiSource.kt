package com.ritesh.newsreader.sources.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ApiSource(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?
)