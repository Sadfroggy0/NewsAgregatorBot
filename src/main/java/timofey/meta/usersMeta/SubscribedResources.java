package timofey.meta.usersMeta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import timofey.db.services.UserServiceImpl;
import timofey.entities.Resource;

import java.util.List;

@Component
public class SubscribedResources {
    private UserServiceImpl userService;
    private List<Resource> resourceList;

    @Autowired
    public SubscribedResources(UserServiceImpl userService){
        this.userService = userService;
        if(userService != null){
            this.resourceList = userService.getAllSubscriptions();
        }

    }



}
