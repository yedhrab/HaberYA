# 🐣 API Kullanımı

## 📰 News API

### 📝 Resmi Dokümanlar

- [📃 API dokümanı](https://newsapi.org/docs)
- [🔥 Top Headlines dokümanı](https://newsapi.org/docs/endpoints/top-headlines)

### 🧱 Temel Kullanım

```java
// Özellik tanımlama ile kullanma
NewsAPIOptions options = NewsAPIOptions.Builder()
    .setCountry(NewsAPIOptions.Country.US)
    .setCategory(NewsAPIOptions.Category.BUSINESS)
    .build();

NewsAPI.requestTopHeadlines(this, this::saveToDB, options);

// Varsayılan kullanım (Country = TR)
NewsAPI.requestTopHeadlines(this, this::saveToDB, null);

// Yeni içerik yapıları (options destekler)
NewsAPI.requestEverything(this, this::saveToDB, null);
NewsAPI.requestSources(this, this::saveToDB, null);
```

### 🔍 Haberlerde Arama

```java
NewsAPIOptions options = NewsAPIOptions.Builder()
    .setCountry(NewsAPIOptions.Country.US)
    .setCategory(NewsAPIOptions.Category.BUSINESS)
    .setQuery("Aranan metin")
    .build();
```
