package br.muhdev.bot.commands.geral;

import br.muhdev.bot.commands.SlashHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;
import java.util.Objects;

public class ClearCommand extends SlashHandler {
    public ClearCommand() {
        super("clear", "Limpar mensagens do chat");
    }

    @Override
    public void execute(SlashCommandInteractionEvent evt) {
        int value = Objects.requireNonNull(evt.getOption("value")).getAsInt();
        if(value > 99) {
            evt.reply("A quantidade não pode ser maior que 99 mensagens.").queue();
            return;
        }
        List<Message> messages = evt.getChannel().getHistory().retrievePast(value + 1).complete();
        for(Message mssg : messages) {
            mssg.delete().queue();
        }
        evt.reply("Foram apagadas " + value + " mensagens, talvez algumas não foram apagadas por causa de seu tempo.")
                .queue();
    }

    @Override
    public CommandData commandData(String name, String desc) {
        return Commands.slash(name, desc)
                .addOption(OptionType.INTEGER, "value", "Quantas mensagens você quer apagar", true);
    }
}
