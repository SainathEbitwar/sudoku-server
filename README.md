
# Sudoku - Server

## Think & Move - the sudoku backend game

### As per the requirement I have built the Sudoku-Server backend game using below tech stack

* Java 17
* Spring Boot

### To run this application use below maven command

    mvn spring-boot:run

### Below are the APIs and its description

* POST _/v1/START_


    This API is used for random initialization of sudoku game.
    When this API get called it will create a new sudoko game with a random starting skeleton.
    If there is any in progress game, then it will close that game and start a new game.
    
    The valid request body for this API is "start" or "START" text with accepted Content-Type header with value "text/plain".
    It will return "READY" as a output by creating a sudoku game.

* POST _/v2/START_

    
    This API functions same as above but it accespts sudoko skeleton from user as json array.
    The request bdoy should be in below json array format and the places which are not known need to be 0.
    
        [
          [1,0,4,0,0,0,0,0,9],
          [0,0,0,0,0,0,0,0,0],
          [0,6,5,0,2,0,0,0,0],
          [0,0,9,0,1,0,0,0,6],
          [0,0,0,0,0,8,0,5,1],
          [0,0,0,7,0,0,2,0,4],
          [5,3,0,0,0,2,0,0,0],
          [0,0,6,0,0,3,1,9,2],
          [0,4,0,0,0,6,0,3,0]
        ]

* GET _/move/{row}/{col}/{val}_

    
    This API will place the val as box value on row as a row value and col as acolumn value.
    If the value is valid the it will return an error with remaining attempts
    and 3 consucutive attempts user will get the suggested correct value in response
    Here the values of row and col are from 1 to 9 and val is also from 1 to 9 other values with throw an error
    One game is comepleted it will return response as "Valid : This Sudoku game is completed pls initialize a new one".

* POST _/move_
    

    This API will behave same as above only diffrence is it POST and accet below request body as a JSON
    {

    "row" : 2,
    "col" : 7,
    "val" : 4
    }

* GET _/status_


    This API is addon which I added to get the current status of Sudoku game
    The response is in below format

        1 * 6 * 9 5 * 8 *
        * * * * * * * * 1
        * * 4 * 2 * * * *
        * * 8 * * 3 7 * *
        * * 2 8 * * * 4 3
        7 * * 4 * * * * *
        5 * * * 1 * * 6 *
        * 2 * * * 6 * 7 5
        * 4 * * * * * * *

    where the * is the values user needs to figure out
