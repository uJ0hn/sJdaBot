package br.muhdev.bot.commands.cluster;

import br.muhdev.bot.commands.SlashHandler;
import br.muhdev.bot.commands.SubCommand;
import br.muhdev.handlers.utils.clusters.GuildCApi;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

public class ClusterCommand extends SlashHandler {

    public ClusterCommand() {
        super("cluster", "Comando para gerencia Clusters");

        new InfoCommand();
    }

    @Override
    public void execute(SlashCommandInteractionEvent evt) {
        OptionMapping option = evt.getOption("subcommand");
        assert option != null;
        Map.Entry<Class<?>, SubCommand> a = SubCommand.commands.get(option.getAsString());
        if(a == null || a.getKey() != this.getClass()) {
            if(!new GuildCApi(Objects.requireNonNull(evt.getGuild()).getId()).isTheCluster()) return;
            evt.reply("Esse subcomando não existe, subcomandos disponiveis:").queue();
            for(Map.Entry<String, Map.Entry<Class<?>, SubCommand>> b : SubCommand.commands.entrySet()) {
                if(b.getValue().getKey() == this.getClass()) {
                    evt.reply(b.getKey()).queue();
                }
            }
            return;
        }

        OptionMapping value = evt.getOption("value");
        a.getValue().perform(evt, value);
    }

    @Override
    public CommandData commandData(String name, String desc) {
        return Commands.slash(name, desc)
                .addOption(OptionType.STRING, "subcommand", "Qual subcomando deseja executar", true)
                .addOption(OptionType.STRING, "value", "Valor adicional");
    }
}
