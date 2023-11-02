package timofey.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import timofey.entities.NewsArticle;

import java.util.List;
import java.util.Optional;


@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    @Query
    List<NewsArticle> findByTitleContainingIgnoreCase(String title);

    List<NewsArticle> findByTopicIgnoreCase(String topic);

    Optional<NewsArticle> findByLink(String link);
}
