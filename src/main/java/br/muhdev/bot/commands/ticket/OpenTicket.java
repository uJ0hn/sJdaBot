package br.muhdev.bot.commands.ticket;

import br.muhdev.backend.Backend;
import br.muhdev.backend.tables.Table;
import br.muhdev.handlers.buttomhandler.Buttons;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static br.muhdev.bot.commands.ticket.TicketSystem.getGuild;

public class OpenTicket extends Buttons {

    private static final Table table = Table.tableslist.get("sguild");

    public OpenTicket() {
        super("ticket");
    }

    @Override
    @SuppressWarnings({"unchecked", "redundant"})
    public void buttonexecute(ButtonInteractionEvent evt) {
        TicketSystem tc = getGuild(Objects.requireNonNull(evt.getGuild()).getId());
        if(!tc.isConfigured()) return;
        JSONObject js = tc.jsonObjectTickets();
        if(js.get(evt.getUser().getId()) != null) {
            evt.reply("Você ja possui um ticket aberto.").submit();
            return;
        }
        Role role = evt.getGuild().getRoleById((String) tc.getValueonJson("staff"));
        Category c = evt.getGuild().getCategoryById((String) tc.getValueonJson("category"));

        Collection<Permission> collection = new ArrayList<>();
        collection.add(Permission.VIEW_CHANNEL);
        collection.add(Permission.MESSAGE_SEND);

        assert role != null;
        ChannelAction<TextChannel> action = Objects.requireNonNull(evt.getGuild()).createTextChannel("ticket-" + evt.getUser().getAsTag() , c)
                .addMemberPermissionOverride(evt.getUser().getIdLong(), collection, null)
                .addRolePermissionOverride(evt.getGuild().getRolesByName("@everyone", true).get(0).getIdLong(), null, collection)
                .addRolePermissionOverride(role.getIdLong(), collection, null);
        TextChannel textChannel = action.complete();
        String channelid = textChannel.getId();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(evt.getGuild().getName() + " - Sistema de ticket");
        eb.setDescription("O suporte estará aqui em breve!\nFeche seu ticket clicando no botão abaixo" +
                "\n \nTicket criado por:  <@" + evt.getUser().getId() + ">");

        TextChannel a = evt.getGuild().getChannelById(TextChannel.class, channelid);
        assert a != null;
        a.sendMessage("<@" + evt.getUser().getId() + ">").queue();
        a.sendMessageEmbeds(eb.build())
                .addActionRow(Button.danger("close-ticket", "\uD83D\uDD12 Fechar")).queue();


        js.put(evt.getUser().getId(), channelid + " " + true);
        evt.reply("O ticket <#" + channelid + "> foi aberto com sucesso!").setEphemeral(true).queue();
        Backend.getInstance().execute(table.update("tickets=?") + " WHERE guildid=?", js.toJSONString(),
                evt.getGuild().getId());


    }

}
