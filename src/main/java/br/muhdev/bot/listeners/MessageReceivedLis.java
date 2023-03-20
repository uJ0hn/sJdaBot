package br.muhdev.bot.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceivedLis extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent evt) {
        if(evt.getMessage().getContentRaw().contains("/createcommand")) {
            evt.getJDA().upsertCommand("ping", "Ping command!").queue();
            evt.getJDA().updateCommands().queue();
        }
    }
}
