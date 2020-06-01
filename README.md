# Name:Shimon Arshavsly
#ID:312119126
- ***I implemented both bonuses***.
- To start the project, place all the files under one directory and run through
 cmd **run.bat** .I tried to be as much as possible user friendly,first you should enter path of xml file and then 
 use the application as you wish.  
 - ***Important points***
   - The algorithm gives you best result base on cost and fuel utilization.
   - Capacity take into consideration what time each passenger join and leave the trip.
   - the application is case sensitive.
   - when you get the massage "incorrectly input" its refers the only case you entered not suitable information to what the app expected  
   , any other case getting specific massage depends on the error or success.      
 
 
 - ***Brief explanation of the data set***  
    - **MapDb**:contains all the Nodes
      - **Node**: represent a stop, contain all the trips id's that going through this stop, and also all the path coming out this stop.
    - **TransPoolTrip**:contains all the information about specific trip and got stop manager.
      - **Stop manager**:contains for each stop of the route relevant information about passengers going up and going down , and also update capacity.
    - **Requested trip**: holds all the information about trip, also got a state which change when match is performed.
 - **Engine** - module:
    - **Trip Details** : class that contains static methods to calculate: length of route,cost,average fuel and more..
    - **Map builder** : the main role of this class is to load xml file to all the data set while checking validity of the content
    - **Api** : performed all the capabilities of the engine.
    - Holds: **MapDb**,**TransPool trips**,**Requested trips**,**Matcher**.
    - **Capabilities**:
      - Insert request.
      - Insert TransPool trip.
      - Get all the matches trip to specific request.
      - Get all the requested that have not been matched.
      - Make a match between requested trip and transPool trip
   - **Matcher** : contains methods to find all relevant transPool trips sorted by cost and fuel utilization base on time of desire requested trip.
  - **UserInterface** - module : holds instance of Api, and array of options.
     - **Option** : Functional Interface - one method action().
     - **Each option in the array describe a command which is a class with static methods:** 
       - Load xml file.
       - View all requested trips.
       - View all transPool trips.
       - Add requested trip.
       - Add transPool trip.
       - Find a match to requested trip.
              