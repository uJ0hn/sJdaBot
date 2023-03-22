package br.muhdev.bot;

import br.muhdev.backend.Backend;
import br.muhdev.bot.commands.SlashHandler;
import br.muhdev.bot.listeners.ServerJoinListener;
import br.muhdev.handlers.bothandler.Handler;
import br.muhdev.handlers.utils.ConfigManager;
import br.muhdev.handlers.utils.clusters.ClustersAPI;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Handler {
    static {handler = new Main();}


    @SneakyThrows
    @Override
    public void onEnable() {
        ConfigManager.saveDefaultConfig("config.yml");
        Backend.makeBackend();
        ClustersAPI.getLocalCluster().insertCluster();
        init(getConfig().getString("discord.token"));
        System.out.println("Iniciando...");
        SlashHandler.setUpCommands();
        getJda().addEventListener(new ServerJoinListener());
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
        new ClustersAPI(1).deleteCluster();
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
