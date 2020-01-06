<h1> Android Mobile App </h1>
 
 <p>It's an Android application, which allows users to search for cities to see weather summary, look at detailed information about them, pin those cities to favorites and post on Twitter about the weather.
Backend service is on Node.js and the app uses API call. 

<p>You will find concepts and technologies usage in the app:
  <ul>
    <li>Java, JSON, Android Lifecycle and Android Studio</li>
    <li> Google’s Material design</li>
    <li> Google’s Material design</li>
    <li> Google Maps APIs and Android SDK </li>
    <li> Third party libraries like Picasso, Glide and Volley </li>
  </ul>
</p>


<h3>References</h3>
<ul>
 <li>
 In order to get the current location, you can use either ip-api or location services.
For your location fetching code to work, you must request the permission from the user. You
can read more about requesting permissions here:
<a href="https://developer.android.com/training/permissions/requesting.html" target="_blank">https://developer.android.com/training/permissions/requesting.html</a></li>

 <li>You will need this for various features like getting the current location and using Google Maps
in your app. You can learn about setting it up here:
<a href="https://developers.google.com/android/guides/setup" target="_blank"></a>
</li>

 <li>
 Volley can be helpful with asynchronously http request to load data. You can also use Volley network ImageView to load photos in Google tab. You can learn more about them here: <a href="https://developer.android.com/training/volley/index.html" target="_blank">https://developer.android.com/training/volley/index.html</a>
</li>

 <li>
 Picasso is a powerful image downloading and caching library for Android.<a href="http://square.github.io/picasso/" target="_blank">http://square.github.io/picasso/</a> 
If you decide to use RecycleView to display the photos with Picasso Please use version 2.5.2
since latest version does not support RecycleView well.<a href="https://github.com/codepath/android_guides/wiki/Displaying-Images-with-the-Picasso-Library" target="_blank>https://github.com/codepath/android_guides/wiki/Displaying-Images-with-the-Picasso-Library</a>
</li>

<li>
 Glide is also powerful image downloading and caching library for Android. It is similar to
Picasso. You can also use Glide to load photos in Google tab.<a href="https://bumptech.github.io/glide/" target="_blank">https://bumptech.github.io/glide/</a>
</li>

<li>
In order to create graphs we will use the MPAndroidChart library. Full code and documentation can be
found here <a href="https://github.com/PhilJay/MPAndroidChart" target="_blank">https://github.com/PhilJay/MPAndroidChart</a> 
For our purposes following these links should suffice:
Creating a LineChart: <a href="https://weeklycoding.com/mpandroidchart-documentation/setting-data/" target="_blank">https://weeklycoding.com/mpandroidchart-documentation/setting-data/</a>  
Styling the Legend: <a href="https://github.com/PhilJay/MPAndroidChart/wiki/Legend" target="_blank">https://github.com/PhilJay/MPAndroidChart/wiki/Legend</a>  
Styling the label: <a href="https://stackoverflow.com/questions/28632489/mpandroidchart-how-to-set-label-color" target="_blank">https://stackoverflow.com/questions/28632489/mpandroidchart-how-to-set-label-color</a>  
</li>

<li>
<a href="https://stackoverflow.com/questions/20586619/android-viewpager-with-bottom-dots" target="_blank">https://stackoverflow.com/questions/20586619/android-viewpager-with-bottom-dots</a>
<a href="https://stackoverflow.com/questions/38459309/how-do-you-create-an-android-view-pager-witha-dots-indicator" target="_blank">
https://stackoverflow.com/questions/38459309/how-do-you-create-an-android-view-pager-witha-dots-indicator</a>
</li>

<li>
<a href="https://developer.android.com/training/appbar/setting-up" target="_blank">https://developer.android.com/training/appbar/setting-up</a>
<a href="https://stackoverflow.com/questions/38195522/what-is-oncreateoptionsmenumenu-menu" target="_blank">https://stackoverflow.com/questions/38195522/what-is-oncreateoptionsmenumenu-menu</a>
</li>

<li>
<h5>SearchBar and AutoCompleteTextView</h5>
To implement the search functionality, these pages will help:
<a href="https://www.youtube.com/watch?v=9OWmnYPX1uc" target="_blank">https://www.youtube.com/watch?v=9OWmnYPX1uc</a>
<a href="https://developer.android.com/guide/topics/search/search-dialog" target="_blank">https://developer.android.com/guide/topics/search/search-dialog</a>

Working with the AutoCompleteTextView to show the suggestions might be a little challenging.
This tutorial goes over how it is done so that you get an idea of how to go about it.
<a href="https://www.truiton.com/2018/06/android-autocompletetextview-suggestions-from-webservicecall/" target="_blank">https://www.truiton.com/2018/06/android-autocompletetextview-suggestions-from-webservicecall/</a>

In order to link your Search Bar with autocomplete suggestions, these links might help:
<a href="https://www.dev2qa.com/android-actionbar-searchview-autocompleteexample/https://stackoverflow.com/questions/34603157/how-to-get-a-text-from-searchview" target="_blank">https://www.dev2qa.com/android-actionbar-searchview-autocompleteexample/https://stackoverflow.com/questions/34603157/how-to-get-a-text-from-searchview</a>

</li>

<li>
There are many ways to implement a splash screen. This blog highlights almost all of them
with examples:
<a href="https://android.jlelse.eu/the-complete-android-splash-screen-guide-c7db82bce565" target="_blank">https://android.jlelse.eu/the-complete-android-splash-screen-guide-c7db82bce565</a>
</li>

</ul>
