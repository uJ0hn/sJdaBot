package br.muhdev.bot.listeners;


import br.muhdev.backend.Backend;
import br.muhdev.backend.tables.Table;
import br.muhdev.bot.commands.SlashHandler;
import br.muhdev.handlers.utils.clusters.ClustersAPI;
import br.muhdev.handlers.utils.clusters.GuildCApi;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ServerJoinListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent evt) {
        if(ClustersAPI.getLocalCluster().getId() == 1) {
            Backend.getInstance().execute(Table.tableslist.get("sguild").insert(), evt.getGuild().getId(), GuildCApi.getRandomCluster(), null);
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
    public void onGuildReady(@NotNull GuildReadyEvent evt) {
        if(ClustersAPI.getLocalCluster().getId() == 1) {
            evt.getGuild().updateCommands().addCommands(SlashHandler.cmd).queue();
        }
    }

}
