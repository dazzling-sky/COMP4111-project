# COMP4111 Project

## Prerequisities

- JDK 11
    - [Oracle JDK] (https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

- MySQL Server 5.7
    - [MySQL Database] (https://www.mysql.com/downloads/)
    
- NaviCat (GUI database manager)
    - [NaviCat for MySQL] (https://www.navicat.com/en/products/navicat-for-mysql)

# Running the Project

- Set default Java installation as JDK 11. 
- Use **Port 8081** to run RESTful Web service

# Running the Application

Before starting the application, ensure that MySQL server has been started.

Then, initalize the database using 'init.sql' file from the root directory.

To ensure all test cases work, make sure to import 'User-info.csv' into the 'users' table.

At the root of the project. run the following command:

```sh
./gradlew run
```

The application should run properly if output similar to the following is displayed on the console:

```
<=========----> 75% EXECUTING [5s]
> :run
```
