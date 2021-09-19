package com.example.ToDoListAndReminder;

import com.example.ToDoListAndReminder.Models.Todos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {

    //od String  vo  JSONArray
    public static ArrayList<Todos> dataParsing(String data) {

        ArrayList<Todos> arrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                int id = jsonObj.getInt("id");
                int userId = jsonObj.getInt("userId");
                String title = jsonObj.getString("title");
                boolean completed = jsonObj.getBoolean("completed");

                Todos model = new Todos();
                model.setId(id);
                model.setUserId(userId);
                model.setTitle(title);
                model.setCompleted(completed);
                arrayList.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
