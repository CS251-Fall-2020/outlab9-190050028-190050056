package com.example.outlab9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private ArrayList<String> news = new ArrayList<String>();
    private boolean isLoading = false;
    private boolean firstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadNews();
        adapter = new CustomAdapter(news);
        recyclerView.setAdapter(adapter);
        initScrollListener();
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                Log.d("Loader", "Loading is required!");
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == news.size()-1) {
                        Log.d("Loader", "Reached end, loading news");
                        loadNews();
                        Log.d("Loader", "Loaded news");
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadNews() {
//        try {
//            GetNews gn = new GetNews(news);
//            ExecutorService es = Executors.newSingleThreadExecutor();
//            Future<ArrayList<String>>  nw = es.submit(gn);
//            news = nw.get();
//            if (firstLoad) {
//                firstLoad = false;
//            }
//            else {
//                recyclerView.post(new Runnable() {
//                    public void run() {
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//            }
//            isLoading = false;
//        } catch (Exception e) {
//            news.add("Early error");
//            if (firstLoad) {
//                firstLoad = false;
//            }
//            else {
//                recyclerView.post(new Runnable() {
//                    public void run() {
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//            }
//            isLoading = false;
//        }
        Handler handler = new Handler();
        handler.post(new Runnable() {
            public void run() {
                GetNews gn = new GetNews(news);
                ExecutorService es = Executors.newSingleThreadExecutor();
                Future<ArrayList<String>> nw = es.submit(gn);
                try {
                    news = nw.get();
                    if (firstLoad) {
                        firstLoad = false;
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    isLoading = false;
                } catch (Exception e){
                    Log.d("Error", "error");
                }
            }
        });
    }
}
