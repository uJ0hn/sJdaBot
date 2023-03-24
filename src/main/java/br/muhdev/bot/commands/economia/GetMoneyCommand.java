package br.muhdev.bot.commands.economia;

import br.muhdev.bot.commands.SubCommand;
import br.muhdev.bot.handlers.utils.economia.EconomiaAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;

public class GetMoneyCommand extends SubCommand {


    public GetMoneyCommand() {
        super("money", "Ver a quantidade de money no seu perfil", EconomiaCommand.class);
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
