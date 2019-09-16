package com.eastinno.otransos.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.eastinno.otransos.core.util.I18n;
import com.eastinno.otransos.web.Globals;

public class DerbyServer {

    private static DerbyServer instance;

    private final static Object keyForDerbyServer = new Object();

    public static DerbyServer getInstance() {
        if (DerbyServer.instance == null) {
            synchronized (DerbyServer.keyForDerbyServer) {
                if (DerbyServer.instance == null) {
                    DerbyServer.instance = new DerbyServer();
                }
            }
        }
        return DerbyServer.instance;
    }

    private DerbyServer() {
    }

    public static void main(String[] a) {
        DerbyServer ds = new DerbyServer();
        ds.run(a);
    }

    /**
     * @param s
     */
    public void run(String... s) {
        String dir = "../data";
        if (s.length == 1) {
            dir = s[0];
        }
        String driver = "org.apache.derby.jdbc.ClientDriver";
        System.setProperty("derby.system.home", dir);
        String dbName = "disco";
        String connectionURL = "";
        File f = new File(dir);
        boolean b = false;
        if (f.exists()) {
            b = true;
            connectionURL = "jdbc:derby://localhost:4944/" + dbName + ";user=disco;pwd=disco;create=false;";
        } else {
            b = false;
            connectionURL = "jdbc:derby://localhost:1527/" + dbName + ";user=disco;pwd=disco;create=true;";
        }
        try {
            // NetworkServerControl derbyServer = new NetworkServerControl();
            PrintWriter pw = new PrintWriter(System.out);
            // derbyServer.start(pw);

            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionURL);// 本地连接数据库
            Statement st = conn.createStatement();

            if (!b) {
                st.execute("create table disco  (version varchar(30) not null)");
                st.executeUpdate("insert into disco values ('" + Globals.VERSION + "')");
            }

            ResultSet rs = st.executeQuery("select * from disco");// 读取刚插入的数据
            while (rs.next()) {
                rs.getString(1);
                // System.out.println("当前Disco 版本 " +
                // version);
            }
            System.out.println(I18n.getLocaleMessage("ext.Successful.launch.of.the.database.server"));
            System.out.println(I18n.getLocaleMessage("ext.Press.Ctrl+C.button.or.the.Enter.key.stop.service"));
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            in.readLine();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
