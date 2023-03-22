package br.muhdev.bot.commands.cluster;

import br.muhdev.bot.commands.SubCommand;
import br.muhdev.handlers.utils.clusters.ClustersAPI;
import br.muhdev.handlers.utils.clusters.GuildCApi;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Objects;

public class InfoCommand extends SubCommand {

    public InfoCommand() {
        super("info", "info", ClusterCommand.class);
    }


    @Override
    public void perform(SlashCommandInteractionEvent evt, OptionMapping args) {
        if(!new GuildCApi(Objects.requireNonNull(evt.getGuild()).getId()).isTheCluster()) return;
        ClustersAPI clustersAPI = ClustersAPI.getLocalCluster();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Informações do Cluster")
                .addField("Id do Cluster", clustersAPI.getId() + "", false)
                .addField("Ping da conexão com o Discord", clustersAPI.getPing() + "m/s", false)
                .setFooter(evt.getUser().getAsTag(), evt.getUser().getAvatarUrl());

        evt.replyEmbeds(embedBuilder.build()).queue();
    }


}
