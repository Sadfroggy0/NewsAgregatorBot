package timofey.db.services;

import org.springframework.stereotype.Service;
import timofey.entities.User;

import java.util.List;

public interface IUserService {

    List<User> findAll();
    User findByTelegramId(Long telegramId);
    User findById(Long id);
    void save(User user);
    void delete(User user);
    void deleteByTelegramId(Long telegramId );



}
