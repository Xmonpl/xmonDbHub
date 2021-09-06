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
public class Tree {
    private String id;
    private List<Entry> entries;
}
