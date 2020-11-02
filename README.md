# G-Fresh
Fetch Fresh, Working and Trending GIFFs from Giphy via RESTful APIs



Features

● All GIFs animating. ✔
● The user must see a list of trending GIFs when launching the app. ✔
● The user must be able to type any search term into an search area on the first screen 
    and have the view refresh the list with the results of their search. ✔
● The user must see a grid of his/her favourite GIFs on the second screen. ✔
● The user must be able to add and remove GIFs to and from their favourites. ✔

Specifications
● There should be one activity with two Fragments. ✔
● The Fragments should be swipe-able using a TabLayout and ViewPager. ✔

FirstFragment
● Contains a search bar (EditText, SearchView, etc.) at the top. ✔
● Contains a RecyclerView that displays searched GIFs. ✔
● The RecyclerView should load trending GIFs if there is no search term provided. ✔
● Show a loading indicator when loading GIFs or refreshing the list. ✔

SecondFragment
● Contains a RecyclerView that displays a grid (at least two columns) of the user’s favourite GIFs stored locally on the deviceList/GridItem. ✔
● Should contain a layout including a running GIF. ✔
● Should contain a button which allows the user to add or remove a GIF from their favourites list. ✔
● This button must also indicate if the GIF is a favourite or not. ✔

Bonus
● Use a software architecture pattern (ie. MVP, MVVM, VIPER, etc.). ✔
● Use RxJava. ✔
● Use Kotlin instead of Java. ✔
● Add pagination (infinite scrolling) to the GIF lists. ✔
● Add animations. ✔
● Add unit tests for your code. ✔

