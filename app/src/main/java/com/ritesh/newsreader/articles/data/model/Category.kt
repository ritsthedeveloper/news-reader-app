package com.ritesh.newsreader.articles.data.model

import androidx.annotation.Keep

@Keep
data class Category(val name: String, val code: String) : NewsFilter
