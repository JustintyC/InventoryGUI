package persistence;

import model.EmptySlot;
import model.Inventory;
import model.Item;
import model.Slot;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Based on https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Inventory read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseInventory(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses JSONObject into inventory and returns it
    private Inventory parseInventory(JSONObject jsonObject) {
        Inventory inventory = new Inventory();
        addSlots(inventory, jsonObject);
        addItemBank(inventory, jsonObject);
        return inventory;
    }

    // MODIFIES: inventory
    // EFFECTS: parses slots from JSONObject and adds them to inventory
    private void addSlots(Inventory inventory, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("inventory");
        int position = 0;
        for (Object json : jsonArray) {
            JSONObject nextSlot = (JSONObject) json;
            addSlot(inventory, nextSlot, position);
            position++;
        }
    }

    // EFFECTS: parses JSONObject into item and adds to correct inventory slot; do nothing if JSONObject represents an
    //          empty slot
    private void addSlot(Inventory inventory, JSONObject jsonObject, int position) {
        if (!(jsonObject.getInt("itemID") == -1)) {
            String itemName = jsonObject.getString("name");
            int itemID = jsonObject.getInt("itemID");
            int stackCount = jsonObject.getInt("stackCount");
            int maxStackSize = jsonObject.getInt("maxStackSize");

            Item item = new Item(itemName, itemID, maxStackSize);
            item.setStackCount(stackCount);
            inventory.insertItem(position, item);
        }
    }

    // MODIFIES: inventory
    // EFFECTS: parses itemBank from JSONObject and sets it as inventory's itemBank
    // TODO
    private void addItemBank(Inventory inventory, JSONObject jsonObject) {

    }
}
