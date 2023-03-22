package br.muhdev.handlers.utils.clusters;

import br.muhdev.backend.Backend;
import br.muhdev.backend.tables.Clusters;
import br.muhdev.backend.tables.Table;
import br.muhdev.bot.Main;
import br.muhdev.handlers.bothandler.Handler;
import lombok.SneakyThrows;

import javax.sql.rowset.CachedRowSet;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClustersAPI {

    private final int id;
    private static final Table table = Table.tableslist.get("clustersonline");

    public ClustersAPI(int id) {
        this.id = id;
    }

    public boolean clusterDisponible() {
        return Backend.getInstance().query(table.select() + " WHERE clusterid=?", "" + id) != null;
    }
    @SneakyThrows
    public static List<ClustersAPI> getAllClusters() {
        List<ClustersAPI> list = new ArrayList<>();
        CachedRowSet q = Backend.getInstance().query(table.select());
        for(int i = 0 ; i < q.size() ; i++) {
            list.add(new ClustersAPI(q.getInt("clusterid")));
            q.next();
        }
        return list;
    }


    public long getPing() {
        long ping = 0;
        try {
            Socket cliente = new Socket("192.168.0.15", (int) getPort());
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeInt(getId());
            saida.writeObject("getping");

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

            ping = (long)entrada.readObject();
        } catch (Exception ignored) {}
        return ping;
    }

    @SneakyThrows
    public long getPort() {
        return Backend.getInstance().query(table.select() + " WHERE clusterid=?", "" + getId()).getLong("clusterport");
    }

    @SneakyThrows
    public void start() {
        ServerSocket servidor = new ServerSocket((int) getPort());
        System.out.println("Servidor ouvindo a porta " + getPort());
        method(servidor);
    }

    private void method(ServerSocket server ){
        try {
            while(true) {
                Socket cliente = server.accept();

                ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
                int id = entrada.readInt();
                if(id != getId()) return;
                String coitado = (String) entrada.readObject();
                if(coitado.equalsIgnoreCase("getping")) {
                    ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                    saida.flush();
                    saida.writeObject(Handler.getInstance().getJda().getGatewayPing());
                    saida.close();
                    cliente.close();
                } else if(coitado.equalsIgnoreCase("exit")) System.exit(0);

            }
        } catch (Exception e) {
            method(server);
        }
    }

    @SneakyThrows
    public void insertCluster() {
        Backend.getInstance().execute(new Clusters().insert(), id, "177.181.3.195", System.currentTimeMillis(), Main.getInstance().getConfig().getInt("clusterport") );
    }

    public int getId() {
        return id;
    }

    @SneakyThrows
    public static ClustersAPI getLocalCluster() {
        return new ClustersAPI(Main.getInstance().getConfig().getInt("cluster"));
    }

    @SneakyThrows
    private String getAddress() {
        URLConnection connect = new URL("https://ipv4.icanhazip.com").openConnection();
        connect.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        Scanner scan = new Scanner(connect.getInputStream());
        StringBuilder b = new StringBuilder();
        while (scan.hasNext()) {
            String text = scan.next();
            b.append(text);
        }
        scan.close();
        return b.toString();
    }

    public void deleteCluster() {
        Backend.getInstance().execute(table.delete() + " WHERE clusterid=?", "" + id);
    }



}
