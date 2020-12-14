package com.example.outlab9;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GetNews implements Callable<ArrayList<String>>{
    public ArrayList<String> call() {
        String news;
        ArrayList<String> values = new ArrayList<String>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://bing-news-search1.p.rapidapi.com/news?safeSearch=Off&textFormat=Raw")
                .get()
                .addHeader("x-bingapis-sdk", "true")
                .addHeader("x-rapidapi-key", "3f3723aa27mshb763be6f66a7c88p1b4bedjsn1a77cf33295a")
                .addHeader("x-rapidapi-host", "bing-news-search1.p.rapidapi.com")
                .build();
        try {
            Response response = client.newCall(request).execute();
            news = response.body().string();
            JSONObject reader = new JSONObject(news);
            ArrayList<String> headlineList = new ArrayList<String>();
            JSONArray newsObjects = reader.getJSONArray("value");
            for (int i = 0; i<newsObjects.length(); i++) {
                JSONObject v = newsObjects.getJSONObject(i);
                values.add(v.getString("name"));
            }
        } catch (Exception e) {
            values.add(e.toString());
        }
        return values;
    }
}
