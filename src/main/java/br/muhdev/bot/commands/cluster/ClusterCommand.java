package br.muhdev.bot.commands.cluster;

import br.muhdev.bot.commands.SlashHandler;
import br.muhdev.bot.commands.SubCommand;
import br.muhdev.bot.handlers.utils.clusters.GuildCApi;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClusterCommand extends SlashHandler {

    public ClusterCommand() {
        super("cluster", "Comando para gerencia Clusters");

        new InfoCommand();
        new TurnOffCommand();
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
                    evt.getChannel().sendMessage(b.getKey() + " " + b.getValue().getValue().getUsage()).queue();
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
                .addOption(OptionType.STRING, "subcommand", "Qual subcomando deseja executar", true, true)
                .addOption(OptionType.STRING, "value", "Valor adicional");
    }



    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent evt) {
        List<String> wordss = new ArrayList<>();
        for(Map.Entry<String, Map.Entry<Class<?>, SubCommand>> b : SubCommand.commands.entrySet()) {
            if(b.getValue().getKey() == this.getClass()) {
                wordss.add(b.getKey());
            }
        }
        String[] words = wordss.toArray(new String[0]);
        if(evt.getName().equalsIgnoreCase(this.name) && evt.getFocusedOption().getName().equals("subcommand")) {
            List<Command.Choice> options = Stream.of(words)
                    .map(word -> new Command.Choice(word, word)) // map the words to choices
                    .collect(Collectors.toList());
            evt.replyChoices(options).queue();
        }
    }
}
