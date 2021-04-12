package de.throsenheim.vvss21.helperclasses.writers;

import de.throsenheim.vvss21.helperclasses.readers.ReadFile;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WriteFilesTest {

    WriteFiles writeFiles = WriteFiles.getWriteFiles();
    File file= new File("./KRwaG3kax9w2nHedwfTvgdYHwggAnJbYChDKaqnBQJsqi5yNDduugbUvCh5XzouYKrXdLs7JEQZV5syZxwopHwj74iE2NLUonsCnWPCd4tMvfBqdpYdf6yF7WjpQWNMR.conf");

    @Test
    void createConfig() {
        writeFiles.createConfig(file);
        assertTrue(file.exists());
        List<String> list = new LinkedList<>();
        list.add("This is the config file for alexanderasbeck");
        list.add("Nothing yet ");
        assertEquals(list, ReadFile.readFile(file));
        if(file.exists()){
            file.delete();
        }
    }

    @Test
    void writeFile() {
        List<String> list = new LinkedList<>();
        list.add("This is a random text.");
        list.add("It belongs to a test.");
        list.add("If this file still exists you can delete it!");
        assertTrue(writeFiles.writeFile(file, list, true));
        assertEquals(list, ReadFile.readFile(file));
    }

    @Test
    void noOverwrite() {
        try {
            file.delete();
            file.createNewFile();
        } catch (IOException ignored) {
        }
        List<String> list = new LinkedList<>();
        list.add("This is a random text.");
        list.add("It belongs to a test.");
        list.add("If this file still exists you can delete it!");
        assertFalse(writeFiles.writeFile(file, list, false));
        assertEquals(new LinkedList<String>(), ReadFile.readFile(file));
    }
}