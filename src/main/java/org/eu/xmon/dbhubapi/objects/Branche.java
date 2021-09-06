package org.eu.xmon.dbhubapi.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data @AllArgsConstructor @RequiredArgsConstructor @ToString
public class Branche {
    private String name;
    private String commit;
    private int commit_count;
    private String description;
    private String default_branch;
}
