package br.muhdev.bot.commands.ticket.buttons;

import br.muhdev.bot.backend.Backend;
import br.muhdev.bot.backend.tables.Table;
import br.muhdev.bot.commands.ticket.TicketSystem;
import br.muhdev.bot.handlers.buttomhandler.Buttons;
import br.muhdev.bot.handlers.utils.Cooldown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class CloseTicket extends Buttons {
    public CloseTicket() {
        super("close-ticket");
    }
    private static final Table table = Table.tableslist.get("sguild");

    @Override
    @SuppressWarnings({"unchecked", "redundant"})
    public void buttonexecute(ButtonInteractionEvent evt) {
        Cooldown cooldown = new Cooldown(evt.getUser().getId(), "close-ticket", 10);
        cooldown.start();
        evt.reply("O ticket será fechado em 10 segundos...").setEphemeral(true).submit();
        while (cooldown.getTimeLeft() >= 0) {
            if(cooldown.getTimeLeft() == 0) {
                TicketSystem tc = TicketSystem.getGuild(Objects.requireNonNull(evt.getGuild()).getId());
                TextChannel channel = evt.getChannel().asTextChannel();
                Collection<Permission> permissions = new ArrayList<>();
                permissions.add(Permission.VIEW_CHANNEL);
                channel.getManager().putMemberPermissionOverride(evt.getUser().getIdLong(), null, permissions).queue();

                EmbedBuilder eb = new EmbedBuilder()
                        .setTitle("Atendimento")
                        .setDescription("Atendimento fechado com sucesso!");
                evt.getChannel().sendMessageEmbeds(eb.build()).setActionRow(
                        Button.success("re-open", "\uD83D\uDD13 Reabrir"),
                        Button.danger("tick-del", "⛔ Deletar o ticket")
                ).submit();

                JSONObject jsonObject = tc.jsonObjectTickets();
                jsonObject.remove(evt.getUser().getId());
                jsonObject.put(evt.getUser().getId(), evt.getChannel().getId() + " " + false);
                Backend.getInstance().execute(table.update("tickets=?") + " WHERE guildid=?", jsonObject.toJSONString(),
                        evt.getGuild().getId());
                cooldown.stop();
                break;
            }
        }
    }

}
