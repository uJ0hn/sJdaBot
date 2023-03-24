package br.muhdev.bot.handlers.bothandler;


import br.muhdev.bot.handlers.utils.ConfigManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public abstract class Handler {


    private static JDA jda;



    public static Handler handler;


    public abstract void onEnable();
    public abstract void onDisable();

    public static void main(String[] args) {
        handler.onEnable();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        handler.onDisable();
        }));
    }

    public void init(String token) {
        jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS).build();
    }

    public void setStatus(OnlineStatus status,  Activity activity) {
        getJda().getPresence().setPresence(status, activity);
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
