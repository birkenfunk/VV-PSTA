package de.throsenheim.vvss21.common;

import de.throsenheim.vvss21.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class ConfigData {

    private static String jsonLocation = "data.json";
    private static int port = 1024;
    private static final Logger LOGGER = LogManager.getLogger(ConfigData.class);

    private ConfigData(){

    }

    /**
     * Method reads the properties out of a file and sets it
     */
    private static void readProperties(){
        try (InputStream fileInputStream = Main.class.getClassLoader().getResourceAsStream("alexanderasbeck.properties")){
            Properties properties = new Properties();
            properties.load(fileInputStream);
            String temp;
            if(properties.containsKey("JSON_FILE")) {
                temp = properties.getProperty("JSON_FILE");
                jsonLocation = transformProperties(temp);
            }
            if(properties.containsKey("PORT")) {
                temp = properties.getProperty("PORT");
                temp = transformProperties(temp);
                port = Integer.parseInt(temp);
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }

    }

    /**
     * Transforms a property string into a Correct property
     * @param property String from the property file
     * @return String with the correct data
     */
    private static String transformProperties(String property){
        if(!property.startsWith("${") && !property.endsWith("}")){
            return property;
        }
        String copyProperty = property.replaceAll("[${|}]","");
        String[] splitedProperty = copyProperty.split(":");
        if (!splitedProperty[0].equals("env") && splitedProperty.length<3){
            return property;
        }
        Map<String, String> env = System.getenv();
        if(env.containsKey(splitedProperty[1])){
            return env.get(splitedProperty[1]);
        }
        return splitedProperty[2].replaceAll("[-]","");
    }

    /**
     * Returns the location for the json file
     * <p> Gets location from the config file
     * @return location for the json file
     */
    public static String getJsonLocation() {
        readProperties();
        return jsonLocation;
    }


    public static int getPort() {
        readProperties();
        return port;
    }

}
