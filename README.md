# Compass Challenge
Compass Challenge

## Get started

To run this application in Java IDE you first need to build the project with maven (https://maven.apache.org/) <br/>
You can use the command: <br/>
```
mvn clean install
```

Next, import the project in one Java IDE and run the main method in the main class. <br/>
The Main class of this application is CompassApplication.java <br/>

You can also run the jar file in the root of this project <br/>
Install Java JDK 1.8 or higher in you computer and use the command: <br/>
```
java -jar challenge-0.0.1-SNAPSHOT.jar
```

## H2 Database

There is a database in memory called H2 <br/>
You can access this database with `/h2-console` URI in a normal browser. <br/>
For example `http://localhost:8080/h2-console` <br/>
Using this configuration: <br/>
- <br/>
Saved Settings: Generic H2 (Embedded) <br/>
Driver Class: org.h2.Driver <br/>
JDBC URL: jdbc:h2:mem:testdb <br/>
User Name: sa <br/>
Password: <br/>
- <br/>

## Insert Data

There is the file `data.sql` in `src/main/resources` directory with some inserts of datas. <br/>
It should load at the beginning of the application. <br/>
But you can copy the content of this file and run manually using `/h2-console` URI to populate the database. <br/>

## Postman Examples

A Postman file called `Compass Challenge.postman_collection.json` is committed in the directory `src/main/doc` <br/>
This file contains examples of URI's and json objects to use in this application. <br/>
You can see examples of methods GET and PUT. <br/>
You need to download the Postman application in (`https://www.getpostman.com`) and import this file to see and run this examples.

