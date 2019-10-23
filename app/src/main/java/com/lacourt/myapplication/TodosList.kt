package com.lacourt.myapplication
//TODO 1. Set epoxy pagedList for My List
//TODO 1. Check if DbMovieDTO is named correctly
//TODO 1. Load backdrop when there's no poster path in Details object to be presented in DetailsFragment
//TODO 2. Transform DetailsActivity in DetailsFragment
//TODO 3. Check if it should be lateinit modifier for variables like recyclerview in MyListFragment and other classes
//TODO 4. Change item layout's width maybe it's better to set a fixed height, wrap_content width, and use matrix instead of centerCrop scaleType
//TODO 5. Adjust icons and bottom navigation
//TODO 6. Set style hierarchy correctly
//TODO 7. Remove hard coded string and colors
//TODO 8. Add Dagger
//TODO 9. Implement Navigation
//TODO 10. Implement backwards navigation for home an mylist fragments (when click back the app closes instead of back to the one was being displayed before)

//TODO adding and removing from "my list"
//1. in DetailsActivity's onCreate
//2. Check if there is a movie with same id in 'my_list' table
//3. if a movie is found, icon is a check mark
//4. if not, the icon is a plus sign
//5. When the plus button is clicked, move is added (asynchronously) check for a callback to change the icon
//a path for building a callback may be iun this link: https://stackoverflow.com/questions/47921312/executing-delete-with-room-rxjava