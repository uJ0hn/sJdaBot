package br.muhdev.bot.backend.tables;

public class UserTable extends Table{
    public UserTable() {
        super("susers");
    }

    @Override
    public String pgcreate() {
        return "CREATE TABLE IF NOT EXISTS susers (" +
                "id TEXT PRIMARY KEY," +
                "userid TEXT," +
                "economy TEXT," +
                "joined TEXT);";
    }

    @Override
    public String select() {
        return "SELECT * FROM susers";
    }

    @Override
    public String insert() {
        return "INSERT INTO susers VALUES( ?, ?, ?, ?)";
    }

    @Override
    public String update(String value) {
        return "UPDATE susers SET " + value;
    }

    @Override
    public String delete() {
        return "DELETE FROM susers";
    }
}
