package br.muhdev.handlers.utils.economia;

import br.muhdev.backend.Backend;
import br.muhdev.backend.tables.Table;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class EconomiaAPI {

    public static final Table table = Table.tableslist.get("susers");

    private final String userid;
    private EconomiaAPI(String userid) {
        this.userid = userid;
    }

    public static EconomiaAPI getEconomia(String userid) {
        return new EconomiaAPI(userid);
    }

    public MoneyInterface getMoney() {
        return new MoneyInterface(userid);
    }



    public enum type {
        BANK,
        MONEY
    }

    public static JSONObject getAsJsonObject(String parse) {
        try {
            return (JSONObject) new JSONParser().parse(parse);
        } catch (Exception ex) {
            throw new IllegalArgumentException("\"" + parse + "\" is not a JsonObject: ", ex);
        }
    }
}



