package timofey.db.repositories;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import timofey.entities.NewsArticle;
import timofey.entities.Resource;
import timofey.entities.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Optional<Resource> findByNameContainingIgnoreCase(String Name);

    Optional<Resource> findByUrl(String url);

    @Query("SELECT r.id FROM User u JOIN u.resources r WHERE u.id = :userId")
    List<Resource> findResourcesByUserId(@Param("userId") Long id);
}
