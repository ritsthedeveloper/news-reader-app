package com.ritesh.newsreader.common.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.ritesh.newsreader.R
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.articles.presentation.screens.ArticleScreen
import com.ritesh.newsreader.articles.presentation.screens.CategoryScreen
import com.ritesh.newsreader.articles.presentation.screens.CountryScreen
import com.ritesh.newsreader.articles.presentation.screens.LanguageScreen
import com.ritesh.newsreader.articles.presentation.screens.NewsScreen
import com.ritesh.newsreader.articles.presentation.screens.NewsScreenPaging
import com.ritesh.newsreader.articles.presentation.screens.SavedScreen
import com.ritesh.newsreader.articles.presentation.screens.SearchScreen
import com.ritesh.newsreader.articles.presentation.screens.SourceScreen
import com.ritesh.newsreader.util.NavigationUtil.navigateSingleTopTo
import com.ritesh.newsreader.util.NavigationUtil.navigateToCategoryScreen
import com.ritesh.newsreader.util.NavigationUtil.navigateToCountryScreen
import com.ritesh.newsreader.util.NavigationUtil.navigateToLanguageScreen
import com.ritesh.newsreader.util.NavigationUtil.navigateToSourceScreen
import com.ritesh.newsreader.util.ValidationUtil.checkIfValidArgNews
import java.net.URLEncoder

/**
 * When implementing navigation in an app, implement a navigation host, graph, and controller.
 */
@Composable
fun NewsReaderContainer() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen =
        bottomBarScreens.find { it.route == currentDestination?.route } ?: Route.TopNews

    Scaffold(
        topBar = {
            NewsTopBar {
                if (navController.currentBackStackEntry?.destination?.route == Route.NewsArticle.route
                    || navController.currentBackStackEntry?.destination?.route == Route.FilterNews.route
                    || navController.currentBackStackEntry?.destination?.route == Route.TopNews.route
                ) {
                    navController.popBackStack()
                } else {
                    navigateSingleTopTo(Route.TopNews.route, navController)
                }
            }
        },
        bottomBar = {
            NewsBottomNavigation(
                currentScreen = currentScreen
            ) {
                navigateSingleTopTo(it.route, navController)
            }
        }
    ) {
        NewsNavHost(
            modifier = Modifier.padding(it),
            navController = navController
        )
    }
}

/**
 * Display the AppName with Back button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTopBar(onBackClicked: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = { onBackClicked() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun NewsNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Route.TopNews.route,
        modifier = modifier
    ) {
        // Top-news or Top-headlines Screen
        composable(
            // Pass on the route
            route = Route.TopNews.route,
            // pass on the values for country, language, source or category as arguments
            arguments = listOf(navArgument("country") { type = NavType.StringType },
                navArgument("language") { type = NavType.StringType },
                navArgument("source") { type = NavType.StringType },
                navArgument("category") { type = NavType.StringType })
        ) {
            // extract the arguments from the NavBackStackEntry that is available in the lambda
            // of the composable() function.
            val countryCode = it.arguments?.getString("country")
            val languageCode = it.arguments?.getString("language")
            val sourceCode = it.arguments?.getString("source")
            val categoryCode = it.arguments?.getString("category")

            // Validate all the arguments before navigation
            if (checkIfValidArgNews(countryCode) || checkIfValidArgNews(languageCode)
                || checkIfValidArgNews(sourceCode) || checkIfValidArgNews(categoryCode)
            ) {
                NewsScreen { article ->
                    navigateToArticleScreen(article, navController)
                }
            } else {
                NewsScreenPaging { article ->
                    navigateToArticleScreen(article, navController)
                }
            }
        }
        // Filter Screen
        composable(route = Route.FilterNews.route) {
            FilterNavHost(parentNavController = navController)
        }
        // Saved/Bookmarked news-article Screen
        composable(route = Route.SavedNews.route) {
            SavedScreen {
                navigateToArticleScreen(it, navController)
            }
        }
        // Search news-article Screen
        composable(route = Route.SearchNews.route) {
            SearchScreen(
                backPressed = {
                    navigateSingleTopTo(Route.TopNews.route, navController)
                }
            ) {
                navigateToArticleScreen(it, navController)
            }
        }
        // Article Detail Screen
        composable(
            route = Route.NewsArticle.route,
            arguments = listOf(navArgument("article") { type = NavType.StringType })
        ) {
            val articleJson = it.arguments?.getString("article")
            val gson = Gson()
            val article = gson.fromJson(articleJson, Article::class.java)
            ArticleScreen(
                article = article
            )
        }
    }
}

@Composable
fun NewsBottomNavigation(
    currentScreen: Route,
    onIconSelected: (Route) -> Unit
) {
    /**
     * Add the functional options like News, Filters, Saved/Bookmarked articles and Search
     * to bottom navigation for easy accessibility.
     */
    NavigationBar {
        bottomBarScreens.forEach { screen ->
            NavigationBarItem(
                selected = screen == currentScreen,
                label = {
                    Text(text = stringResource(id = screen.resourceId))
                },
                icon = {
                    Icon(painter = painterResource(id = screen.icon), null)
                },
                onClick = {
                    onIconSelected.invoke(screen)
                }
            )
        }
    }
}


private fun navigateToArticleScreen(
    it: Article,
    navController: NavHostController
) {
    val articleJsonString = Gson().toJson(it)
    val encodedArticle = URLEncoder.encode(articleJsonString, Charsets.UTF_8.name())
    navController.navigate(Route.NewsArticle.routeWithoutArgs + "/${encodedArticle}") {
        launchSingleTop = true
    }
}

@Composable
fun FilterNavHost(
    parentNavController: NavHostController
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    // By default country filter is used to fetch the top headlines.
    val currentScreen =
        filterScreens.find { it.route == currentDestination?.route } ?: FilterRoute.Country

    Scaffold(
        topBar = {
            FilterNavigation(
                currentScreen = currentScreen
            ) {
                navigateSingleTopTo(it.route, navController)
            }
        }
    ) {
        FilterNavHost(
            navController = navController,
            parentNavController = parentNavController,
            modifier = Modifier.padding(it)
        )
    }

}

@Composable
fun FilterNavHost(
    navController: NavHostController,
    parentNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    val mContext = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = FilterRoute.Country.route,
        modifier = modifier
    ) {
        // Country Filter - Country list Screen
        composable(route = FilterRoute.Country.route) {
            CountryScreen {
                navigateToCountryScreen(it.code, parentNavController)
            }
        }
        // Language Filter - Language List screen
        composable(route = FilterRoute.Language.route) {
            LanguageScreen {
                navigateToLanguageScreen(it.code, parentNavController)
            }
        }
        // Category Filter - Category List Screen
        composable(route = FilterRoute.Category.route) {
            CategoryScreen {
                navigateToCategoryScreen(it.code, parentNavController)
            }
        }
        // News Source Filter - News Source screen
        composable(route = FilterRoute.Source.route) {
            SourceScreen {
                if (it.id != null) {
                    navigateToSourceScreen(it.id, parentNavController)
                } else {
                    Toast.makeText(
                        mContext,
                        mContext.resources.getString(R.string.news_not_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

@Composable
fun FilterNavigation(
    currentScreen: FilterRoute,
    onIconSelected: (FilterRoute) -> Unit
) {
    /**
     * Add the filtering options like Country, Language, Category and News Source
     * for easy accessibility.
     */
    NavigationBar {
        filterScreens.forEach { screen ->
            NavigationBarItem(
                selected = screen == currentScreen,
                label = {
                    Text(text = stringResource(id = screen.resourceId))
                },
                icon = {
                    Icon(painter = painterResource(id = screen.icon), null)
                },
                onClick = {
                    onIconSelected.invoke(screen)
                }
            )
        }
    }
}

