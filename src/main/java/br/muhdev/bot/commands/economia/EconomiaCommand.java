package br.muhdev.bot.commands.economia;

import br.muhdev.bot.commands.SlashHandler;
import br.muhdev.bot.commands.SubCommand;
import br.muhdev.handlers.utils.Cooldown;
import br.muhdev.handlers.utils.clusters.GuildCApi;
import br.muhdev.handlers.utils.economia.EconomiaAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.*;
import java.util.Map;
import java.util.Objects;

public class EconomiaCommand extends SlashHandler {
    public EconomiaCommand() {
        super("economia", "Comando de gerenciamento de economias");

        new getMoney();
        new Work();
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
                    evt.reply(b.getKey() + " " + b.getValue().getValue().getUsage()).complete();
                }
            }
            return;
        }

        OptionMapping value = evt.getOption("value");
        a.getValue().perform(evt, value);
    }

    @Override
    public CommandData commandData(String name, String desc) {
        return Commands.slash(name, desc).addOption(OptionType.STRING, "subcommand", "Qual subcomando deseja executar", true)
                .addOption(OptionType.STRING, "value", "Valor adicional");
    }
}
class getMoney extends SubCommand{


    public getMoney() {
        super("getmoney", "Ver a quantidade de money no seu perfil", EconomiaCommand.class);
    }

    @Override
    public void perform(SlashCommandInteractionEvent evt, OptionMapping args) {
        EconomiaAPI economiaAPI = EconomiaAPI.getEconomia(evt.getUser().getId());
        EmbedBuilder eb = new EmbedBuilder();
        long coins = economiaAPI.getMoney().getQuantie(EconomiaAPI.type.MONEY);
        long bank = economiaAPI.getMoney().getQuantie(EconomiaAPI.type.BANK);
        eb.setTitle("\uD83D\uDC77 Money");
        eb.setDescription("> Estou listando abaixo o saldo da conta: <@" + evt.getUser().getId() + ">.");
        eb.addField("\ud83d\udcb8 Coins", coins + "", true);
        eb.addField("\uD83C\uDFDB Banco", bank + "", true);
        eb.addField("\uD83E\uDD11 Total", "" + (coins + bank), true);
        eb.setFooter(evt.getUser().getName(), evt.getUser().getAvatarUrl());
        eb.setColor(Color.decode("#9c2c2f"));
        evt.replyEmbeds(eb.build()).queue();

    }

}
class Work extends SubCommand{

    public Work() {
        super("work", "Trabalhar para ganhar dinheiro", EconomiaCommand.class);
    }

    @Override
    public void perform(SlashCommandInteractionEvent evt, OptionMapping args) {
        Cooldown.economia cooldown = Cooldown.economia.getCooldown(evt.getUser().getId());
        if(cooldown.isInCooldown()) {
            evt.reply("Faltam " + cooldown.getTimeLeftAll() + " para que você possa usar este comando novamente.").queue();
            return;
        }
        EconomiaAPI economiaAPI = EconomiaAPI.getEconomia(evt.getUser().getId());
        EmbedBuilder eb = new EmbedBuilder();
        int quantie = (int) (Math.random() * 9999);
        long coins = economiaAPI.getMoney().getQuantie(EconomiaAPI.type.MONEY) + quantie;
        economiaAPI.getMoney().addQuantie(quantie, EconomiaAPI.type.MONEY, "");
        long bank = economiaAPI.getMoney().getQuantie(EconomiaAPI.type.BANK);
        eb.setTitle("\uD83D\uDC77 Trabalho");
        eb.setDescription("> O usuario: <@" + evt.getUser().getId() + "> trabalhou e ganhou " + quantie + " de coins!");
        eb.addField("\ud83d\udcb8 Coins", coins + "", true);
        eb.addField("\uD83C\uDFDB Banco", bank + "", true);
        eb.addField("\uD83E\uDD11 Total", "" + (coins + bank), true);
        eb.setFooter(evt.getUser().getName(), evt.getUser().getAvatarUrl());
        eb.setColor(Color.decode("#9c2c2f"));
        evt.replyEmbeds(eb.build()).queue();
        cooldown.start();
    }


}