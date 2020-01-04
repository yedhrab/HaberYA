package com.iuce.news.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iuce.news.R;
import com.iuce.news.api.NewsAPI;
import com.iuce.news.db.entity.News;
import com.iuce.news.db.entity.State;
import com.iuce.news.db.pojo.NewsWithState;
import com.iuce.news.viewmodel.NewsViewModel;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private NewsViewModel newsViewModel;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // lifecycle-extensions:$arch_lifecycle:2.2.0-beta01 versiyonuna uygun :'(
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        initRecyclerView();

    }

    private void initRecyclerView() {
        newsViewModel.getAllNewsWithState().observe(this, this::fillView);
    }

    private void fillView(List<NewsWithState> newsWithStateList) {
        RecyclerView recyclerView = findViewById(R.id.news_recycler_view);
        NewsAdapter newsAdapter = new NewsAdapter(this, newsWithStateList);
        recyclerView.setAdapter(newsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
         * Details:
         * https://google-developer-training.github.io/android-developer-fundamentals-course-concepts-v2/unit-2-user-experience/lesson-4-user-interaction/4-3-c-menus-and-pickers/4-3-c-menus-and-pickers.html
         */
        getMenuInflater().inflate(R.menu.main_layout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.get_all_reacted_but:
                switchActivity(null);
                return true;
            case R.id.get_liked_but:
                switchActivity(State.Type.LIKED);
                return true;
            case R.id.get_saved_but:
                switchActivity(State.Type.LATER);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void switchActivity(State.Type type) {
        Intent intent = new Intent(this, ReactedActivity.class);
        intent.putExtra(ReactedActivity.NAME_STATE_TYPE, type);
        this.startActivity(intent);
    }





}
