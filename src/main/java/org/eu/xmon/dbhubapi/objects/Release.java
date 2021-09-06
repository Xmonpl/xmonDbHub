package org.eu.xmon.dbhubapi.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Release {
    private String name;
    private String commit;
    private String date;
    private String description;
    private String email;
    private String author;
    private long size;
}
