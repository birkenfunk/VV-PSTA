package de.throsenheim.vvss21.measurement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class MeasurementTest {

    private Measurement measurement;
    private String compare;
    private File file;

    @BeforeEach
    void setUp() {
        measurement = new Measurement(10, Unit.CELSIUS,Type.TEMPERATURE, Timestamp.valueOf("2021-04-03 17:48:01"));
        compare = "{\"unit\":\"CELSIUS\",\"type\":\"TEMPERATURE\",\"value\":10,\"timestamp\":\"2021-04-03 17:48:01.0\"}";
        file = new File("KRwaG3kax9w2nHedwfTvgdYHwggAnJbYChDKaqnBQJsqi5yNDduugbUvCh5XzouYKrXdLs7JEQZV5syZxwopHwj74iE2NLUonsCnWPCd4tMvfBqdpYdf6yF7WjpQWNMR.json");
    }

    @Test
    void toJson() {
        assertEquals(compare,measurement.toJson());
    }

    @Test
    void testToJson() {
        measurement.toJson(file);
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    void fromJson() {
        measurement.toJson(file);
        Measurement newMes = Measurement.fromJson(file);
        assertNotNull(newMes);
        assertEquals(compare , newMes.toJson());
    }

    @Test
    void toJson2(){
        assertThrows(IOException.class ,()->measurement.toJson(new FileWriter(file)));
    }

    @Test
    void fromNullJson() {
        measurement = new Measurement(0,null,null,null);
        assertEquals("{\"unit\":null,\"type\":null,\"value\":0,\"timestamp\":null}",measurement.toJson());
        measurement.toJson(file);
        assertTrue(file.exists());
        Measurement newMes = Measurement.fromJson(file);
        assertNotNull(newMes);
        assertEquals("{\"unit\":null,\"type\":null,\"value\":0,\"timestamp\":null}",newMes.toJson());
    }
}