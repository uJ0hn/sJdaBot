package br.muhdev.bot.commands;

import br.muhdev.bot.Main;
import br.muhdev.handlers.utils.clusters.ClustersAPI;
import br.muhdev.handlers.utils.clusters.GuildCApi;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Objects;

public class PingCommand extends SlashHandler {
    public PingCommand() {
        super("ping", "Ping command");
    }

    @Override
    public void execute(SlashCommandInteractionEvent evt) {
        if(!new GuildCApi(Objects.requireNonNull(evt.getGuild()).getId()).isTheCluster()) return;

        evt.reply("Pong! Latência de " + evt.getJDA().getGatewayPing() + " m/s no Cluster " + ClustersAPI.getLocalCluster().getId()).queue();
    }

    @Override
    public CommandData commandData(String name, String desc) {
        return Commands.slash(name, desc);
    }
}
