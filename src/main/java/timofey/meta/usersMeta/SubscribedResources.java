package timofey.meta.usersMeta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import timofey.db.services.UserServiceImpl;

@Component
public class SubscribedResources {
    private UserServiceImpl userService;

    @Autowired
    public SubscribedResources(UserServiceImpl userService){
        this.userService = userService;

    }


}
