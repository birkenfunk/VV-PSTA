package de.throsenheim.vvss21.persistance;

import de.throsenheim.vvss21.Main;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandlineReaderTest {

    @Test
    void stopTest() throws InterruptedException {
        CommandlineReader commandlineReader = new CommandlineReader(Main.getMeasurementList());
        Thread thread = new Thread(commandlineReader);
        thread.start();
        commandlineReader.stop();
        Thread.sleep(1000);
        assertFalse(thread.isAlive());

    }
}