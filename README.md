# Client

## Deploy

### Requirement

- Maven
- Java jdk 11 or more
- MySQL remote server

### Client

1. In Chatsystem/src/main/resources, setup app.properties depending on deployment environment <br/> Important properties to configure are :
  - dbURL : JDBC URL of the remote database in the form : jdbc:mysql://hsotname:port/databaseName
  - dbLogin : Mysql user login 
  - dbPassword : Mysql user password 
  - presenceServiceURL : Url to reach PresenceService in the form : http://hostname/presenceservice/users 
  - downloadPath : Place where files will be downloaded 
  - embeddedDB : set to "true" to use a SQLite embedded database instead of the remote MySQL database. <br/> Username uniquess is not guaranteed in this case. This should be only used for testing purposes.  

2. Execute "mvn package" command where the pom.xml is located (ChatSystem folder)

3. get the .jar with "with-dependencies" in the name, in ChatSystem/target

4. Execute .jar by double clicking on it or using command "java -jar client.jar"

### Database

1. Install MySQL client with a version corresponding to the MySQL server

2. Connect to the MySQL serveur with following command : "mysql -h hostnaùe -u login -p" <br/>
   You must have create a user with enough privileges to create a database or with access to a database already setup by the server admin beforehand

3. If necessary, create a database with the following command : "create database databaseName ;"

# Presence Service

## Deploy

### Requirement

- Maven
- Java jdk 11 or more
- Tomcat server

### Steps

1. Execute "mvn install" command where the pom.xml is located (PresenceService folder)

2. Get the .war file in PresenceService/target

3. Connect to the tomcat server through a web browser and drop the .war file <br/> 
Or drop the .war in the webapp folder of the tomcat server manually 
