package br.muhdev.handlers.bothandler;


import br.muhdev.handlers.utils.ConfigManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Handler {


    private static JDA jda;



    public static Handler handler;


    public abstract void onEnable();
    public abstract void onDisable();

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

        }));
    }

    public void init(String token) {
        jda = JDABuilder.createDefault(token).build();
    }

    public void setStatus(Map<Integer, Map.Entry<String, Map.Entry<OnlineStatus, Activity>>> status, long time) {
        final int[] i = {1};
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getJda().getPresence().setPresence(status.get(i[0]).getValue().getKey(), status.get(i[0]).getValue().getValue());
                i[0]++;
            }
        }, 1L, time);

    }

    public JDA getJda() {
        return jda;
    }

    public static Handler getInstance() {
        return handler;
    }

    public ConfigManager getConfig(String config) {
        return new ConfigManager(config);
    }

    public ConfigManager getConfig() {
        return new ConfigManager("config.yml");
    }

    public void saveDefaultConfig(String... path) {
        ConfigManager.saveDefaultConfig(path);
    }


}
