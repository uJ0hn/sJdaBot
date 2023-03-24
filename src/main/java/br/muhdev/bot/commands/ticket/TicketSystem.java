package br.muhdev.bot.commands.ticket;

import br.muhdev.backend.Backend;
import br.muhdev.backend.tables.Table;
import br.muhdev.handlers.buttomhandler.Buttons;
import br.muhdev.handlers.utils.economia.EconomiaAPI;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.json.simple.JSONObject;

import javax.sql.rowset.CachedRowSet;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class TicketSystem {

    private final String guildid;

    private static final Table table = Table.tableslist.get("sguild");


    public TicketSystem(String guildid) {
        this.guildid = guildid;
    }

    public static TicketSystem getGuild(String guild) {
        return new TicketSystem(guild);
    }

    @SuppressWarnings("unchecked")
    public void setup(MessageReceivedEvent evt, String title, String text, String channelid, String categoryid, Role staff) {
        TextChannel channel = Objects.requireNonNull(evt.getGuild()).getChannelById(TextChannel.class,
                channelid);
        Category category = evt.getGuild().getCategoryById(categoryid) ;
        if(staff == null) {
            evt.getChannel().sendMessage("Role invalida.").queue();
            return;
        }
        if(channel == null) {
            evt.getChannel().sendMessage("Canal invalido.").queue();
            return;
        }
        if(category == null) {
            evt.getChannel().sendMessage("Categoria invalida.").queue();
            return;
        }

        JSONObject j = new JSONObject();
        j.put("channel", channelid);
        j.put("category", categoryid);
        j.put("staff", staff.getId());
        j.put("tittle", title);
        j.put("text", text);

        Backend.getInstance().execute(table.update("ticket=?") + " WHERE guildid=?", j.toJSONString(), guildid);
    }

    @SneakyThrows
    public boolean isConfigured() {
        CachedRowSet q = Backend.getInstance().query(table.select() + " WHERE guildid=?", guildid);
        return q.getString("ticket") != null;
    }

    @SneakyThrows
    public void sendTicket(SlashCommandInteractionEvent evt) {
        if(!isConfigured()) {
            evt.reply("Você precisa configurar o ticket primeiro.").setEphemeral(true).queue();
            return;
        }
        TextChannel channel = Objects.requireNonNull(evt.getGuild()).getChannelById(TextChannel.class,
                (String) getValueonJson("channel"));

        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor((String) getValueonJson("tittle"));
        eb.setDescription((CharSequence) getValueonJson("text"));
        eb.setFooter(evt.getGuild().getName(), evt.getGuild().getIconUrl());
        eb.setColor(new Color(47, 49, 54));

        assert channel != null;
        channel.sendMessageEmbeds(eb.build()).addActionRow(Button.success("ticket", "🎫 Abrir Ticket")).queue();
        evt.reply("Enviado com sucesso").setEphemeral(true).queue();
    }

    @SneakyThrows
    public JSONObject jsonObjectTickets() {
        CachedRowSet query = Backend.getInstance().query(table.select() + " WHERE guildid='" + guildid + "'");
        return EconomiaAPI.getAsJsonObject(query.getString("tickets"));
    }

    public Object getValueonJsonTickets(String value) {
        return jsonObject().get(value);
    }

    @SneakyThrows
    private JSONObject jsonObject() {
        CachedRowSet query = Backend.getInstance().query(table.select() + " WHERE guildid='" + guildid + "'");
        return EconomiaAPI.getAsJsonObject(query.getString("ticket"));
    }

    public Object getValueonJson(String value) {
        return jsonObject().get(value);
    }








}
