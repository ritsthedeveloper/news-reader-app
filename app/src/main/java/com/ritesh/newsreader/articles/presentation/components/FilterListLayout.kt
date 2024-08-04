package com.ritesh.newsreader.articles.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.ritesh.newsreader.articles.data.model.Category
import com.ritesh.newsreader.articles.data.model.Country
import com.ritesh.newsreader.articles.data.model.Language
import com.ritesh.newsreader.articles.data.repository.database.entity.Source

@Composable
fun CountryListLayout(
    countryList: List<Country>,
    countryClicked: (Country) -> Unit
) {
    LazyColumn {
        items(countryList) {
            CountryItem(it) { country ->
                countryClicked(country)
            }
        }
    }
}

@Composable
fun LanguageListLayout(
    languageList: List<Language>,
    languageClicked: (Language) -> Unit
) {
    LazyColumn {
        items(languageList) {
            LanguageItem(it) { language ->
                languageClicked(language)
            }
        }
    }
}

@Composable
fun SourceListLayout(
    sourceList: List<Source>,
    sourceClicked: (Source) -> Unit
) {
    LazyColumn {
        items(sourceList) {
            SourceItem(it) { source ->
                sourceClicked(source)
            }
        }
    }
}

@Composable
fun CategoryListLayout(
    categoryList: List<Category>,
    categoryClicked: (Category) -> Unit
) {
    LazyColumn {
        items(categoryList) {
            CategoryItem(it) { category ->
                categoryClicked(category)
            }
        }
    }
}

