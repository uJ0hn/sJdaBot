package br.muhdev.bot;

import br.muhdev.handlers.bothandler.Handler;

public class Main extends Handler {
    static {handler = new Main();}


    @Override
    public void onEnable() {
        saveDefaultConfig();
        init(getConfig().getString("discord.token"));
        System.out.println("Iniciando...");
    }

    @Override
    public void onDisable() {
        System.out.println("Desligando...");
    }


}
