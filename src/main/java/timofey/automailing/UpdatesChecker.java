package timofey.automailing;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import timofey.config.SourceInit;
import timofey.db.services.NewsArticleServiceImpl;
import timofey.db.services.UserServiceImpl;
import timofey.entities.NewsArticle;
import timofey.entities.User;
import java.util.List;
import java.util.Map;

/**
 * класс для проверки обновления по новостям
 * Просто создал класс, нужно вынести в отдельный сервис:
 * Должно быть отдельное приложение, которое стучится на сервер с новостями
 */
@Component
public class UpdatesChecker {
    @Autowired
    SourceInit sources;
    @Autowired
    private NewsArticleServiceImpl newsArticleService;
    @Autowired
    private UserServiceImpl userService;
    private List<User> userList;
    private NewsArticle newsArticle;
    private Map<String, String> sourcesMap;

    @PostConstruct
    private void init(){
        if(newsArticleService != null && userService != null && sources != null){
            userList = userService.findAll();
            newsArticle = newsArticleService.findById(80L);
            sourcesMap = sources.getResourceMap();
        }
    }
//    public SendMessage check(){
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(userList.get(0).getChatId());
//        sendMessage.setText(newsArticle.toString());
//        sendMessage.setReplyMarkup(null);
//        System.out.println("UPDATED INFO FROM DB");
//
//        return sendMessage;
//    }
}
