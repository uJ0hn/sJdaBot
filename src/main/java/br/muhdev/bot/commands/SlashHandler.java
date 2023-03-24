package br.muhdev.bot.commands;

import br.muhdev.bot.commands.cluster.ClusterCommand;
import br.muhdev.bot.commands.economia.EconomiaCommand;
import br.muhdev.bot.commands.geral.PingCommand;
import br.muhdev.bot.commands.ticket.TicketCommand;
import br.muhdev.handlers.bothandler.Handler;
import br.muhdev.handlers.utils.ConfigManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class SlashHandler extends ListenerAdapter {

    private final String defaultname;
    private final String name;
    private final String desc;

    public static List<CommandData> cmd = new ArrayList<>();

    public SlashHandler(String name, String desc) {
        this.defaultname = name;
        ConfigManager.writeCommand(name , desc);
        this.name = getCommand().getString("command");
        this.desc = getCommand().getString("description");
        if(getCommand().getBoolean("enabled")) {
            Handler.getInstance().getJda().addEventListener(this);
            cmd.add(commandData(name, desc));
        }

    }

    public ConfigManager getCommand() {
        return new ConfigManager("commands/" + this.defaultname + ".yml");
    }



    public static void setUpCommands() {
        new ClusterCommand();
        new PingCommand();
        new EconomiaCommand();
        new TicketCommand();
    }

    public abstract void execute(SlashCommandInteractionEvent evt);

    public abstract CommandData commandData(String name, String desc);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent evt) {
        if(evt.getName().equalsIgnoreCase(name)) this.execute(evt);
    }


}
