package br.muhdev.bot.commands.cluster;

import br.muhdev.bot.commands.SlashHandler;
import br.muhdev.handlers.utils.clusters.ClustersAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class ClusterInfoCommand extends SlashHandler {
    public ClusterInfoCommand() {
        super("clusterinfo", "Ver as informações do cluster da guild!");
    }

    @Override
    public void execute(SlashCommandInteractionEvent evt) {
        ClustersAPI clustersAPI = ClustersAPI.getLocalCluster();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Informações do Cluster")
                .addField("Id do Cluster", clustersAPI.getId() + "", false)
                .addField("Ping da conexão com o Discord", clustersAPI.getPing() + "m/s", false)
                .setFooter(evt.getUser().getAsTag(), evt.getUser().getAvatarUrl());

        evt.replyEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public CommandData commandData(String name, String desc) {
        return Commands.slash(name, desc);
    }
}
