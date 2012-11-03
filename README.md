# Nounou Application

This application aims to manage planning and reports for the nurse.
the application is available on heroku http://hello-mlecoutre.herokuapp.com/

## Use cases
* Declare some User of type [Nurse, Father, Mother, Friend, Family, Child]
** list of users that have rights to get the child.
* Each time you get your child, you start the app and declare the action
* At _posteriory_ modify or create the declaration
* Manage planning
* Manage reports per month
* Manage alerts (declare event on your personal calendar)


## Architecture
* _nounou-app_ gather the core services hosted on a tomcat7 in the cloud (heroku platform)
** Rest API services to declare User, Planning and Reports
** Web Interface for PC & mobile
* We will define a bit later some specific client on android and iTrucs

## Technologies
* Rest services
* JPA
* CDI (todo)
* Twitter bootstrap, mustache.js jQuery, dbUnit
    
## Running the application locally
Run your application

To build your application simply run:

$ mvn package
And then run your app using the java command:

$ java -jar target/dependency/webapp-runner.jar target/*.war

