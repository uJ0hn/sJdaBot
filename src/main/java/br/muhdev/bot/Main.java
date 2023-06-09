package br.muhdev.bot;

import br.muhdev.bot.backend.Backend;
import br.muhdev.bot.commands.SlashHandler;
import br.muhdev.bot.listeners.ServerJoinListener;
import br.muhdev.bot.handlers.bothandler.Handler;
import br.muhdev.bot.handlers.utils.ConfigManager;
import br.muhdev.bot.handlers.utils.clusters.ClustersAPI;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends Handler {
    static {handler = new Main();}


    @SneakyThrows
    @Override
    public void onEnable() {
        ConfigManager.saveDefaultConfig("config.yml");
        Backend.makeBackend();

        init(getConfig().getString("discord.token"));
        System.out.println("Iniciando...");
        SlashHandler.setUpCommands();
        getJda().addEventListener(new ServerJoinListener());
        ClustersAPI.getLocalCluster().insertCluster();
        getJda().updateCommands().queue();
        if(ClustersAPI.getLocalCluster().getId() == 1) {
            sets();
        }

        new Thread(() -> ClustersAPI.getLocalCluster().start()).start();
    }


    public void sets() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String aaaa1 = Main.getInstance().getConfig().getStringList("status").get((int)
                        (Math.random() * Main.getInstance().getConfig().getStringList("status").size()));
                String[] args = aaaa1.split(" ; ");
                String finale = args[0];
                for(ClustersAPI api : ClustersAPI.getAllClusters()) {
                    finale = finale.replace("{cluster_" + api.getId() + "}", "" + api.getPing());
                }
                setStatus(OnlineStatus.valueOf(args[1]), get(Activity.ActivityType.valueOf(args[2]), finale));
            }
        }, 0, 6000L);
    }


    @Override
    public void onDisable() {
        ClustersAPI.getLocalCluster().deleteCluster();
        getJda().shutdown();
        Backend.getInstance().closeConnection();
        System.out.println("Desligando...");
    }


    private static Activity get(Activity.ActivityType activityType, String get) {
        Activity activity;
        if(activityType == Activity.ActivityType.LISTENING) activity = Activity.listening(get);
        else activity = Activity.playing(get);
        return activity;
    }

}
