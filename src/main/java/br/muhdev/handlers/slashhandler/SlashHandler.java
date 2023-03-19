package br.muhdev.handlers.slashhandler;

import br.muhdev.handlers.bothandler.Handler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class SlashHandler extends ListenerAdapter {

    private final String name;
    private final String desc;

    public static List<SlashHandler> cmd = new ArrayList<>();

    public SlashHandler(String command, String desc) {
        this.name = command;
        this.desc = desc;
        Handler.getInstance().getJda().addEventListener(this);
        cmd.add(this);
    }


    public abstract void execute(SlashCommandInteractionEvent evt);



    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent evt) {
        if(evt.getName().equalsIgnoreCase(name)) this.execute(evt);
    }


}
