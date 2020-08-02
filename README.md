## Movie Catalog

Available on Google Play Store
https://play.google.com/store/apps/details?id=com.movies.allmovies&hl=en
### What you will find in this project
- **Dagger2** : injecting ViewModels, Coroutines Dispatchers (for easier testing) and all instances needed for network requests.
- **MVVM** - ViewModel, LiveData, Databinding and Repository (for the HomeScreen)
- **Room** - Used for persistence of the movies in "My List" feature
- **Firebase Cloud Message - FCM**
- **Retrofit** CallAdapter Factory built for a concise way of making web requests in the ViewModel

ps.: PersonViewModel, PersonDetailsFragment and MyListFragment are still not refactored. Another point is that, **due to a lack of time**, the request error cases are not being handled by the views in **this version**, but want to implement it as soon as possible.

### Home Screen
![tape-one](https://github.com/igorlacourt/All-Movies/blob/feature/dagger/tape1.gif)

### Movie's Details Screen
![tape-two](https://github.com/igorlacourt/All-Movies/blob/feature/dagger/tape2.gif)

### Actor's Details Screen
![tape-three](https://github.com/igorlacourt/All-Movies/blob/feature/dagger/tape3.gif)
