package com.iuce.news.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iuce.news.db.entity.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewsAPI {
    static final String TAG = "NewsAPI"; // May be in `strings.xml`

    private static final String[] API_KEYS = {"cf9168e3e5ff4e8987492262f92632fb", "f82c72913e944b0c838e24a52e90db8c"}; // Not secure!
    private static final String URL_TEMPLATE = "https://newsapi.org/v2/top-headlines?country=tr&apiKey=%s";

    /**
     * Gets URL
     *
     * @return Generated {@link #URL_TEMPLATE} with a random {@link #API_KEYS}
     */
    private static String generateURL() {
        int ix = new Random().nextInt(API_KEYS.length);
        return String.format(URL_TEMPLATE, API_KEYS[ix]);
    }

    /**
     * @see <a href="https://android.yemreak.com/temel/http-istekleri">HTTPS istekleri ~ YEmreAk</a>
     * @see <a href="https://developer.android.com/training/volley/simple.html">Volley ~ Android
     * Developer</a>
     */
    public static void requestNewsData(Context context, ResponseListener responseListener) {

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, generateURL(), (response) -> {
            // https://stackoverflow.com/a/9606629/9770490
            try {
                // new JSONObject(response).getJSONArray("articles").get(1).getString("source")
                JSONObject responseObject = new JSONObject(response);

                String status = responseObject.getString("status");
                if (status.equals("ok")) {
                    JSONArray articles = new JSONObject(response).getJSONArray("articles");

                    List<News> newsDataList = new ArrayList<>();
                    for (int i = 0; i < articles.length(); i++) {
                        JSONObject article = articles.getJSONObject(i);

                        News news = new News(
                                article.getString("title"),
                                article.getString("description"),
                                article.getString("urlToImage"),
                                article.getString("url"),
                                article.getString("content"),
                                article.getJSONObject("source").getString("name"),
                                article.getString("publishedAt")
                        );
                        newsDataList.add(news);

                        Log.i(TAG, "getNewsData: " + newsDataList.get(i));
                    }

                    responseListener.onResponse(newsDataList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        queue.add(stringRequest);
    }

    public interface ResponseListener {
        void onResponse(List<News> newsDataList);
    }
}
