package org.eu.xmon.dbhubapi.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Entry {
    private String entry_type;
    private String last_modified;
    private String license;
    private String name;
    private String sha256;
    private long size;
}
