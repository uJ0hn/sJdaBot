package br.muhdev.bot.handlers.utils;

import br.muhdev.bot.handlers.bothandler.Handler;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;

public final class ConfigManager {

    public Map<String, Object> config;

    public final String path;

    @SneakyThrows
    public ConfigManager(String path) {
        this.path = path;
        InputStream inputStream = Files.newInputStream(new File(path).toPath());
        Yaml yaml = new Yaml();
        config = yaml.load(inputStream);
    }

    @SneakyThrows
    public static void writeCommand(String defaultname, String defaultdesc) {
            File file = new File("commands", defaultname + ".yml");
            String test = "commands/" + defaultname + ".yml";
            if (!file.exists()) {
                File folder = new File("commands");
                if(!folder.mkdir()) System.out.println("Erro ao criar pasta");
                if(!file.createNewFile()) System.out.println("Erro ao criar arquivo");

                new Timer().schedule(new TimerTask() {
                    @Override
                    @SneakyThrows
                    public void run() {
                        Map<String, Object> dataMap = new HashMap<>();
                        dataMap.put("command", defaultname);
                        dataMap.put("description", defaultdesc);
                        dataMap.put("enabled", true);
                        dataMap.put("type", "Geral");

                        PrintWriter writer = new PrintWriter(test);
                        Yaml yaml = new Yaml();
                        yaml.dump(dataMap, writer);
                    }
                }, 2L);


                System.out.println("A configuração " + test + " foi criada.");
                System.exit(0);
            }

        }

    







    public List<String> getKeys(String section) {
        return Arrays.asList(getLinkedHashMap(section).keySet().toString().replace("[", "")
                .replace("]", "")
                .replace(" ", "").split(","));
    }

    public int getInt(String path){
        if(path.contains(".")) {
            String[] paa = path.split("\\.");
            Object result1 = getLinkedHashMap(paa[0]).get(paa[1]);

            if(paa.length == 3) {
                Object result2 = ((LinkedHashMap<?, ?>) result1).get(paa[2]);
                return (Integer) result2;
            } else if (paa.length == 4) {
                LinkedHashMap<?, ?> result3 = (LinkedHashMap<?, ?>) ((LinkedHashMap<?, ?>) result1).get(paa[2]);
                return (Integer) result3.get(paa[3]);
            }
            return (Integer) result1;
        }
        return (Integer) config.get(path);
    }


    public String getString(String path){
        if(path.contains(".")) {
            String[] paa = path.split("\\.");
            Object result1 = getLinkedHashMap(paa[0]).get(paa[1]);

            if(paa.length == 3) {
                Object result2 = ((LinkedHashMap<?, ?>) result1).get(paa[2]);
                return (String) result2;
            } else if (paa.length == 4) {
                LinkedHashMap<?, ?> result3 = (LinkedHashMap<?, ?>) ((LinkedHashMap<?, ?>) result1).get(paa[2]);
                return (String) result3.get(paa[3]);
            }
            return (String) result1;
        }
        return (String) config.get(path);
    }



    public LinkedHashMap<?, ?> getLinkedHashMap(String path) {
        return (LinkedHashMap<?, ?>) config.get(path);
    }

    public boolean getBoolean(String path){
        if(path.contains(".")) {
            String[] paa = path.split("\\.");
            Object result1 = getLinkedHashMap(paa[0]).get(paa[1]);

            if(paa.length == 3) {
                Object result2 = ((LinkedHashMap<?, ?>) result1).get(paa[2]);
                return (Boolean) result2;
            } else if (paa.length == 4) {
                LinkedHashMap<?, ?> result3 = (LinkedHashMap<?, ?>) ((LinkedHashMap<?, ?>) result1).get(paa[2]);
                return (Boolean) result3.get(paa[3]);
            }
            return (Boolean) result1;
        }
        return (Boolean) config.get(path);
    }



    @SuppressWarnings("unchecked")
    public List<String> getStringList(String path) {
        if(path.contains(".")) {
            String[] paa = path.split("\\.");
            Object result1 = getLinkedHashMap(paa[0]).get(paa[1]);

            if(paa.length == 3) {
                Object result2 = ((LinkedHashMap<?, ?>) result1).get(paa[2]);
                return (List<String>) result2;
            } else if (paa.length == 4) {
                LinkedHashMap<?, ?> result3 = (LinkedHashMap<?, ?>) ((LinkedHashMap<?, ?>) result1).get(paa[2]);
                return (List<String>) result3.get(paa[3]);
            }
            return (List<String>) result1;
        }
        return (List<String>) config.get(path);
    }

    @SneakyThrows
    public static void saveDefaultConfig(String... path) {
        File file;
        String test;
        if(path.length == 0) {
            file = new File("config.yml");
            test = "config.yml";
        } else {
            file = new File(path[0]);
            test = path[0];
        }
        if (!file.exists()) {
            FileUtils.copyFile(Objects.requireNonNull(Handler.getInstance().getClass().getClassLoader().getResourceAsStream(test)), file);
            System.out.print("A configuração " + test + " foi criada, configure o seu bot e depois inicie-o novamente.");
        }
    }



}
