package br.muhdev.bot.commands.ticket;

import br.muhdev.backend.Backend;
import br.muhdev.backend.tables.Table;
import br.muhdev.handlers.buttomhandler.Buttons;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class ReOpenTicket extends Buttons {
    public ReOpenTicket() {
        super("re-open");
    }

    private static final Table table = Table.tableslist.get("sguild");
    @Override
    @SuppressWarnings({"unchecked", "redundant"})
    public void buttonexecute(ButtonInteractionEvent evt) {
        Collection<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.VIEW_CHANNEL);
        permissions.add(Permission.MESSAGE_SEND);
        TicketSystem tc = TicketSystem.getGuild(Objects.requireNonNull(evt.getGuild()).getId());
        JSONObject jsonObject = tc.jsonObjectTickets();
        for(Object a : jsonObject.entrySet()) {
            Map.Entry<String, String> test = (Map.Entry<String, String>) a;
            if(test.getValue().equalsIgnoreCase(evt.getChannel().getId() + " " + false)) {
                evt.getChannel().asTextChannel().getManager().putMemberPermissionOverride(
                        Long.parseLong(test.getKey()),permissions, null ).submit();
                evt.getChannel().deleteMessageById(evt.getChannel().asTextChannel().getLatestMessageId()).submit();
                evt.reply("Ticket Reaberto!").submit();
                JSONObject js = tc.jsonObjectTickets();
                js.remove(test.getKey());
                js.put(test.getKey(), evt.getChannel().getId() + " " + true);
                Backend.getInstance().execute(table.update("tickets=?") + " WHERE guildid=?", jsonObject.toJSONString(),
                        evt.getGuild().getId());
            }
        }


    }
}
