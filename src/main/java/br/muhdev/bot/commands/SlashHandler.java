package br.muhdev.bot.commands;

import br.muhdev.handlers.bothandler.Handler;
import br.muhdev.handlers.utils.ConfigManager;
import br.muhdev.handlers.utils.clusters.ClustersAPI;
import br.muhdev.handlers.utils.clusters.GuildCApi;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class SlashHandler extends ListenerAdapter {

    private final String defaultname;
    private final String name;

    public static List<SlashHandler> cmd = new ArrayList<>();

    public SlashHandler(String name, String desc) {
        this.defaultname = name;
        ConfigManager.writeCommand(name , desc);
        this.name = getCommand().getString("command");
        String desc1 = getCommand().getString("description");
        if(getCommand().getBoolean("enabled")) {
            Handler.getInstance().getJda().addEventListener(this);
            if(ClustersAPI.getLocalCluster().getId() == 1) {
                Handler.getInstance().getJda().upsertCommand(commandData(this.name, desc1)).queue();
            }
            Handler.getInstance().getJda().updateCommands().queue();
            cmd.add(this);
        }

    }

    public ConfigManager getCommand() {
        return new ConfigManager("commands/" + this.defaultname + ".yml");
    }


    public static void setUpCommands() {
        new PingCommand();
    }

    public abstract void execute(SlashCommandInteractionEvent evt);

    public abstract CommandData commandData(String name, String desc);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent evt) {
        if(evt.getName().equalsIgnoreCase(name) && new GuildCApi(Objects.requireNonNull(evt.getGuild()).getId())
                .isTheCluster()) this.execute(evt);
    }


}
