package de.throsenheim.vvss21.measurement;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import de.throsenheim.vvss21.helperclasses.readers.ReadFile;
import de.throsenheim.vvss21.helperclasses.writers.WriteFiles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class Measurement implements Jsonable {

    private static final Logger LOGGER = LogManager.getLogger(Measurement.class);
    private final int value;
    private final Unit unit;
    private final Type type;
    private final Timestamp timestamp;


    public Measurement(int value, Unit unit, Type type, Timestamp timestamp) {
        this.value = value;
        this.unit = unit;
        this.type = type;
        this.timestamp = timestamp;
    }

    /**
     * Serialize to a JSON formatted string.
     * @return a string, formatted in JSON, that represents the Jsonable.
     */
    @Override
    public String toJson() {
        String keySting;
        JsonObject json = new JsonObject();
        json.put("value",value);
        keySting = "unit";
        if(unit!=null) {
            json.put(keySting, unit.toString());
        }else {
            json.put(keySting, null);
        }
        keySting = "type";
        if(type!=null) {
            json.put(keySting, type.toString());
        }else {
            json.put(keySting, null);
        }
        keySting = "timestamp";
        if(timestamp!=null) {
            json.put(keySting, timestamp.toString());
        }else {
            json.put(keySting, null);
        }
        return json.toJson();
    }

    /**
     * Serialize to a JSON formatted stream.
     *
     * @param writable where the resulting JSON text should be sent.
     * @throws IOException when the writable encounters an I/O error.
     */
    @Override
    public void toJson(Writer writable) throws IOException {
        throw new IOException("Method out of Order use toJson(File file) instead");
    }

    /**
     * Writes the params to a file witch is declared
     * @param file Where to write the file has to be a .json file
     */
    public void toJson(File file){
        if (!file.getName().endsWith(".json")){
            throw new IllegalArgumentException("File has to be a .json file!");
        }
        List<String> list = new LinkedList<>();
        list.add(toJson());
        WriteFiles.getWriteFiles().writeFile(file,list,true);
        String debugString = "Written Json to: "+ file.getAbsolutePath();
        LOGGER.debug(debugString);
    }

    /**
     * Gets all data from a file and translates into {@link Measurement} Objects
     * @param file Where to get the data
     * @return List off Measurement
     */
    public static List<Measurement> fromJson(File file){
        if (!file.getName().endsWith(".json")){
            throw new IllegalArgumentException("File has to be a .json file!");
        }
        List<Measurement> res = new LinkedList<>();
        List<String> data = ReadFile.readFile(file);
        while (!data.isEmpty()) {
            res.add(fromJson(data.remove(0)));
            String debugString = res.get(res.size()-1).toString();
            LOGGER.debug(debugString);
        }
        return res;
    }

    /**
     * Translates a String to a {@link Measurement} Object
     * @param line Sting with the data
     * @return Measurement Object with the data of the Sting
     */
    public static Measurement fromJson(String line){
        int value;
        Unit unit;
        Timestamp timestamp;
        Type type;
        line=line.replaceAll("[\"{}]","");
        String[] splitedline = line.split("[,:]");
        value = searchValue(splitedline);
        unit = searchUnit(splitedline);
        type = searchType(splitedline);
        timestamp = searchTimestamp(splitedline);
        return new Measurement(value,unit,type,timestamp);
    }

    /**
     * Gets the value of a sting Array
     * @param data the data of a json string
     * @return value part of the json sting
     */
    private static int searchValue(String[] data){
        for (int i = 0; i < data.length; i++) {
            if(data[i].equals("value") && data.length>i+1){
                return Integer.parseInt(data[i+1]);
            }
        }
        return Integer.MIN_VALUE;
    }

    /**
     * Gets the {@link Unit} of a sting Array
     * @param data the data of a json string
     * @return Unit part of the json sting
     */
    private static Unit searchUnit(String[] data){
        for (int i = 0; i < data.length; i++) {
            if(data[i].equals("unit") && data.length>i+1 && !data[i+1].equals("null")){
                return Unit.valueOf(data[i+1]);
            }
        }
        return null;
    }

    /**
     * Gets the {@link Type} of a sting Array
     * @param data the data of a json string
     * @return Type part of the json sting
     */
    private static Type searchType(String[] data){
        for (int i = 0; i < data.length; i++) {
            if(data[i].equals("type") && data.length>i+1 && !data[i+1].equals("null")){
                return Type.valueOf(data[i+1]);
            }
        }
        return null;
    }

    /**
     * Gets the {@link Timestamp} of a sting Array
     * @param data the data of a json string
     * @return Timestamp part of the json sting
     */
    private static Timestamp searchTimestamp(String[] data){
        for (int i = 0; i < data.length; i++) {
            if(data[i].equals("timestamp") && data.length>i+1 && !data[i+1].equals("null")){
                StringBuilder buffer = new StringBuilder();
                buffer.append(data[i+1]);
                for (int j = i+2; j < data.length && (data[j].isEmpty() || Character.isDigit(data[j].charAt(0)));j++) {
                    buffer.append(":").append(data[j]);
                }
            return Timestamp.valueOf(buffer.toString());
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "value=" + value +
                ", unit=" + unit +
                ", type=" + type +
                ", timestamp=" + timestamp +
                '}';
    }
}
