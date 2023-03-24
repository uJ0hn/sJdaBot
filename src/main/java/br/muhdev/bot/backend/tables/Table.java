package br.muhdev.bot.backend.tables;

import java.util.HashMap;
import java.util.Map;

public abstract class Table {


    final String name;

    public static final Map<String, Table> tableslist = new HashMap<>();

    public Table(String name) {
        this.name = name;
    }

    public static void init() {
        tableslist.put(new GuildTable().name, new GuildTable());
        tableslist.put(new Clusters().name, new Clusters());
        tableslist.put(new UserTable().name, new UserTable());
    }

    public abstract String pgcreate();
    public abstract String select();

    public abstract String insert();

    public abstract String update(String value);

    public abstract String delete();

}

