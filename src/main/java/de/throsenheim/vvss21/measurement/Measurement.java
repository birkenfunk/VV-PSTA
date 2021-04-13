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
     *
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

    public void toJson(File file){
        List<String> list = new LinkedList<>();
        list.add(toJson());
        WriteFiles.getWriteFiles().writeFile(file,list,true);
        String debugString = "Written Json to: "+ file.getAbsolutePath();
        LOGGER.debug(debugString);
    }

    public static List<Measurement> fromJson(File file){
        List<Measurement> res = new LinkedList<>();
        List<String> data = ReadFile.readFile(file);
        while (!data.isEmpty()) {
            res.add(fromJson(data.remove(0)));
            String debugString = res.get(res.size()-1).toString();
            LOGGER.debug(debugString);
        }
        return res;
    }

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

    private static int searchValue(String[] data){
        for (int i = 0; i < data.length; i++) {
            if(data[i].equals("value") && data.length>i+1){
                return Integer.parseInt(data[i+1]);
            }
        }
        return Integer.MIN_VALUE;
    }

    private static Unit searchUnit(String[] data){
        for (int i = 0; i < data.length; i++) {
            if(data[i].equals("unit") && data.length>i+1 && !data[i+1].equals("null")){
                return Unit.valueOf(data[i+1]);
            }
        }
        return null;
    }

    private static Type searchType(String[] data){
        for (int i = 0; i < data.length; i++) {
            if(data[i].equals("type") && data.length>i+1 && !data[i+1].equals("null")){
                return Type.valueOf(data[i+1]);
            }
        }
        return null;
    }

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
