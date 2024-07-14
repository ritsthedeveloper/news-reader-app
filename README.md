# About NewsReader

- NewsReader is an application where user can read the top news/articles from all over the world. 
- User can filter the news based on the country, language, source and category of the news.
- User can bookmark/save the articles of interest for reading at some later time. Once done user can delete the 
  saved article.
- Support for Light as well as Dark mode makes pleasant user experience.

## Features 
- Show list of top news articles
- Filter news by country, language, news-category and news-source
- Bookmark/Save news articles for future reference and delete once done.
- Search for specific news articles.
- News articles details 
## Note : News API - public API service used
- Visit newsapi.org and sign up for an API key,

## Tech Stack

- **Architecture** - Clean + MVVM architecture with Repository pattern
- **Programming Language** - **Kotlin** and **Kotlin DSL**
- **Data Layer**
  - **Room DB** (Model)
    - for local storage of news articles
    - Also provides caching of articles in order to reduce the Network Calls
- **Presentation Layer**
  - **Jetpack Compose** for modern UI (View)
  - **ViewModel** of Android (VM)
  - **Pull to refresh** for refreshing top-news content
  - **Swipe to delete** for removing the saved news articles 
  - **Navigation** Navigation controller
  - **Pagination** to efficiently load and display news articles
  - **StateFlow** for streamlined state management
- **Networking**
  - **Retrofit** for seamless networking
- **Async programming** and Reactive programming
  - **Coroutines** and **Flow** for asynchronous programming
- **Dependency Injection**
  - **Dagger Hilt** for dependency injection.
- **Coil** for image loading
- **Timber** for Logging purpose
- **Gradle version Catalog** for Dependency Management


