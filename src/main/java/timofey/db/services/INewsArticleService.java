package timofey.db.services;

import timofey.entities.NewsArticle;

import java.util.List;

public interface INewsArticleService {
    NewsArticle findById(Long id);
    List<NewsArticle> findByTitle(String title);

    NewsArticle save(NewsArticle article);
    List<NewsArticle> saveAll(List<NewsArticle> articleList);

}
