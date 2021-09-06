package org.eu.xmon.dbhubapi.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Commit {
    private String author_email;
    private String author_name;
    private String committer_email;
    private String committer_name;
    private String id;
    private String message;
    private Object[] other_parents;
    private String parent;
    private String timestamp;
    private Tree tree;
}
