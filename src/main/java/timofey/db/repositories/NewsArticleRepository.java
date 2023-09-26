package timofey.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import timofey.entities.NewsArticle;

import java.util.List;


@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    @Query
    List<NewsArticle> findByTitleContainingIgnoreCase(String title);

}
