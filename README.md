# xmonDbHub
A Java library for accessing and using SQLite databases on DBHub.io

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
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
    <groupId>com.github.Xmonpl</groupId>
    <artifactId>xmonDbHub</artifactId>
    <version>1.1-SNAPSHOT</version>
</dependency>
```
