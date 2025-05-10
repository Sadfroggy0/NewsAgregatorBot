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

    @Query("SELECT u.id FROM Resource r JOIN r.users u WHERE r.id = :resourceId")
    List<User> findUsersByResourceId(@Param("resourceId") Long id);

    Optional<User> findByChatId(Long chatId);

    Optional<User> findByUserName(String user_name);

}
