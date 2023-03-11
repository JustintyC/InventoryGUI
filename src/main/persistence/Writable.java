package persistence;

import org.json.JSONObject;

// Based on https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public interface Writable {
    JSONObject toJson();
}
