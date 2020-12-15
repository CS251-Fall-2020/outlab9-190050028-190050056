package com.example.outlab9;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GetNews implements Callable<ArrayList<String>>{

    private ArrayList<String> newsArray;

    public GetNews(ArrayList<String> arr) {
        newsArray = arr;
    }

    public ArrayList<String> call() {
        String news;
        OkHttpClient client = new OkHttpClient();
        int offset = newsArray.size();
        Request request = new Request.Builder()
                .url("https://bing-news-search1.p.rapidapi.com/news?count=15&offset=" + offset + "&mkt=en-IN&safeSearch=Off&category=World&textFormat=Raw")
                .get()
                .addHeader("x-bingapis-sdk", "true")
                .addHeader("x-rapidapi-key", "3f3723aa27mshb763be6f66a7c88p1b4bedjsn1a77cf33295a")
                .addHeader("x-rapidapi-host", "bing-news-search1.p.rapidapi.com")
                .build();
        try {
            Response response = client.newCall(request).execute();
            news = response.body().string();
            JSONObject reader = new JSONObject(news);
            JSONArray newsObjects = reader.getJSONArray("value");
            for (int i = 0; i<newsObjects.length(); i++) {
                JSONObject v = newsObjects.getJSONObject(i);
                newsArray.add(v.getString("name"));
            }
        } catch (Exception e) {
            newsArray.add(e.toString());
        }
        return newsArray;
    }
}
