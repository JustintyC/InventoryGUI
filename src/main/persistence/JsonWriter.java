package persistence;

import model.Inventory;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Based on https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a writer that writes JSON representation of workroom to file
public class JsonWriter {
    private static final int TAB = 4;
    private String destination;
    private PrintWriter writer;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of workroom to file
    public void write(Inventory inventory) {
        JSONObject json = inventory.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }

}
