package com.ritesh.newsreader.sources.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Main Article-Sources response wrapper.
 */
@Keep
data class SourcesResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("sources")
    val sources: List<ApiSource>
)