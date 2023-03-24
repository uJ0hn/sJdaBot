package br.muhdev.bot.commands.economia;

import br.muhdev.bot.commands.SubCommand;
import br.muhdev.bot.handlers.utils.Cooldown;
import br.muhdev.bot.handlers.utils.economia.EconomiaAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;

public class WorkCommand extends SubCommand {

    public WorkCommand() {
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
