package de.throsenheim.vvss21;

import de.throsenheim.vvss21.tcpserver.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private Main main;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private static final Logger LOGGER = LogManager.getLogger(MainTest.class);

    @AfterAll
    static void afterAll() {
        Server.stop();
    }

    @AfterEach
    void tearDown() {
        Main.getMeasurementList().stop();
    }

    @BeforeEach
    void setUp() {
        LOGGER.debug("Executing Tests for main\n\n\n");
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void changeConfFile() {
        String[] args = {"--conf","test.conf"};
        main = new Main(args);
        assertEquals(new File("test.conf"),main.getConfigFile());
    }

    @Test
    void nonConfFile() {
        String[] args = {"--conf","test"};
        main = new Main(args);
        assertEquals(new File("alexanderasbeck.conf"),main.getConfigFile());
    }

    @Test
    void testEmptyReadConf() throws Exception{
        String[] args = {"--conf","test.conf"};
        main = new Main(args);
        List<List> list = new LinkedList<>();

        Method method = main.getClass().getDeclaredMethod("readConf", List.class);
        method.setAccessible(true);
        assertFalse((Boolean) method.invoke(main,list));

    }

    @Test
    void testReadConf() throws Exception{
        String[] args = {"--conf","test.conf"};
        main = new Main(args);
        List<String> list = new LinkedList<>();
        list.add("This is the config file for alexanderasbeck");
        list.add("JSON_Location");
        Method method = main.getClass().getDeclaredMethod("readConf", List.class);
        method.setAccessible(true);
        assertTrue((Boolean) method.invoke(main,list));
    }

    @Test
    void testCompareParameterChangeConfigFile() throws Exception{
        String[] args = {};
        main = new Main(args);
        assertEquals(new File("alexanderasbeck.conf"),main.getConfigFile());
        Field field = main.getClass().getDeclaredField("readConsole");
        field.setAccessible(true);
        Main.ReadConsole readConsole = (Main.ReadConsole) field.get(main);
        Method method = readConsole.getClass().getDeclaredMethod("commandComparison", String.class);
        method.setAccessible(true);
        method.invoke(readConsole,"config test.conf");
        field = Main.ReadConsole.class.getDeclaredField("read");
        field.setAccessible(true);
        assertTrue((Boolean) field.get(readConsole));
        method.invoke(readConsole, "exit");
        assertFalse((Boolean) field.get(readConsole));
        assertEquals(new File("test.conf"),main.getConfigFile());
    }
}