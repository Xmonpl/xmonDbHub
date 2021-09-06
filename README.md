# xmonDbHub
A Java library for accessing and using SQLite databases on DBHub.io

# How to use?
```java
final DbHubDatabase dbHubDatabase = new DbHubDatabase("API_KEY", "OWNERDB_NAME", "DB_NAME");

//Returns information about all available Branches
dbHubDatabase.getBranches().thenAccept(System.out::println);

//Returns information about all available Columns in table 'users'
dbHubDatabase.getColumns("users").thenAccept(System.out::println);

//Returns information about all available commits
dbHubDatabase.getCommits().thenAccept(System.out::println);

//Returns information about all available database
dbHubDatabase.getDatabases().thenAccept(System.out::println);

//Download a latest database to file
dbHubDatabase.download(new File("database.db")).thenAccept(System.out::println);

//Send a query request to database (Web) not local db
dbHubDatabase.sendQuery("SELECT * FROM `users`").thenAccept(System.out::println);

//Returns information about all available tables
dbHubDatabase.getTables().thenAccept(System.out::println);

//Upload a database from disk
dbHubDatabase.upload(new File("database.db"), "master", "Test from api").thenAccept(System.out::println);

//Returns information about all releases
dbHubDatabase.getReleases().thenAccept(System.out::println);

//Return link to webpage
dbHubDatabase.getWebPage().thenAccept(System.out::println);

//Returns information about all tags
dbHubDatabase.getTags().thenAccept(System.out::println);

//Close connection
dbHubDatabase.closeConnection();
```

# Maven
1. Authenticate to GitHub Packages. For more information, see [Authenticating with a personal access token](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#authenticating-with-a-personal-access-token)
2. Add the package dependencies to the dependencies element of your project pom.xml file.
```maven
<dependency>
  <groupId>org.eu.xmon</groupId>
  <artifactId>dbhubapi</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
