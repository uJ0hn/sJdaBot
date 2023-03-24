package br.muhdev.bot.handlers.utils.clusters;

import br.muhdev.bot.backend.Backend;
import br.muhdev.bot.backend.tables.Table;
import lombok.SneakyThrows;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class GuildCApi {

    private final String guildid;
    private static final Table table= Table.tableslist.get("sguild");


    public GuildCApi(String guildid) {
        this.guildid = guildid;
    }

    @SneakyThrows
    public int getClusterId() {
        int c = Backend.getInstance().query(table.select() + " WHERE guildid=?", "" + guildid).getInt("cluster");
        CachedRowSet query = Backend.getInstance().query(Table.tableslist.get("clustersonline").select() + " WHERE clusterid=?", "" +c);
        int id;
        if(query == null) {
            id = getRandomCluster();
            Backend.getInstance().execute(table.update("cluster=?") + " WHERE guildid=?", "" +id, "" +guildid);
        } else id = c;

        return id;
    }


    public boolean isTheCluster() {
        int guildcluster = getClusterId();
        return guildcluster == ClustersAPI.getLocalCluster().getId();
    }

    @SneakyThrows
    public static int getRandomCluster() {
        Table tablee= Table.tableslist.get("clustersonline");
        CachedRowSet q = Backend.getInstance().query(tablee.select());
        if(q == null) return 1;
        List<Integer> integers = new ArrayList<>();
        for(int i = 0 ; i < q.size() ; i++) {
            integers.add(q.getInt("clusterid"));
            q.next();
        }

        return integers.get((int) (Math.random() * integers.size()));
    }


}
