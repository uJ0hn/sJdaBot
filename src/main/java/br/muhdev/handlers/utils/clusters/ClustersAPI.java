package br.muhdev.handlers.utils.clusters;

import br.muhdev.backend.Backend;
import br.muhdev.backend.tables.Table;
import br.muhdev.bot.Main;
import lombok.SneakyThrows;

import javax.sql.rowset.CachedRowSet;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ClustersAPI {

    private final int id;
    private static final Table table = Table.tableslist.get("clustersonline");

    public ClustersAPI(int id) {
        this.id = id;
    }

    public boolean clusterDisponible() {
        return Backend.getInstance().query(table.select() + " WHERE clusterid=?", id) != null;
    }

    @SneakyThrows
    public void insertCluster() {
        Backend.getInstance().execute(table.insert(), id, getAddress(), System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    @SneakyThrows
    public static ClustersAPI getLocalCluster() {
        return new ClustersAPI(Main.getInstance().getConfig().getInt("cluster"));
    }

    @SneakyThrows
    private static String getAddress() {
        URLConnection connect = new URL("https://ipv4.icanhazip.com").openConnection();
        connect.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        Scanner scan = new Scanner(connect.getInputStream());
        StringBuilder b = new StringBuilder();
        while (scan.hasNext()) {
            String text = scan.next();
            b.append(text);
        }
        scan.close();
        return b.toString();
    }

    public void deleteCluster() {
        Backend.getInstance().execute(table.delete() + " WHERE clusterid=?", "" + id);
    }



}
