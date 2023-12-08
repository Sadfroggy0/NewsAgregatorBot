package timofey.meta.usersMeta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import timofey.db.services.ResourceServiceImpl;
import timofey.db.services.UserServiceImpl;
import timofey.entities.Resource;
import timofey.entities.User;

import java.util.*;

/**
 * класс для счета подписок каждого пользователя
 * при запуске приложения bean получает данные из DB
 * эти данные потом нужно отображать в клавиатуре при выборе подписки
 * subscribed/not subscribed
 * подумать о том, чтобы передавать это в кэш
 */
@Component
public class SubscribedResources {
    private ResourceServiceImpl resourceService;
    private UserServiceImpl userService;
    private List<Resource> resourceList;
    private Map<Long, List<String>> usersResourcesMap;

    @Autowired
    public SubscribedResources(ResourceServiceImpl resourceService, UserServiceImpl userService){
        this.resourceService = resourceService;
        this.userService = userService;
        usersResourcesMap = new HashMap<>();
        List<User> userList = userService.findAll();
        for (User user : userList) {
            usersResourcesMap.put(user.getChatId(), user.getResources().stream().map(Resource::getName).toList());
        }
    }

    public List<String> getUserResourcesNamesByChatId(Long chatId) {
        if (!usersResourcesMap.containsKey(chatId)) {
            User user = userService.findByChatId(chatId);
            if (user != null)
                usersResourcesMap.put(chatId, user.getResources().stream().map(Resource::getName).toList());
            else
                usersResourcesMap.put(chatId, Collections.emptyList());
        }
        return usersResourcesMap.get(chatId);
    }

    public void subscribeUserToResourceByChatIdAndResourceName(Long chatId, String resourceName) {
        User user = userService.findByChatId(chatId);
        Resource resource = resourceService.findByName(resourceName);
        if (user != null && resource != null) {
            List<Resource> userResources = user.getResources();
            if (!userResources.contains(resource)) {
                userResources.add(resource);
                userService.save(user);
            }
            usersResourcesMap.put(chatId, user.getResources().stream().map(Resource::getName).toList());
        }
    }

    public void unsubscribeUserFromResourceByChatIdAndResourceName(Long chatId, String resourceName) {
        User user = userService.findByChatId(chatId);
        Resource resource = resourceService.findByName(resourceName);
        if (user != null && resource != null) {
            Long resourceId = resource.getId();
            user.getResources().removeIf(userResource -> userResource.getId().equals(resourceId));
            userService.save(user);
            usersResourcesMap.put(chatId, user.getResources().stream().map(Resource::getName).toList());
        }
    }

}
