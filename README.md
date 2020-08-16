# Name:Shimon Arshavsly

Car managment application.
-Build the application with maven
- I used the Jfeonix library to get special style to my application.
- After loading the map - xml file (you have exmples maps in xml files folder) you will see on the center the map and underneath table of all trip requests and offers with the relevant details.
- By clicking on row of the offer table you will see animation of the specific route of this offer, which you can cancel by clicking on the right pop up on animation on.
- By clicking twice on any row at the table you will get full details about offer/request . - After set a match you will see relevant updates at the request table.
- by clicking twice on offer row you will see buttons for rank and feedbacks, the rank is getting by choosing how many stars you are willing to endow for the driver, the rank will be updated automatically to the component by stars icons(also half star icon for a not integer rank)
- On the map you will see the current time and for each stop you will see the number of cars that currently at this station, also by passing the mouse you will get the name and coordinates, and by clicking you will see for each trip the relevant capacity, id, passengers are leave and join and also the all passengers that attached to the specific trip. Off course for repeated trip the details are changing according to the current time.
  
- on trip offer you setting a route by changing the station in the combo box and it will updated on the text area above , the text area is disable so the color is grey and maybe it's not so comprehend but I didn’t find any other prettier option.
- Match algorithm is based on BFS using queue , my algorithm sort all the result by number of trip changes, then time and then cost and average foul consumption. Also the algorithm is not allowing repeated stations because the mathematical fact of cycles in graph must have a simple route (without cycles).
              