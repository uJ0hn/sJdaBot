package br.muhdev.bot.listeners;


import br.muhdev.backend.Backend;
import br.muhdev.backend.tables.Table;
import br.muhdev.handlers.utils.clusters.ClustersAPI;
import br.muhdev.handlers.utils.clusters.GuildCApi;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Map;

public class ServerJoinListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent evt) {
        if(ClustersAPI.getLocalCluster().getId() == 1) {
            Backend.getInstance().execute(Table.tableslist.get("sguild").insert(), evt.getGuild().getId(), GuildCApi.getRandomCluster(), null);
        }
    }


}
