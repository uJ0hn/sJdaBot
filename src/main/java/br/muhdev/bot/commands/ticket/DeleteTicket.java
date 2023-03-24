package br.muhdev.bot.commands.ticket;

import br.muhdev.backend.Backend;
import br.muhdev.backend.tables.Table;
import br.muhdev.handlers.buttomhandler.Buttons;
import br.muhdev.handlers.utils.Cooldown;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.json.simple.JSONObject;

import java.util.Objects;

public class DeleteTicket extends Buttons {
    public DeleteTicket() {
        super("tick-del");
    }
    private static final Table table = Table.tableslist.get("sguild");
    @Override
    public void buttonexecute(ButtonInteractionEvent evt) {
        TicketSystem tc = TicketSystem.getGuild(Objects.requireNonNull(evt.getGuild()).getId());
        Cooldown cooldown = new Cooldown(evt.getUser().getId(), "tick-del", 10);
        cooldown.start();
        evt.reply("O ticket será fechado em 10 segundos...").setEphemeral(true).submit();
        while (cooldown.getTimeLeft() >= 0) {
            if(cooldown.getTimeLeft() == 0) {
                evt.getChannel().delete().submit();
                JSONObject jsonObject = tc.jsonObjectTickets();
                jsonObject.remove(evt.getUser().getId());
                Backend.getInstance().execute(table.update("tickets=?") + " WHERE guildid=?", jsonObject.toJSONString(),
                        evt.getGuild().getId());
                cooldown.stop();
                break;
            }
        }
    }
}
