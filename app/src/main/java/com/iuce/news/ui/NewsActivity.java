package com.iuce.news.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.iuce.news.R;
import com.iuce.news.db.entity.News;
import com.iuce.news.db.entity.State;
import com.iuce.news.db.pojo.NewsWithState;
import com.iuce.news.viewmodel.NewsViewModel;
import com.squareup.picasso.Picasso;

public class NewsActivity extends AppCompatActivity {

    public static final String TAG = "NewsActivity";
    public static final String NAME_NEWS_ID = "newsID";

    private NewsViewModel newsViewModel;
    private NewsWithState selectedNewsWithState;

    TextView source;
    TextView date;
    TextView title;
    TextView description;
    TextView content;
    ImageView image;
    TextView tv_showInWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_news_activity);

        initViews();

        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        newsViewModel.getNewsWithStateByIDs(getIntent().getIntExtra(NAME_NEWS_ID, 0)).observe(this,
                newsWithStates -> {
                    selectedNewsWithState = newsWithStates.get(0);
                    fillView(selectedNewsWithState.getNews());
                    newsViewModel.insertStates(State.Builder(selectedNewsWithState, State.Type.READ));
                }
        );
    }

    public void initViews() {
        source = findViewById(R.id.news_source);
        date = findViewById(R.id.news_date);
        title = findViewById(R.id.news_title);
        description = findViewById(R.id.news_description);
        content = findViewById(R.id.news_content);
        image = findViewById(R.id.news_image);
        tv_showInWeb = findViewById(R.id.show_in_web);
    }

    public void fillView(News news) {
        source.setText(news.getSource());
        date.setText(news.getPublishedAt());
        description.setText(news.getDescription());
        content.setText(news.getContent());
        title.setText(news.getTitle());

        // Details: https://stackoverflow.com/a/40440694
        Picasso.get()
                .load(Uri.parse(news.getUrlToImage()))
                .into(image);

        // Details: https://stackoverflow.com/a/25380654
        String html = "<a href=\""+ news.getUrl() +"\">🌐 Web sitesinde görüntüle</a>";
        tv_showInWeb.setMovementMethod(LinkMovementMethod.getInstance());
        tv_showInWeb.setText(Html.fromHtml(html));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
         * Details:
         * https://google-developer-training.github.io/android-developer-fundamentals-course-concepts-v2/unit-2-user-experience/lesson-4-user-interaction/4-3-c-menus-and-pickers/4-3-c-menus-and-pickers.html
         */
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);

        if (State.Type.LIKED.isExist(selectedNewsWithState.getStates())) {
            menu.findItem(R.id.fav_button).setIcon(R.drawable.ic_favorite_white_24dp);
        } else {
            menu.findItem(R.id.fav_button).setIcon(R.drawable.ic_favorite_border_white_24dp);
        }

        return true;
    }

    public void onShareClick(MenuItem item) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.putExtra(Intent.EXTRA_TEXT, selectedNewsWithState.getNews().getTitle() + "\n"
                + selectedNewsWithState.getNews().getDescription() + "\n"
                + selectedNewsWithState.getNews().getUrl());
        shareIntent.setType("text/plain");
        Intent chooser = Intent.createChooser(shareIntent, "title");

        // Resolve the intent before starting the activity
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }


    public void toggleLikeIcon(MenuItem item) {
        State state = State.Type.LIKED.findState(selectedNewsWithState.getStates());
        if (state != null) {
            newsViewModel.deleteStates(state);
            item.setIcon(R.drawable.ic_favorite_border_white_24dp);
        } else {
            newsViewModel.insertStates(State.Builder(selectedNewsWithState, State.Type.LIKED));
            item.setIcon(R.drawable.ic_favorite_white_24dp);
        }

        Log.d(TAG, "toggleLikeIcon: " + selectedNewsWithState.getStates());
    }
}
