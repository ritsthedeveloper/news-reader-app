package com.ritesh.newsreader.articles.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ritesh.newsreader.R
import com.ritesh.newsreader.articles.data.model.Category
import com.ritesh.newsreader.articles.data.model.Country
import com.ritesh.newsreader.articles.data.model.Language
import com.ritesh.newsreader.articles.data.repository.database.entity.Source
import com.ritesh.newsreader.common.ui.components.FilterItemCard

@Composable
fun CountryItem(
    country: Country,
    onItemClick: (Country) -> Unit
) {
    FilterItemCard(strTxt = country.name, filterItem = country) {
        onItemClick(country)
    }
}

@Composable
fun LanguageItem(
    language: Language,
    onItemClick: (Language) -> Unit
) {
    FilterItemCard(strTxt = language.name, filterItem = language) {
        onItemClick(language)
    }
}

@Composable
fun SourceItem(
    source: Source,
    onItemClick: (Source) -> Unit
) {
    FilterItemCard(strTxt = source.name ?: stringResource(R.string.unknown), filterItem = source) {
        onItemClick(source)
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onItemClick: (Category) -> Unit
) {
    FilterItemCard(strTxt = category.name, filterItem = category) {
        onItemClick(category)
    }
}
