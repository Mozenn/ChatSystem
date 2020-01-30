# Client

## Deploy

### Requirement

- Maven
- Java jdk 11 or more
- MySQL remote server

### Client

1. In Chatsystem/src/main/resources, setup app.properties depending on deployment environment \ Important properties to configure are :
  - dbURL :
  - dbLogin :
  - dbPassword :
  - presenceServiceURL :
  - downloadPath :
  - embeddedDB :

2. Execute "mvn package" command where the pom.xml is located (ChatSystem folder)

3. get the .jar with "with-dependencies" in the name, in ChatSystem/target

4. Execute .jar by double clicking on it or using command "java -jar client.jar"

### Database

1. Install MySQL client with a version corresponding to the MySQL server

2. Conect to the MySQL serveur with following command : "mysql -h hostnaùe -u login -p"
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

3. Connect to the tomcat server through a web browser and drop the .war file \ 
Or drop the .war in the webapp folder of the tomcat server manually 
