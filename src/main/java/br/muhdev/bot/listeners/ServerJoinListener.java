package br.muhdev.bot.listeners;


import br.muhdev.backend.Backend;
import br.muhdev.backend.tables.Table;
import br.muhdev.bot.commands.SlashHandler;
import br.muhdev.handlers.utils.clusters.ClustersAPI;
import br.muhdev.handlers.utils.clusters.GuildCApi;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import javax.sql.rowset.CachedRowSet;
import java.util.Objects;

public class ServerJoinListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent evt) {
        if(ClustersAPI.getLocalCluster().getId() == 1) {
            Backend.getInstance().execute(Table.tableslist.get("sguild").insert(), evt.getGuild().getId(), GuildCApi.getRandomCluster(), null, "{}");
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent evt) {
        if(evt.getMessage().getContentRaw().contains("atualizar")) {
            evt.getGuild().updateCommands().queue();
            evt.getJDA().updateCommands().queue();
            evt.getChannel().sendMessage("Atualizado").queue();
        }
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void onGuildReady(@NotNull GuildReadyEvent evt) {
        if(ClustersAPI.getLocalCluster().getId() == 1) {
            evt.getGuild().updateCommands().addCommands(SlashHandler.cmd).queue();
            for(Guild guild : evt.getJDA().getGuilds()) {
                Table t = Table.tableslist.get("sguild");
                CachedRowSet query = Backend.getInstance().query(t.select() + " WHERE guildid=?", evt.getGuild().getId());
                if(query != null) return;
                int clusterid = (int) (Math.random() * ClustersAPI.getAllClusters().size());
                Backend.getInstance().execute(t.insert(), evt.getGuild().getId(), clusterid, null, "{}");
            }

            for(Guild guild : evt.getJDA().getGuilds()) guild.loadMembers().onSuccess(member -> {
                for(Member user : member) {
                    if(user.getUser().isBot()) return;
                    try {
                        System.out.println(user.getId());
                        Table t = Table.tableslist.get("susers");
                        JSONObject j = new JSONObject();
                        j.put("money", "0");
                        j.put("bank", "0");
                        j.put("cooldown", "0");
                        CachedRowSet query = Backend.getInstance().query(t.select() + " ORDER BY joined DESC;");
                        int id = query == null ? 1 : query.getInt("id") + 1;
                        if(Backend.getInstance().query(t.select() + " WHERE userid=?", user.getId()) == null) Backend.getInstance().execute(t.insert(), id ,user.getId(), j.toJSONString(), System.currentTimeMillis());
                    }catch (Exception ignored) {}
                }
            });




        }
    }

}
