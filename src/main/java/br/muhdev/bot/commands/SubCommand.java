package br.muhdev.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public abstract class SubCommand {
  
  private final String name;
  private final String usage;
  private final Class<?> cmd;
  public static final Map<String, Map.Entry<Class<?>, SubCommand>> commands = new HashMap<>();
  
  public SubCommand(String name, String usage, Class<?> cmd) {
    this.name = name;
    this.usage = usage;
    this.cmd = cmd;
    if(!commands.containsKey(name)) {
      commands.put(this.name, new AbstractMap.SimpleEntry<>(this.cmd, this));
    }

  }

  public static void put(SubCommand cmd) {

  }


  public abstract void perform(SlashCommandInteractionEvent evt, OptionMapping args);

  public String getName() {
    return this.name;
  }

  
  public String getUsage() {
    return this.usage;
  }

}
