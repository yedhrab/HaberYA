package com.iuce.news;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.iuce.news.News.TABLE_NAME;

/**
 * Details: https://android.yemreak.com/veriler/room-database#entity-yapisi
 */
@Entity(tableName = TABLE_NAME)
public class News {

    public static final String TABLE_NAME = "news_table";
    public static final String COLUMN_ID = "nid";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_URL_TO_IMAGE = "url_to_image";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_SOURCE = "source";
    private static final String COLUMN_PUBLISHED_AT = "published_at";
    private static final String COLUMN_URL = "url";

    private static News instance;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    private int id;

    @ColumnInfo(name = COLUMN_TITLE)
    private String title;

    @ColumnInfo(name = COLUMN_DESCRIPTION)
    private String description;

    @ColumnInfo(name = COLUMN_URL_TO_IMAGE)
    private String urlToImage;

    @ColumnInfo(name = COLUMN_URL)
    private String url;

    @ColumnInfo(name = COLUMN_CONTENT)
    private String content;

    @ColumnInfo(name = COLUMN_SOURCE)
    private String source;

    @ColumnInfo(name = COLUMN_PUBLISHED_AT)
    private String publishedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static synchronized News getInstance(){
        if(instance == null){
            instance = new News();
        }
        return instance;
    }
}
