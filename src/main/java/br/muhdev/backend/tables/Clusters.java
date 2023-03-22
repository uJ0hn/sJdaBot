package br.muhdev.backend.tables;

public class Clusters extends Table{

    public Clusters() {
        super("clustersonline");
    }

    @Override
    public String pgcreate() {
        return "CREATE TABLE IF NOT EXISTS clustersonline (" +
                "clusterid TEXT PRIMARY KEY," +
                "clusterip TEXT," +
                "onlinein TEXT," +
                "clusterport TEXT" +
                ");";
    }

    @Override
    public String select() {
        return "SELECT * FROM clustersonline";
    }

    @Override
    public String insert() {
        return "INSERT INTO clustersonline VALUES (?, ?, ?, ?)";
    }

    @Override
    public String update(String value) {
        return "UPDATE clustersonline SET " + value + " WHERE clusterid=?";
    }

    @Override
    public String delete() {
        return "DELETE FROM clustersonline";
    }
}
