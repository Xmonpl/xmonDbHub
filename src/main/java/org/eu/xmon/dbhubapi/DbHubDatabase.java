package org.eu.xmon.dbhubapi;

import lombok.NonNull;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.request.body.multipart.FilePart;
import org.asynchttpclient.request.body.multipart.Part;
import org.asynchttpclient.request.body.multipart.StringPart;
import org.eu.xmon.dbhubapi.objects.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.asynchttpclient.Dsl.asyncHttpClient;


public class DbHubDatabase {
    private String apikey;
    private String dbowner;
    private String dbname;
    private AsyncHttpClient asyncHttpClient;

    public DbHubDatabase(final String apikey, final String dbowner, final String dbname){
        this.apikey = apikey;
        this.dbowner = dbowner;
        this.dbname = dbname;
        this.asyncHttpClient = asyncHttpClient();
    }

    public DbHubDatabase closeConnection(){
        try {
            this.asyncHttpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.asyncHttpClient = null;
        return this;
    }

    public CompletableFuture<Optional<List<Column>>> getColumns(@NonNull final String table){
        if (asyncHttpClient != null) {
            return asyncHttpClient
                    .prepareGet("https://api.dbhub.io/v1/columns")
                    .addHeader("Content-Type", "multipart/form-data")
                    .setBodyParts(List.of(new StringPart("apikey", this.apikey), new StringPart("dbowner", this.dbowner), new StringPart("dbname", this.dbname), new StringPart("table", table)))
                    .execute()
                    .toCompletableFuture()
                    .thenApply(x -> {
                        final JSONArray jsonArray = new JSONArray(x.getResponseBody(StandardCharsets.UTF_8));
                        List<Column> columns = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            columns.add(new Column(jsonArray.getJSONObject(i).getInt("column_id"), jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("data_type"), jsonArray.getJSONObject(i).getBoolean("not_null"), jsonArray.getJSONObject(i).getString("default_value"), jsonArray.getJSONObject(i).getInt("primary_key")));
                        }
                        return Optional.of(columns);
                    }).exceptionally(x -> Optional.empty());
        }else{
            CompletableFuture<Optional<List<Column>>> completableFuture = new CompletableFuture<>();
            completableFuture.complete(Optional.empty());
            return completableFuture;
        }
    }

    public CompletableFuture<Optional<List<Commit>>> getCommits(){
        if (asyncHttpClient != null) {
            return asyncHttpClient
                    .prepareGet("https://api.dbhub.io/v1/commits")
                    .addHeader("Content-Type", "multipart/form-data")
                    .setBodyParts(List.of(new StringPart("apikey", this.apikey), new StringPart("dbowner", this.dbowner), new StringPart("dbname", this.dbname)))
                    .execute()
                    .toCompletableFuture()
                    .thenApply(x -> {
                        final JSONObject jsonObject = new JSONObject(x.getResponseBody(StandardCharsets.UTF_8));
                        return Optional.of( jsonObject.keySet().stream().map(key ->{
                            final JSONObject object = jsonObject.getJSONObject(key);
                            final Commit commit = new Commit();
                            commit.setAuthor_email(object.getString("author_email"));
                            commit.setAuthor_name(object.getString("author_name"));
                            commit.setCommitter_email(object.getString("committer_email"));
                            commit.setCommitter_name(object.getString("committer_name"));
                            commit.setId(object.getString("id"));
                            commit.setMessage(object.getString("message"));
                            commit.setOther_parents(null);
                            commit.setParent(object.getString("parent"));
                            commit.setTimestamp(object.getString("timestamp"));
                            final JSONObject treeObject = object.getJSONObject("tree");
                            final Tree tree = new Tree();
                            tree.setId(treeObject.getString("id"));
                            List<Entry> entries = new ArrayList<>();
                            for (int i = 0; i < treeObject.getJSONArray("entries").length(); i++) {
                                final Entry entryObject = new Entry();
                                entryObject.setEntry_type(treeObject.getJSONArray("entries").getJSONObject(i).getString("entry_type"));
                                entryObject.setLast_modified(treeObject.getJSONArray("entries").getJSONObject(i).getString("last_modified"));
                                entryObject.setLicense(treeObject.getJSONArray("entries").getJSONObject(i).getString("licence"));
                                entryObject.setName(treeObject.getJSONArray("entries").getJSONObject(i).getString("name"));
                                entryObject.setSha256(treeObject.getJSONArray("entries").getJSONObject(i).getString("sha256"));
                                entryObject.setSize(treeObject.getJSONArray("entries").getJSONObject(i).getInt("size"));
                                entries.add(entryObject);
                            }
                            tree.setEntries(entries);
                            commit.setTree(tree);
                            return commit;
                        }).collect(Collectors.toList()));
                    }).exceptionally(x -> Optional.empty());
        }else{
            CompletableFuture<Optional<List<Commit>>> completableFuture = new CompletableFuture<>();
            completableFuture.complete(Optional.empty());
            return completableFuture;
        }
    }
    public CompletableFuture<Optional<List<String>>> getDatabases(){
        if (asyncHttpClient != null) {
            return asyncHttpClient
                    .prepareGet("https://api.dbhub.io/v1/databases")
                    .addHeader("Content-Type", "multipart/form-data")
                    .setBodyParts(List.of(new StringPart("apikey", this.apikey)))
                    .execute()
                    .toCompletableFuture()
                    .thenApply(x -> Optional.of(new JSONArray(x.getResponseBody(StandardCharsets.UTF_8)).toList().stream().map(y -> (String) y).collect(Collectors.toList())))
                    .exceptionally(x -> Optional.empty());
        }else{
            CompletableFuture<Optional<List<String>>> completableFuture = new CompletableFuture<>();
            completableFuture.complete(Optional.empty());
            return completableFuture;
        }
    }

    public CompletableFuture<Optional<String>> getWebPage(){
        if (asyncHttpClient != null) {
            return asyncHttpClient
                    .prepareGet("https://api.dbhub.io/v1/webpage")
                    .addHeader("Content-Type", "multipart/form-data")
                    .setBodyParts(List.of(new StringPart("apikey", this.apikey), new StringPart("dbowner", this.dbowner), new StringPart("dbname", this.dbname)))
                    .execute()
                    .toCompletableFuture()
                    .thenApply(x ->Optional.of(new JSONObject(x.getResponseBody(StandardCharsets.UTF_8)).getString("web_page")))
                    .exceptionally(x -> Optional.empty());
        }else{
            CompletableFuture<Optional<String>> completableFuture = new CompletableFuture<>();
            completableFuture.complete(Optional.empty());
            return completableFuture;
        }
    }

    public CompletableFuture<Optional<List<String>>> getViews(){
        if (asyncHttpClient != null) {
            return asyncHttpClient
                    .prepareGet("https://api.dbhub.io/v1/views")
                    .addHeader("Content-Type", "multipart/form-data")
                    .setBodyParts(List.of(new StringPart("apikey", this.apikey), new StringPart("dbowner", this.dbowner), new StringPart("dbname", this.dbname)))
                    .execute()
                    .toCompletableFuture()
                    .thenApply(x ->Optional.of(new JSONArray(x.getResponseBody(StandardCharsets.UTF_8)).toList().stream().map(y -> (String) y).collect(Collectors.toList())))
                    .exceptionally(x -> Optional.empty());
        }else{
            CompletableFuture<Optional<List<String>>> completableFuture = new CompletableFuture<>();
            completableFuture.complete(Optional.empty());
            return completableFuture;
        }
    }

    public CompletableFuture<Optional<List<Release>>> getReleases(){
        if (asyncHttpClient != null) {
            return asyncHttpClient
                    .prepareGet("https://api.dbhub.io/v1/releases")
                    .addHeader("Content-Type", "multipart/form-data")
                    .setBodyParts(List.of(new StringPart("apikey", this.apikey), new StringPart("dbname", this.dbname), new StringPart("dbowner", this.dbowner)))
                    .execute()
                    .toCompletableFuture()
                    .thenApply(x -> {
                        final JSONObject jsonObject = new JSONObject(x.getResponseBody(StandardCharsets.UTF_8));
                        final List<Release> releases = jsonObject.keySet().stream().map(key ->{
                            final JSONObject object = jsonObject.getJSONObject(key);
                            final Release release = new Release();
                            release.setName(key);
                            release.setAuthor(object.getString("name"));
                            release.setDate(object.getString("date"));
                            release.setCommit(object.getString("commit"));
                            release.setDescription(object.getString("description"));
                            release.setEmail(object.getString("email"));
                            release.setSize(object.getInt("size"));
                            return release;
                        }).collect(Collectors.toList());
                        return Optional.of(releases);
                    }).exceptionally(x -> Optional.empty());
        }else{
            CompletableFuture<Optional<List<Release>>> completableFuture = new CompletableFuture<>();
            completableFuture.complete(Optional.empty());
            return completableFuture;
        }
    }

    public CompletableFuture<Optional<List<Tag>>> getTags(){
        if (asyncHttpClient != null) {
            return asyncHttpClient
                    .prepareGet("https://api.dbhub.io/v1/tags")
                    .addHeader("Content-Type", "multipart/form-data")
                    .setBodyParts(List.of(new StringPart("apikey", this.apikey), new StringPart("dbname", this.dbname), new StringPart("dbowner", this.dbowner)))
                    .execute()
                    .toCompletableFuture()
                    .thenApply(x -> {
                        final JSONObject jsonObject = new JSONObject(x.getResponseBody(StandardCharsets.UTF_8));
                        final List<Tag> tags = jsonObject.keySet().stream().map(key ->{
                            final JSONObject object = jsonObject.getJSONObject(key);
                            final Tag tag = new Tag();
                            tag.setDate(object.getString("date"));
                            tag.setCommit(object.getString("commit"));
                            tag.setDescription(object.getString("description"));
                            tag.setEmail(object.getString("email"));
                            tag.setName(key);
                            return tag;
                        }).collect(Collectors.toList());
                        return Optional.of(tags);
                    }).exceptionally(x -> Optional.empty());
        }else{
            CompletableFuture<Optional<List<Tag>>> completableFuture = new CompletableFuture<>();
            completableFuture.complete(Optional.empty());
            return completableFuture;
        }
    }

    public CompletableFuture<Optional<List<String>>> getTables(){
        if (asyncHttpClient != null) {
            return asyncHttpClient
                    .prepareGet("https://api.dbhub.io/v1/tables")
                    .addHeader("Content-Type", "multipart/form-data")
                    .setBodyParts(List.of(new StringPart("apikey", this.apikey), new StringPart("dbname", this.dbname), new StringPart("dbowner", this.dbowner)))
                    .execute()
                    .toCompletableFuture()
                    .thenApply(x ->Optional.of(new JSONArray(x.getResponseBody(StandardCharsets.UTF_8)).toList().stream().map(y -> (String) y).collect(Collectors.toList())))
                    .exceptionally(x -> Optional.empty());
        }else{
            CompletableFuture<Optional<List<String>>> completableFuture = new CompletableFuture<>();
            completableFuture.complete(Optional.empty());
            return completableFuture;
        }
    }

    public CompletableFuture<Status> delete(){
        if (asyncHttpClient != null) {
            return asyncHttpClient
                    .prepareGet("https://api.dbhub.io/v1/delete")
                    .addHeader("Content-Type", "multipart/form-data")
                    .setBodyParts(List.of(new StringPart("apikey", this.apikey), new StringPart("dbname", this.dbname)))
                    .execute()
                    .toCompletableFuture()
                    .thenApply(x -> {
                        final String status = new JSONObject(x.getResponseBody(StandardCharsets.UTF_8)).getString("status").toUpperCase(Locale.ROOT);
                        if (Status.valueOf(status) == Status.OK){
                            return Status.OK;
                        }
                        return Status.ERROR;
                    }).exceptionally(x -> Status.ERROR);
        }else{
            CompletableFuture<Status> completableFuture = new CompletableFuture<>();
            completableFuture.complete(Status.ERROR);
            return completableFuture;
        }
    }

    public CompletableFuture<Status> upload(@NonNull File location, @NonNull String branch, @NonNull String commit_name){
        if (asyncHttpClient != null) {
            final Optional<List<Branche>> branches;
            try {
                branches = this.getBranches().get();
                if (branches.isPresent()){
                    final Optional<Branche> branche = branches.get().stream().filter(y -> y.getName().equals(branch)).findFirst();
                    if (branche.isPresent()){
                        final List<Part> parts = List.of(
                                new StringPart("apikey", this.apikey),
                                new StringPart("dbname", this.dbname),
                                new StringPart("branch", branch),
                                new StringPart("commit", branche.get().getCommit()),
                                new StringPart("commitmsg", commit_name),
                                new StringPart("lastmodified", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(location.lastModified())),
                                new FilePart("file", location, "application/x-sqlite3", StandardCharsets.UTF_8)
                        );
                        return asyncHttpClient
                                .prepareGet("https://api.dbhub.io/v1/upload")
                                .addHeader("Content-Type", "multipart/form-data")
                                .setBodyParts(parts)
                                .execute()
                                .toCompletableFuture()
                                .thenApply(o -> {
                                    if (o.getStatusCode() == 200){
                                        return Status.OK;
                                    }else{
                                        System.out.println(o.getStatusCode());
                                        System.out.println(o.getResponseBody());
                                        return Status.ERROR;
                                    }
                                }).exceptionally(o -> Status.ERROR);
                    }else{
                        CompletableFuture<Status> completableFuture = new CompletableFuture<>();
                        completableFuture.complete(Status.ERROR);
                        return completableFuture;
                    }
                }else{
                    CompletableFuture<Status> completableFuture = new CompletableFuture<>();
                    completableFuture.complete(Status.ERROR);
                    return completableFuture;
                }
            } catch (InterruptedException | ExecutionException e) {
                CompletableFuture<Status> completableFuture = new CompletableFuture<>();
                completableFuture.complete(Status.ERROR);
                return completableFuture;
            }
        } else {
            CompletableFuture<Status> completableFuture = new CompletableFuture<>();
            completableFuture.complete(Status.ERROR);
            return completableFuture;
        }
    }
    public CompletableFuture<Status> download(@NonNull File location){
        if (asyncHttpClient != null) {
            return asyncHttpClient
                    .prepareGet("https://api.dbhub.io/v1/download")
                    .addHeader("Content-Type", "multipart/form-data")
                    .setBodyParts(List.of(new StringPart("apikey", this.apikey), new StringPart("dbname", this.dbname), new StringPart("dbowner", this.dbowner)))
                    .execute()
                    .toCompletableFuture()
                    .thenApply(x -> {
                        try (FileOutputStream fos = new FileOutputStream(location.getAbsolutePath())) {
                            fos.write(x.getResponseBodyAsBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return Status.OK;
                    }).exceptionally(x -> Status.ERROR);
        }else{
            CompletableFuture<Status> completableFuture = new CompletableFuture<>();
            completableFuture.complete(Status.ERROR);
            return completableFuture;
        }
    }
    public CompletableFuture<Optional<String>> sendQuery(@NonNull final String query){
        if (asyncHttpClient != null) {
            return asyncHttpClient
                    .prepareGet("https://api.dbhub.io/v1/query")
                    .addHeader("Content-Type", "multipart/form-data")
                    .setBodyParts(List.of(new StringPart("apikey", this.apikey), new StringPart("dbowner", this.dbowner), new StringPart("dbname", this.dbname), new StringPart("sql", Base64.getEncoder().encodeToString(query.getBytes(StandardCharsets.UTF_8)))))
                    .execute()
                    .toCompletableFuture()
                    .thenApply(x -> Optional.of( x.getResponseBody(StandardCharsets.UTF_8)))
                    .exceptionally(x -> Optional.empty());
        }else{
            CompletableFuture<Optional<String>> completableFuture = new CompletableFuture<>();
            completableFuture.complete(Optional.empty());
            return completableFuture;
        }
    }
    public CompletableFuture<Optional<List<Branche>>> getBranches(){
        if (asyncHttpClient != null) {
            return asyncHttpClient
                    .prepareGet("https://api.dbhub.io/v1/branches")
                    .addHeader("Content-Type", "multipart/form-data")
                    .setBodyParts(List.of(new StringPart("apikey", this.apikey), new StringPart("dbowner", this.dbowner), new StringPart("dbname", this.dbname)))
                    .execute()
                    .toCompletableFuture()
                    .thenApply(x -> {
                        final JSONObject jsonObject = new JSONObject(x.getResponseBody(StandardCharsets.UTF_8));
                        return Optional.of(jsonObject.getJSONObject("branches").keySet().stream().map(key -> {
                            final JSONObject keys = jsonObject.getJSONObject("branches").getJSONObject(key);
                            return new Branche(key, keys.getString("commit"), keys.getInt("commit_count"), keys.getString("description"), jsonObject.getString("default_branch"));
                        }).collect(Collectors.toList()));
                    }).exceptionally(x -> Optional.empty());
        }else{
            CompletableFuture<Optional<List<Branche>>> completableFuture = new CompletableFuture<>();
            completableFuture.complete(Optional.empty());
            return completableFuture;
        }
    }

    public static final class Builder{
        private String apikey;
        private String dbowner;
        private String dbname;

        public Builder apikey(final String apikey){
            this.apikey = apikey;
            return this;
        }

        public Builder dbowner(final String dbowner){
            this.dbowner = dbowner;
            return this;
        }

        public Builder dbname(final String dbname){
            this.dbname = dbname;
            return this;
        }

        public static Builder builder() {
            return new Builder();
        }

        public DbHubDatabase build(){
            if (apikey.isEmpty()){
                throw new IllegalStateException("Apikey cannot be empty");
            }
            if (dbname.isEmpty()){
                throw new IllegalStateException("DbName cannot be empty");
            }
            if (dbowner.isEmpty()){
                throw new IllegalStateException("DbOwner cannot be empty");
            }
            return new DbHubDatabase(this.apikey, this.dbowner, this.dbname);
        }
    }
}
