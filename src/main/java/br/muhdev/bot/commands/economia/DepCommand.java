package br.muhdev.bot.commands.economia;

import br.muhdev.bot.commands.SubCommand;
import br.muhdev.bot.handlers.utils.economia.EconomiaAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class DepCommand extends SubCommand {
    public DepCommand() {
        super("dep", "Depositar uma quantia no banco", EconomiaCommand.class);
    }

    @Override
    public void perform(SlashCommandInteractionEvent evt, OptionMapping args) {
        if(args == null || !isValid(args.getAsString())) {
            evt.reply("Uso correto: /economia subcommand:dep value:<quantidade> ").queue();
            return;
        }
        int quant = args.getAsInt();
        EconomiaAPI economiaAPI = EconomiaAPI.getEconomia(evt.getUser().getId());
        EmbedBuilder eb = new EmbedBuilder();
        long coins = economiaAPI.getMoney().getQuantie(EconomiaAPI.type.MONEY) -quant;
        long bank = economiaAPI.getMoney().getQuantie(EconomiaAPI.type.BANK) + quant;
        economiaAPI.getMoney().setQuantie(coins, EconomiaAPI.type.MONEY, "0");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                economiaAPI.getMoney().setQuantie(bank, EconomiaAPI.type.BANK, "0");
            }
        }, 3L);
        eb.setTitle("\uD83D\uDC77 Money");
        eb.setDescription("> O: <@" + evt.getUser().getId() + "> depositou " + quant + " no banco.");
        eb.addField("\ud83d\udcb8 Coins", coins + "", true);
        eb.addField("\uD83C\uDFDB Banco", bank + "", true);
        eb.addField("\uD83E\uDD11 Total", "" + (coins + bank), true);
        eb.setFooter(evt.getUser().getName(), evt.getUser().getAvatarUrl());
        eb.setColor(Color.decode("#9c2c2f"));
        evt.replyEmbeds(eb.build()).queue();

    }

    public boolean isValid(String valid) {
        try {
            Integer.parseInt(valid);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
