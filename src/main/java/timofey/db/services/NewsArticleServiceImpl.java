package timofey.db.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timofey.db.repositories.NewsArticleRepository;
import timofey.entities.NewsArticle;

import java.util.List;

@Service
public class NewsArticleServiceImpl implements INewsArticleService{

    NewsArticleRepository newsArticleRepository;
    @Autowired
    public NewsArticleServiceImpl(NewsArticleRepository newsArticleRepository){
        this.newsArticleRepository = newsArticleRepository;
    }

    @Override
    public NewsArticle findById(Long id) {
        return newsArticleRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<NewsArticle> findByTitle(String title) {
        return newsArticleRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<NewsArticle> findByTopic(String topic) {
        return newsArticleRepository.findByTopicIgnoreCase(topic);
    }

    @Override
    public NewsArticle findByLink(String link) {
        return newsArticleRepository.findByLink(link).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public NewsArticle save(NewsArticle article) {
        return newsArticleRepository.save(article);
    }

    @Override
    public List<NewsArticle> saveAll(List<NewsArticle> articleList) {
        return newsArticleRepository.saveAll(articleList);
    }
}
