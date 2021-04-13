package de.throsenheim.vvss21.measurement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

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


}