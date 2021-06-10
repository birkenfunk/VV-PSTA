package de.throsenheim.vvss21.presentation;

import de.throsenheim.vvss21.application.IDBConnector;
import de.throsenheim.vvss21.persistence.MySQLConnector;

public class DatabaseMapper {

    private DatabaseMapper(){

    }

    public static IDBConnector getMySQLDatabase(){
        return new MySQLConnector();
    }

}
