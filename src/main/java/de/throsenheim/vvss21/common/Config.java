package de.throsenheim.vvss21.common;

public class Config {

    private static final String DBUSERNAME = readDBUsername();
    private static final String DBPASSWORD = readDBPassword();
    private static final String DBURL = readDBURL();

    private Config() {
    }

    private static String readDBPassword() {
        return System.getenv().getOrDefault("DBPassword","1234");
    }

    private static String readDBURL() {
        return System.getenv().getOrDefault("DBURL","localhost:3306/SmartHomeService");
    }

    private static String readDBUsername() {
        return System.getenv().getOrDefault("DBUsername","Alex");
    }

    public static String getdBUsername() {
        return DBUSERNAME;
    }

    public static String getdBPassword() {
        return DBPASSWORD;
    }

    public static String getdBURL() {
        return DBURL;
    }
}
