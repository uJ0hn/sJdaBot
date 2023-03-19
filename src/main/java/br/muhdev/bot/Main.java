package br.muhdev.bot;

import br.muhdev.handlers.bothandler.Handler;

public class Main extends Handler {
    static {handler = new Main();}

    public Main() {
        super(getInstance().getConfig().getString("discord.token"));
    }

    @Override
    public void onEnable() {
        init();
        System.out.println("Iniciando...");
    }

    @Override
    public void onDisable() {
        System.out.println("Desligando...");
    }


}
