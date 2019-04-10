# CrimeSpotV1
###### A simple crime statistics app, used to get UK crime data based on location, crime categories and dates.

## Introduction

This project is a working process for a University assignment. The aim of the assignment is to effectively make use of external APIs and parsing multi length JSON strings.

My code implementation makes use of the MVC design pattern, Response Listeners for Volley Requests and good design practice throughout. I aim to make my code as maintainable and dynamic as possible.

## Feature List
###### Search Features
* **Query Locations**, the app allows you to enter a UK based location: "folkestone", "hythe" etc. Using the Google GEOCoding API it will find a best matched location.
* **Query Month**, you can query a location for a single month of a certain year. Extended date ranges will be added soon, see Future Implementations for more information on this.
* **Query Crime Categories**, if desired, you can query a specific crime category, or all categories at the same time.

###### Specific Crime Features
* **List/Map View**, toggle between a list or map representation of your crime query.
* **Specific Crime Info**, displays all information and location based on a specific crime from the crimes list. 
* **Save Specific Crime**, this saves the current specific crime to the local SQLite Database (phone memory) which allows storing of crimes to allow persistent data throughout instances of the app.

###### Menu Features
* **Saved Crimes**, using a SQLite database, you can save specific crimes to be viewed at a later date, as mentioned previously this will remain persistent throughout all instances of the app. View all saved crimes (SQLite Database).
* **Recent Searches/Crimes**, all searches and specific crimes viewed are saved in phone memory. This allows you to have a saved log of everything you've looked at specific to the instance of the app (doesn't save upon app closure). View all recent searches/crimes.
* **Clear All Saved Crimes**, clears the SQLite database, removing **ALL** saved crimes.
* **Delete Saved Crime**, clears the specific crime from SQLite database, only removing **ONE** saved crime.


## Known Limitations
* **Multiple Categories**, Due to limitations with the UK Police API you cannot query multiple categories at the same time, without significant load on the application running memory.
* **Larger Date Ranges**, Due to the format of the data received from the UK Police API, date ranges have to be month specific. I plan to implement a multiple month query which will take longer to process, however, would allow output of multiple months. See Future Implementations for more info.
* **Specific Radius**, To be implemented, see Future Implementations.


## Future Implementations
* **Custom Query Radius**, currently each query is based on a 1 mile radius for the given based latitude and logitude of the town (from the Google GEOCoding API). Custom ranges to allow a larger than 1 mile radius will be implemented in the future.
* **Larger Date Range**, due to the UK Police API all data requested is return based on specific months of the year. Querying larger date ranges would have to be done as individual requests which puts significant load on the app. Larger date ranges will be implemented in the future, once a more optimized and efficient method for doing so has been found.
