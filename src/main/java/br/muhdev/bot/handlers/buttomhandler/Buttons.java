package br.muhdev.bot.handlers.buttomhandler;

import br.muhdev.bot.handlers.bothandler.Handler;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public abstract class Buttons extends ListenerAdapter {
    public final String nome;

    public Buttons(String buttonlabel) {
        Handler.getInstance().getJda().addEventListener(this);
        this.nome = buttonlabel;
    }

    public abstract void buttonexecute(ButtonInteractionEvent evt);

    public void onButtonInteraction(final ButtonInteractionEvent event) {
        if(event.getComponentId().equals(nome)) {
            this.buttonexecute(event);
        }
    }

}
