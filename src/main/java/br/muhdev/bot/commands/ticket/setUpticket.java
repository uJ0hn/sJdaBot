package br.muhdev.bot.commands.ticket;

import br.muhdev.bot.Main;
import br.muhdev.bot.commands.SubCommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class setUpticket extends SubCommand {



    private static String title;
    private static String desc ;
    private static String channel ;

    private static String category;

    private static String role;

    Map<User, Integer> userIntegerMap = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent evt) {
        if(!evt.isFromGuild()) return;
        if(userIntegerMap.get(evt.getAuthor()) != null) {
            if(evt.getMessage().getContentRaw().contains("cancel")) {
                userIntegerMap.remove(evt.getAuthor());
                evt.getChannel().sendMessage("Cancelado.").queue();
                return;
            }
            int where = userIntegerMap.get(evt.getAuthor());
            if(where == 1) {
                title = evt.getMessage().getContentRaw();
                userIntegerMap.remove(evt.getAuthor());
                userIntegerMap.put(evt.getAuthor(), 2);
                evt.getChannel().sendMessage("Qual vai ser a descrição da embed do Ticket?").queue();
            } else if(where == 2) {
                desc = evt.getMessage().getContentRaw();
                userIntegerMap.remove(evt.getAuthor());
                userIntegerMap.put(evt.getAuthor(), 3);
                evt.getChannel().sendMessage("Qual vai ser o canal da embed do Ticket?").queue();
            } else if(where == 3) {
                if(evt.getGuild().getChannelById(TextChannel.class, evt.getMessage().getContentRaw()) == null) {
                    evt.getChannel().sendMessage("Canal invalido.").queue();
                    evt.getChannel().sendMessage("Qual vai ser o canal da embed do Ticket?").queue();
                    return;
                }
                channel = evt.getMessage().getContentRaw();
                userIntegerMap.remove(evt.getAuthor());
                userIntegerMap.put(evt.getAuthor(), 4);
                evt.getChannel().sendMessage("Qual vai ser a categoria dos Tickets?").queue();
            } else if (where == 4) {
                if(evt.getGuild().getCategoryById(evt.getMessage().getContentRaw()) == null) {
                    evt.getChannel().sendMessage("Categoria invalida.").queue();
                    evt.getChannel().sendMessage("Qual vai ser a categoria dos Tickets?").queue();
                    return;
                }
                category = evt.getMessage().getContentRaw();
                userIntegerMap.remove(evt.getAuthor());
                userIntegerMap.put(evt.getAuthor(), 5);
                evt.getChannel().sendMessage("Qual vai ser a role da staff dos Tickets?").queue();
            } else if (where == 5) {
                if(evt.getGuild().getRoleById(evt.getMessage().getContentRaw()) == null) {
                    evt.getChannel().sendMessage("Categoria invalida.").queue();
                    evt.getChannel().sendMessage("Qual vai ser a role da staff dos Tickets?").queue();
                    return;
                }
                role = evt.getMessage().getContentRaw();
                userIntegerMap.remove(evt.getAuthor());
                TicketSystem tc = TicketSystem.getGuild(Objects.requireNonNull(evt.getGuild()).getId());
                tc.setup(evt, title, desc, channel, category, evt.getGuild().getRoleById(role));
                evt.getChannel().sendMessage("Ticket configurado com sucesso!").queue();
            }
        }
    }

    public setUpticket() {
        super("setup", "Setar a configuração do ticket para guild", TicketCommand.class);
        Main.getInstance().getJda().addEventListener(this);
    }

    @Override
    public void perform(SlashCommandInteractionEvent evt, OptionMapping args) {
        userIntegerMap.put(evt.getUser(), 1);
        evt.reply("Qual vai ser o titulo da embed do Ticket? | Para cancelar, digite cancel").queue();
    }

}
