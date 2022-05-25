# xmonDbHub
A Java library for accessing and using SQLite databases on DBHub.io

Discord for help https://discord.gg/qyeDzjrfkv

# How to use?
```java
final DbHubDatabase dbHubDatabase = new DbHubDatabase("API_KEY", "DB_OWNER", "DB_NAME");
//or
final DbHubDatabase dbHubDatabase = new DbHubDatabase.Builder()
                .apikey("API_KEY")
                .dbname("DB_NAME")
                .dbowner("DB_OWNER")
                .build();
//or
final DbHubDatabase dbHubDatabase = DbHubDatabase.builder()
                .apikey("API_KEY")
                .dbname("DB_NAME")
                .dbowner("DB_OWNER")
                .build();

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
```xml
<repository>
  <id>xmon-repo-snapshots</id>
  <name>Xmon Repository</name>
  <url>https://repo.xmon.eu.org/snapshots</url>
</repository>

<dependency>
    <groupId>org.eu.xmon</groupId>
    <artifactId>dbhubapi</artifactId>
    <version>1.2-SNAPSHOT</version>
</dependency>
```
