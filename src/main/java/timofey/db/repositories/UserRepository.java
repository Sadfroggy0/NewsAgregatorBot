package timofey.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timofey.entities.User;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

//    @Query("SELECT u FROM User u WHERE u.telegramUserId = :telegramUserId")
    Optional<User> findByTelegramUserId(Long aLong);
}
