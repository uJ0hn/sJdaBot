package br.muhdev.bot;

import br.muhdev.handlers.bothandler.Handler;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends Handler {
    static {handler = new Main();}


    @Override
    public void onEnable() {
        saveDefaultConfig();
        init(getConfig().getString("discord.token"));
        System.out.println("Iniciando...");
        sets();
    }


    public void sets() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String aaaa1 = Main.getInstance().getConfig().getStringList("status").get((int)
                        (Math.random() * Main.getInstance().getConfig().getStringList("status").size()));
                String[] args = aaaa1.split(" ; ");
                setStatus(OnlineStatus.valueOf(args[1]), get(Activity.ActivityType.valueOf(args[2]), args[0]));
            }
        }, 0, 6000L);
    }


    @Override
    public void onDisable() {
        getJda().shutdown();
        System.out.println("Desligando...");
    }


    private static Activity get(Activity.ActivityType activityType, String get) {
        Activity activity;
        if(activityType == Activity.ActivityType.LISTENING) activity = Activity.listening(get);
        else activity = Activity.playing(get);
        return activity;
    }

}
