package br.muhdev.bot.handlers.utils;

import br.muhdev.bot.backend.Backend;
import br.muhdev.bot.backend.tables.Table;
import br.muhdev.bot.handlers.utils.economia.EconomiaAPI;
import br.muhdev.bot.handlers.utils.economia.MoneyInterface;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;


import java.util.HashMap;
import java.util.Map;

public class Cooldown {


    public static class economia {
        private final int timeInSeconds;
        private final String cooldownName;
        private final MoneyInterface money;

        private static final Table table = Table.tableslist.get("susers");

        public economia(String player, int timeInSeconds) {
            this.cooldownName = player;
            this.timeInSeconds = timeInSeconds;
            this.money = EconomiaAPI.getEconomia(player).getMoney();
        }

        public static economia getCooldown(String player) {
            return new economia(player, 1800);
        }

        @SuppressWarnings("unchecked")
        public void stop() {
            JSONObject object = new JSONObject();
            object.put("money", "" +money.getQuantie(EconomiaAPI.type.MONEY));
            object.put("bank", "" + money.getQuantie(EconomiaAPI.type.BANK));
            object.put("cooldown", "0");
            Backend.getInstance().execute(table.update("economy=?") + " WHERE userid=?", object.toJSONString(), cooldownName);
        }

        @SneakyThrows
        public long getTimeLeft() {
                long current = System.currentTimeMillis();
                long time = Long.parseLong((String) money.getValueonJson("cooldown"));
                int r = (int)(current - time) / 1000;
                return (r - 1800) * -1;
        }


        @SneakyThrows
        public String getTimeLeftAll() {
            int timeuntilm = (int) (getTimeLeft() / 60);
            int timeuntilh = timeuntilm / 60;
            return (timeuntilh % 24) + " hora(s), " + (timeuntilm % 60) + " minuto(s) e " + (getTimeLeft() % 60) + " segundo(s)";
        }




        @SneakyThrows
        public boolean isInCooldown() {
            long time = getTimeLeft();
            if(time <= 0) {
                stop();
                return false;
            } else return true;

        }

        @SuppressWarnings("unchecked")
        public void start() {
            JSONObject object = new JSONObject();
            object.put("money", "" +money.getQuantie(EconomiaAPI.type.MONEY));
            object.put("bank", "" + money.getQuantie(EconomiaAPI.type.BANK));
            object.put("cooldown", "" + System.currentTimeMillis());
            Backend.getInstance().execute(table.update("economy=?") + " WHERE userid=?", object.toJSONString(), cooldownName);
        }

    }

    private static final Map<String, Cooldown> cooldowns = new HashMap();
    private final int timeInSeconds;
    private final String id;
    private final String cooldownName;
    private long start;

    public Cooldown(String id, String cooldownName, int timeInSeconds) {
        this.id = id;
        this.cooldownName = cooldownName;
        this.timeInSeconds = timeInSeconds;
    }

    public static boolean isInCooldown(String id, String cooldownName) {
        Cooldown cooldown = getCooldown(id, cooldownName);
        if(cooldown == null) return false;
        if (cooldown.getTimeLeft() >= 1) {
            return true;
        } else {
            cooldown.stop();
            return false;
        }
    }

    public void stop() {
        cooldowns.remove(id + cooldownName);
    }

    private static Cooldown getCooldown(String id, String cooldownName) {
        return cooldowns.get(id + cooldownName);
    }

    public int getTimeLeft() {
        Cooldown cooldown = getCooldown(id, cooldownName);
        int f = -1;
        if (cooldown != null) {
            long now = System.currentTimeMillis();
            long cooldownTime = cooldown.start;
            int r = (int)(now - cooldownTime) / 1000;
            f = (r - cooldown.timeInSeconds) * -1;
        }

        return f;
    }

    public static double getTimeLeftDouble(String id, String cooldownName) {
        Cooldown cooldown = getCooldown(id, cooldownName);
        double f = -1.0D;
        if (cooldown != null) {
            long now = System.currentTimeMillis();
            long cooldownTime = cooldown.start;
            double r = (double)((now - cooldownTime) / 1000L);
            f = (r - (double)cooldown.timeInSeconds) * -1.0D;
        }

        return f;
    }

    public void start() {
        this.start = System.currentTimeMillis();
        cooldowns.put(this.id + this.cooldownName, this);
    }
}
