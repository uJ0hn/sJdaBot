package br.muhdev.bot.commands.cluster;

import br.muhdev.bot.commands.SubCommand;
import br.muhdev.handlers.utils.clusters.ClustersAPI;
import br.muhdev.handlers.utils.clusters.GuildCApi;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Objects;
import java.util.regex.Pattern;

public class TurnOffCommand extends SubCommand {
    public TurnOffCommand() {
        super("turnoff", "Comando para desligar um cluster", ClusterCommand.class);
    }
    private static final Pattern p = Pattern.compile("[0-9]+");
    @Override
    public void perform(SlashCommandInteractionEvent evt, OptionMapping args) {
        if (!new GuildCApi(Objects.requireNonNull(evt.getGuild()).getId()).isTheCluster()) return;
        if (evt.getUser().getId().equalsIgnoreCase("992810758998073404")) {
            if (args == null) {
                evt.reply("Uso correto: /cluster subcommand:turnoff value:<id do cluster> ").setEphemeral(true).queue();
                return;
            } else if (!(p.matcher(args.getAsString()).find())) {
                evt.reply("O value precisa ser um numero valido.").setEphemeral(true).queue();
                return;
            }
            int id = args.getAsInt();
            ClustersAPI clustersAPI = ClustersAPI.getCluster(id);
            if (!clustersAPI.clusterDisponible()) {
                evt.reply("Esse cluster não existe").queue();
                return;
            }

            evt.reply("Foi enviado o sinal de desligamento para o cluster " + id + ".").queue();
            clustersAPI.shutdown();
        }
    }
}
