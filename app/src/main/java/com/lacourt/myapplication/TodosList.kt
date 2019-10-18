package com.lacourt.myapplication

//TODO 1. Implement adding and removing from "my list" with button icon change
//TODO 2. Check if it should be lateinit modifier for variables like recyclerview in MyListFragment and other classes
//TODO 2. Change item layout's width maybe it's better to set a fixed height, wrap_content width, and use matrix instead of centerCrop scaleType
//TODO 3. Adjust icons and bottom navigation
//TODO 4. Set style hierarchy correctly
//TODO 5. Remove hard coded string and colors
//TODO 6. Add Dagger
//TODO 7. Implement Navigation
//TODO 8. Implement backwards navigation for home an mylist fragments (when click back the app closes instead of back to the one was being displayed before)

//TODO adding and removing from "my list"
//1. in DetailsActivity's onCreate
//2. Check if there is a movie with same id in 'my_list' table
//3. if a movie is found, icon is a check mark
//4. if not, the icon is a plus sign
//5. When the plus button is clicked, move is added (asynchronously) check for a callback to change the icon
//a path for building a callback may be iun this link: https://stackoverflow.com/questions/47921312/executing-delete-with-room-rxjava