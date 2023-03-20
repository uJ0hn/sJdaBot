package br.muhdev.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class PingCommand extends SlashHandler {
    public PingCommand() {
        super("ping", "Ping command");
    }

    @Override
    public void execute(SlashCommandInteractionEvent evt) {
            evt.reply("Pong! Latência de " + evt.getJDA().getGatewayPing() + " m/s").queue();
    }

    @Override
    public CommandData commandData(String name, String desc) {
        return Commands.slash(name, desc);
    }
}
