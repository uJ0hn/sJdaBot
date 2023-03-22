package br.muhdev.backend.tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Table {


    final String name;

    public static final Map<String, Table> tableslist = new HashMap<>();

    public static final List<Table> tables = new ArrayList<>();

    public Table(String name) {
        this.name = name;
    }

    public static void init() {
        tableslist.put(new GuildTable().name, new GuildTable());
        tableslist.put(new Clusters().name, new Clusters());
    }

    public abstract String pgcreate();
    public abstract String select();

    public abstract String insert();

    public abstract String update(String value);

    public abstract String delete();

}

