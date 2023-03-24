package br.muhdev.bot.handlers.utils.clusters;

import br.muhdev.bot.backend.Backend;
import br.muhdev.bot.backend.tables.Clusters;
import br.muhdev.bot.backend.tables.Table;
import br.muhdev.bot.Main;
import br.muhdev.bot.handlers.bothandler.Handler;
import lombok.SneakyThrows;

import javax.sql.rowset.CachedRowSet;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ClustersAPI {

    private final int id;
    private static final Table table = Table.tableslist.get("clustersonline");


    ClustersAPI(int id) {
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
            Socket cliente = new Socket(getAddress(), (int) getPort());
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeInt(getId());
            saida.writeUTF(Main.getInstance().getConfig().getString("clusteruser") + " " +
                    Main.getInstance().getConfig().getString("clusterpass"));
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
        if (!servidor.isBound()){
            servidor.bind(new InetSocketAddress("0.0.0.0", 0));
        }
        System.out.println("Servidor ouvindo a porta " + getPort());
        method(servidor);
    }

    @SneakyThrows
    public void shutdown() {
        Socket cliente = new Socket(getAddress(), (int) getPort());
        ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
        saida.flush();
        saida.writeInt(getId());
        saida.writeUTF(Main.getInstance().getConfig().getString("clusteruser") + " " +
                Main.getInstance().getConfig().getString("clusterpass"));
        saida.writeObject("exit");
    }


    private void method(ServerSocket server ){
        try {
            while(true) {
                Socket cliente = server.accept();

                ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
                int id = entrada.readInt();
                if(id != getId()) return;
                String[] user = entrada.readUTF().split(" ");
                if(!user[0].equalsIgnoreCase(Main.getInstance().getConfig().getString("clusteruser"))) return;
                else if(!user[1].equalsIgnoreCase(Main.getInstance().getConfig().getString("clusterpass"))) return;
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
        Backend.getInstance().execute(new Clusters().insert(), id, Main.getInstance().getConfig().getString("clusterip"), System.currentTimeMillis(), Main.getInstance().getConfig().getInt("clusterport") );
    }

    public int getId() {
        return id;
    }

    @SneakyThrows
    public static ClustersAPI getLocalCluster() {
        return new ClustersAPI(Main.getInstance().getConfig().getInt("cluster"));
    }


    @SneakyThrows
    public static ClustersAPI getCluster(int id) {
        return new ClustersAPI(id);
    }

    @SneakyThrows
    public String getAddress() {
        return Backend.getInstance().query(table.select() + " WHERE clusterid=?", "" +id).getString("clusterip");
    }

    public void deleteCluster() {
        Backend.getInstance().execute(table.delete() + " WHERE clusterid=?", "" + id);
    }



}
