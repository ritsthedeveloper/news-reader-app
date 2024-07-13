package com.ritesh.newsreader.util

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.ritesh.newsreader.common.ui.navigation.Route

object NavigationUtil {
    fun navigateSingleTopTo(
        route: String,
        navController: NavHostController
    ) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }

    fun navigateToCountryScreen(
        countryId: String,
        navController: NavHostController
    ) {
        navController.navigate(Route.TopNews.routeWithoutArgs + "/${countryId}/{language}/{source}/{category}") {
            launchSingleTop = true
        }
    }

    fun navigateToLanguageScreen(
        languageId: String,
        navController: NavHostController
    ) {
        navController.navigate(Route.TopNews.routeWithoutArgs + "/{country}/${languageId}/{source}/{category}") {
            launchSingleTop = true
        }
    }

    fun navigateToSourceScreen(
        sourceId: String,
        navController: NavHostController
    ) {
        navController.navigate(Route.TopNews.routeWithoutArgs + "/{country}/{language}/${sourceId}/{category}") {
            launchSingleTop = true
        }
    }

    fun navigateToCategoryScreen(
        categoryId: String,
        navController: NavHostController
    ){
        navController.navigate(Route.TopNews.routeWithoutArgs + "/{country}/{language}/{source}/${categoryId}") {
            launchSingleTop = true
        }
    }

}