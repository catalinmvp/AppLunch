#The AppLauncher was developed in Java in order to display all the installed apps from the mobile system. 

Android version : 8.1


In order to obtain the List of apps, a ListView was used to be able to display the apps which can be found in activity_main.xml. Each app will be considered as a singular item, each one having a defined layout (item_layout.xml). 
Each Item will display the title(name) of the application and the Icon image. 
In order for the data to be displayed in the ListView, an Adapter will be used which represents a bridge between the UI component and the data.
As it was stated, making use of the Adapter, in the AppAdapter the connection between data and the UI will be made, so that it the list of the applications will be displayed.


For the Search and filter function, the SearchView was used along with the interface Filterable. This interface defines a filterable behavior. A filterable class can have its data constrained by a filter. 
A filter constrains data with a filtering pattern.
Filterable classes are usually Adapter implementations.
In our case, we made use of the public Filter getFilter() which returns a filter to be used to constrain data. While creating the list of applications, all the applications titles were saved in an ArrayList. 
With that ArrayList, we will filter through all the apps, to obtain the wanted result. The search will be done with every added letter in the SearchView.
Task 3
In order to obtain a drop down menu, a Spinner was used. The Spinner will contain the data from strings.xml from the string array “features”. The Spinner will be initialized in the ActivityMain. 


#Features:
-	For the sorting part , while loading the data to be displayed, the Comparator interface was used in order to sort the alphabetically. 
-	Refresh feature. For this feature, a SwipeRefreshLayout was used which provides us the following:  when a swipe gesture correctly triggers, a refresh should implement this interface.
-	Design: a nice and visible front end for the list of the application to be more user friendly.
