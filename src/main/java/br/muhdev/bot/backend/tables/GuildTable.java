package br.muhdev.bot.backend.tables;

public class GuildTable extends Table{

    public GuildTable() {
        super("sguild");
    }

    @Override
    public String pgcreate() {
        return "CREATE TABLE IF NOT EXISTS sguild(" +
                "guildid TEXT PRIMARY KEY," +
                "cluster TEXT," +
                "ticket TEXT," +
                "tickets TEXT" +
                ");";
    }

    @Override
    public String select() {
        return "SELECT * FROM sguild";
    }

    @Override
    public String insert() {
        return "INSERT INTO sguild VALUES(?, ?, ?, ?)";
    }

    @Override
    public String update(String value) {
        return "UPDATE sguild SET " + value ;
    }

    @Override
    public String delete() {
        return "DELETE FROM sguiild WHERE guildid=?";
    }

}
