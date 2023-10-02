package timofey.db.repositories;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import timofey.entities.Resource;
import timofey.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByTelegramUserId(Long id);
    @Query("SELECT r.id FROM User u JOIN u.resources r WHERE u.id = :userId")
    List<Resource> findResourcesByUserId(@Param("userId") Long id);



}
