package br.muhdev.handlers.utils.economia;

import br.muhdev.backend.Backend;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;

import javax.sql.rowset.CachedRowSet;

public class MoneyInterface {
    private final String userid;

    MoneyInterface(String userid) {
        this.userid = userid;

    }

 


    @SneakyThrows
    private JSONObject jsonObject() {
        CachedRowSet query = Backend.getInstance().query("SELECT * FROM susers WHERE userid='" + userid + "'");
        return EconomiaAPI.getAsJsonObject(query.getString("economy"));
    }

    public Object getValueonJson(String value) {
        return jsonObject().get(value);
    }

    public long getQuantie(EconomiaAPI.type a) {
        String quantie = a == EconomiaAPI.type.BANK ? "bank" : "money";
        return Long.parseLong((String) jsonObject().get(quantie));
    }


    @SuppressWarnings("unchecked")
    public void addQuantie(long quantie, EconomiaAPI.type a, String cooldown) {
        long bank = a == EconomiaAPI.type.BANK ? (getQuantie(EconomiaAPI.type.BANK) + quantie) : getQuantie(EconomiaAPI.type.BANK);
        long money = a == EconomiaAPI.type.MONEY ? (getQuantie(EconomiaAPI.type.MONEY) + quantie) : getQuantie(EconomiaAPI.type.MONEY);
        JSONObject object = new JSONObject();
        object.put("money", "" +money);
        object.put("bank", "" +bank);
        object.put("cooldown", "" +cooldown);
        Backend.getInstance().execute(EconomiaAPI.table.update("economy=?"), object.toJSONString());
    }


    @SuppressWarnings("unchecked")
    public void removeQuantie(long quantie, EconomiaAPI.type a, String cooldown) {
        long bank = a == EconomiaAPI.type.BANK ? (getQuantie(EconomiaAPI.type.BANK) - quantie) : getQuantie(EconomiaAPI.type.BANK);
        long money = a == EconomiaAPI.type.MONEY ? (getQuantie(EconomiaAPI.type.MONEY) - quantie) : getQuantie(EconomiaAPI.type.MONEY);
        JSONObject object = new JSONObject();
        object.put("money", "" +money);
        object.put("bank", "" +bank);
        object.put("cooldown", "" +cooldown);
        Backend.getInstance().execute(EconomiaAPI.table.update("economy=?"), object.toJSONString());
    }

    @SuppressWarnings("unchecked")
    public boolean setQuantie(long quantie, EconomiaAPI.type a, String cooldown) {
        long bank = a == EconomiaAPI.type.BANK ? quantie : getQuantie(a);
        long money = a == EconomiaAPI.type.MONEY ? quantie : getQuantie(a);
        JSONObject object = new JSONObject();
        object.put("money", "" +money);
        object.put("bank", "" +bank);
        object.put("cooldown", "" +cooldown);
        Backend.getInstance().execute(EconomiaAPI.table.update("economy=?"), object.toJSONString());
        return true;
    }

}
