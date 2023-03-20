package br.muhdev.handlers.utils.clusters;

import br.muhdev.backend.Backend;
import br.muhdev.backend.tables.Table;
import lombok.SneakyThrows;

public class GuildCApi {

    private final String guildid;
    private static final Table table= Table.tableslist.get("sguild");


    public GuildCApi(String guildid) {
        this.guildid = guildid;
    }

    @SneakyThrows
    public int getClusterId() {
        System.out.println(Backend.getInstance().query(table.select() + " WHERE guildid=?", guildid));
        return Backend.getInstance().query(table.select() + " WHERE guildid=?", guildid).getInt("cluster");
    }


    public boolean isTheCluster() {
        int guildcluster = getClusterId();
        return guildcluster == ClustersAPI.getLocalCluster().getId();
    }


}
