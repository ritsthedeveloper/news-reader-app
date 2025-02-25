package com.ritesh.newsreader.articles.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ritesh.newsreader.R
import com.ritesh.newsreader.articles.data.model.Category
import com.ritesh.newsreader.articles.data.model.Country
import com.ritesh.newsreader.articles.data.model.Language
import com.ritesh.newsreader.articles.data.repository.database.entity.Source
import com.ritesh.newsreader.articles.presentation.viewmodel.CategoryFilterViewModel
import com.ritesh.newsreader.articles.presentation.viewmodel.CountryFilterViewModel
import com.ritesh.newsreader.articles.presentation.viewmodel.LanguageFilterViewModel
import com.ritesh.newsreader.articles.presentation.viewmodel.SourceFilterViewModel
import com.ritesh.newsreader.common.network.NoInternetException
import com.ritesh.newsreader.common.ui.base.ShowError
import com.ritesh.newsreader.common.ui.base.ShowLoading
import com.ritesh.newsreader.common.ui.base.UIState
import com.ritesh.newsreader.articles.presentation.components.CategoryListLayout
import com.ritesh.newsreader.articles.presentation.components.CountryListLayout
import com.ritesh.newsreader.articles.presentation.components.LanguageListLayout
import com.ritesh.newsreader.articles.presentation.components.SourceListLayout

/**
 * Filter Screen displays all the Filtering options like Country, Category, Language and Source to the user,
 * where user can select any filter from these options. Once selected the Filter data will be presented to the user.
 * On landing on Filter Screen, by default country filter is selected and list of countries is presented to user.
 */
@Composable
fun CountryScreen(
    countryFilterViewModel: CountryFilterViewModel = hiltViewModel(),
    countryClicked: (Country) -> Unit
) {
    val countryUiState: UIState<List<Country>> by countryFilterViewModel.countryItem.collectAsStateWithLifecycle()

    when (countryUiState) {
        is UIState.Loading -> {
            ShowLoading()
        }

        is UIState.Failure -> {
            var errorText = stringResource(id = R.string.something_went_wrong)
            if ((countryUiState as UIState.Failure<List<Country>>).throwable is NoInternetException) {
                errorText = stringResource(id = R.string.no_internet_available)
            }
            ShowError(
                text = errorText,
                retryEnabled = true
            ) {
                countryFilterViewModel.getCountries()
            }
        }

        is UIState.Success -> {
            CountryListLayout(countryList = (countryUiState as UIState.Success<List<Country>>).data) {
                countryClicked(it)
            }
        }

        is UIState.Empty -> {

        }
    }

}

@Composable
fun LanguageScreen(
    languageFilterViewModel: LanguageFilterViewModel = hiltViewModel(),
    languageClicked: (Language) -> Unit
) {
    val languageUiState: UIState<List<Language>> by languageFilterViewModel.languageItem.collectAsStateWithLifecycle()

    when (languageUiState) {
        is UIState.Loading -> {
            ShowLoading()
        }

        is UIState.Failure -> {
            var errorText = stringResource(id = R.string.something_went_wrong)
            if ((languageUiState as UIState.Failure<List<Language>>).throwable is NoInternetException) {
                errorText = stringResource(id = R.string.no_internet_available)
            }
            ShowError(
                text = errorText,
                retryEnabled = true
            ) {
                languageFilterViewModel.getLanguage()
            }
        }

        is UIState.Success -> {
            LanguageListLayout(languageList = (languageUiState as UIState.Success<List<Language>>).data) {
                languageClicked(it)
            }
        }

        is UIState.Empty -> {

        }
    }

}

@Composable
fun SourceScreen(
    sourceFilterViewModel: SourceFilterViewModel = hiltViewModel(),
    sourceClicked: (Source) -> Unit
) {
    val sourceUiState: UIState<List<Source>> by sourceFilterViewModel.sourceItem.collectAsStateWithLifecycle()

    when (sourceUiState) {
        is UIState.Loading -> {
            ShowLoading()
        }

        is UIState.Failure -> {
            var errorText = stringResource(id = R.string.something_went_wrong)
            if ((sourceUiState as UIState.Failure<List<Source>>).throwable is NoInternetException) {
                errorText = stringResource(id = R.string.no_internet_available)
            }
            ShowError(
                text = errorText,
                retryEnabled = true
            ) {
                sourceFilterViewModel.getSources()
            }
        }

        is UIState.Success -> {
            SourceListLayout(sourceList = (sourceUiState as UIState.Success<List<Source>>).data) {
                sourceClicked(it)
            }
        }

        is UIState.Empty -> {

        }
    }

}

@Composable
fun CategoryScreen(
    categoryFilterViewModel: CategoryFilterViewModel = hiltViewModel(),
    categoryClicked: (Category) -> Unit
) {
    val categoryUiState: UIState<List<Category>> by categoryFilterViewModel.categoryItem.collectAsStateWithLifecycle()

    when (categoryUiState) {
        is UIState.Loading -> {
            ShowLoading()
        }

        is UIState.Failure -> {
            var errorText = stringResource(id = R.string.something_went_wrong)
            if ((categoryUiState as UIState.Failure<List<Category>>).throwable is NoInternetException) {
                errorText = stringResource(id = R.string.no_internet_available)
            }
            ShowError(
                text = errorText,
                retryEnabled = true
            ) {
                categoryFilterViewModel.getCategories()
            }
        }

        is UIState.Success -> {
            CategoryListLayout(categoryList = (categoryUiState as UIState.Success<List<Category>>).data) {
                categoryClicked(it)
            }
        }

        is UIState.Empty -> {

        }
    }

}
