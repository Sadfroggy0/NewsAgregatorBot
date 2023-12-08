package timofey.db.services;

import org.springframework.stereotype.Service;
import timofey.entities.Resource;
import timofey.entities.User;

import java.util.List;

public interface IUserService {

    List<User> findAll();
    User findByTelegramId(Long telegramId);
    User findByChatId(Long chatId);
    User findById(Long id);
    List<User> findUserSubscribedToResource(Long resourceId);
    void save(User user);
    void delete(User user);
    void deleteByTelegramId(Long telegramId);
    void subscribeUserToSource(int userId, int resourceId);




}
