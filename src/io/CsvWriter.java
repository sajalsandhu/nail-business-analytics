package io;

import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {

    public static void appendLine(String path, String line) {
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(line);
            fw.write(System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException("Could not write to " + path + ": " + e.getMessage());
        }
    }
}
