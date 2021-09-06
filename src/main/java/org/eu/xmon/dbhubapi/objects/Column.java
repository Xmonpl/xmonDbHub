package org.eu.xmon.dbhubapi.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Column {
    private int column_id;
    private String name;
    private String data_type;
    private boolean not_null;
    private String default_value;
    private int primary_key;
}
