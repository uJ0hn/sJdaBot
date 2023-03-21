package br.muhdev.backend;

import br.muhdev.backend.tables.Table;
import br.muhdev.bot.Main;
import lombok.SneakyThrows;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PostgreSQLBackend extends Backend{

    private Connection connection;

    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final ExecutorService executor;
    private final String password;

    @SneakyThrows
    public PostgreSQLBackend() {
        Class.forName("org.postgresql.Driver");
        this.host = Main.getInstance().getConfig().getString("database.remote.address");
        this.port = Main.getInstance().getConfig().getString("database.remote.port");
        this.database = Main.getInstance().getConfig().getString("database.remote.database");
        this.username = Main.getInstance().getConfig().getString("database.remote.user");
        this.password = Main.getInstance().getConfig().getString("database.remote.password");

        this.executor = Executors.newCachedThreadPool();
        openConnection();
        Table.init();
        for(Map.Entry<String, Table> table : Table.tableslist.entrySet()) {
            update(table.getValue().pgcreate());
        }
    }


    public void openConnection() {
        try {
            this.connection = DriverManager.getConnection("jdbc:postgresql://"+ host +":" + port +"/"+ database, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void closeConnection() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PreparedStatement prepareStatement(String query, Object... vars) {
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            for (int i = 0; i < vars.length; i++) {
                ps.setObject(i + 1, vars[i]);
            }
            return ps;
        } catch (SQLException e) {
            System.out.println("Cannot Prepare Statement: " +e);
        }

        return null;
    }

    @Override
    public void update(String sql, Object... vars) {
        try {
            PreparedStatement ps = prepareStatement(sql, vars);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Cannot execute SQL: " + e);
        }
    }

    @Override
    public void execute(String sql, Object... vars) {
        executor.execute(() -> update(sql, vars));
    }


    @Override
    public CachedRowSet query(String sql, Object... vars) {
        CachedRowSet rowSet = null;
        try {
            Future<CachedRowSet> future = executor.submit(() -> {
                try {
                    PreparedStatement ps = prepareStatement(sql, vars);

                    ResultSet rs = ps.executeQuery();
                    CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
                    crs.populate(rs);
                    rs.close();
                    ps.close();

                    if (crs.next()) {
                        return crs;
                    }
                } catch (Exception e) {
                    System.out.println("Cannot execute Query: " + e);
                }

                return null;
            });

            if (future.get() != null) {
                rowSet = future.get();
            }
        } catch (Exception e) {
            System.out.println("Cannot call FutureTask: " + e);
        }

        return rowSet;
    }
}
