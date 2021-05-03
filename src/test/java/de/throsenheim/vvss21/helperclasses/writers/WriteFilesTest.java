package de.throsenheim.vvss21.helperclasses.writers;

import de.throsenheim.vvss21.common.ReadFile;
import de.throsenheim.vvss21.common.WriteFiles;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WriteFilesTest {


    File file= new File("./KRwaG3kax9w2nHedwfTvgdYHwggAnJbYChDKaqnBQJsqi5yNDduugbUvCh5XzouYKrXdLs7JEQZV5syZxwopHwj74iE2NLUonsCnWPCd4tMvfBqdpYdf6yF7WjpQWNMR.conf");

    @Test
    void writeFile() {
        List<String> list = new LinkedList<>();
        list.add("This is a random text.");
        list.add("It belongs to a test.");
        list.add("If this file still exists you can delete it!");
        assertTrue(WriteFiles.writeFile(file, list, true));
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
        assertTrue(WriteFiles.writeFile(file, list, true));
        list.add("New Line");
        List<String> list1 = new LinkedList<>();
        list1.add("New Line");
        assertTrue(WriteFiles.writeFile(file, list1,false));
        assertEquals(list, ReadFile.readFile(file));
    }
}