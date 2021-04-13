package de.throsenheim.vvss21.helperclasses.readers;

import de.throsenheim.vvss21.helperclasses.writers.WriteFiles;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReadFileTest {


    File file= new File("./KRwaG3kax9w2nHedwfTvgdYHwggAnJbYChDKaqnBQJsqi5yNDduugbUvCh5XzouYKrXdLs7JEQZV5syZxwopHwj74iE2NLUonsCnWPCd4tMvfBqdpYdf6yF7WjpQWNMR.conf");

    @Test
    void readFile() {
        List<String> list = new LinkedList<>();
        list.add("This is a random text.");
        list.add("It belongs to a test.");
        list.add("If this file still exists you can delete it!");
        WriteFiles.writeFile(file, list, true);
        assertEquals(list, ReadFile.readFile(file));
    }

    @Test
    void readNonExistingFile() {
        if(file.exists()){
            file.delete();
        }
        assertEquals(new LinkedList<String>(), ReadFile.readFile(file));
    }
}