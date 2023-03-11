package persistence;

import org.json.JSONObject;

// Based on https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo in Writable
// interface for classes that can be converted into a JSON file
public interface Writable {
    JSONObject toJson();
}
