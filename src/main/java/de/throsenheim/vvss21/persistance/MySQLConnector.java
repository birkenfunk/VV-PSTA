package de.throsenheim.vvss21.persistance;

import com.sun.security.auth.login.ConfigFile;
import de.throsenheim.vvss21.application.interfaces.IDatabase;
import de.throsenheim.vvss21.common.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnector implements IDatabase {

    private static final MySQLConnector MY_SQL_CONNECTOR = new MySQLConnector();
    private Connection con;
    private Statement stmt;
    private boolean running;

    private MySQLConnector() {
    }

    private void open(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://"+ Config.getdBURL() +"?useSSL=false&serverTimezone=UTC",Config.getdBUsername(),Config.getdBPassword());
            stmt = con.createStatement();
            running=true;
        }catch (SQLException | ClassNotFoundException e){
            running=false;
            e.printStackTrace();
        }
    }

}
