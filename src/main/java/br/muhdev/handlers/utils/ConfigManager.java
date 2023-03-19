package br.muhdev.handlers.utils;

import br.muhdev.handlers.bothandler.Handler;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
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


    public List<String> getKeys(String section) {
        return Arrays.asList(getLinkedHashMap(section).keySet().toString().replace("[", "")
                .replace("]", "")
                .replace(" ", "").split(","));
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
    public static void saveCommand(String path) {
        File file = new File("commands", path + ".yml");
            String test = "commands/" + path + ".yml";

        if (!file.exists()) {
            File folder = new File("commands");
            if(!folder.exists()) folder.mkdir();
            FileUtils.copyFile(Objects.requireNonNull(Handler.getInstance().getClass().getClassLoader().getResourceAsStream(test)), file);
            System.out.println("A configuração " + test + " foi criada.");
        }
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




    public static ConfigManager getConfig(String... path) {
        String path1 = path.length == 0 ? "config.yml" : path[0];
        return new ConfigManager(path1);
    }


}
