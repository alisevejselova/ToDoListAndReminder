package com.example.ToDoListAndReminder;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataPutRequest extends AsyncTask<String, Void, Void> {


    @Override
    public Void doInBackground(String... strings) {
        InputStream inputStream = null;
        String result = "";

        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            String baseurl = "https://jsonplaceholder.typicode.com/todos/";
            String todoId = strings[0];
            String url = baseurl + todoId;
            // 2. make POST request to the given URL
            HttpPut httpPUT = new HttpPut(url);
            String json = "";
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            String complete = strings[1];
            jsonObject.put("completed", complete);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
            // 6. set httpPost Entity
            httpPUT.setEntity(se);
            // 7. Set some headers to inform server about the type of the content
            httpPUT.setHeader("Accept", "application/json");
            httpPUT.setHeader("Content-type", "application/json");
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPUT);
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
            Log.i("response", result);
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return null;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        inputStream.close();
        return sb.toString();
    }
}
