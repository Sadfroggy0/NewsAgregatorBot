import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import timofey.ApplicationRunner;
import timofey.db.services.UserServiceImpl;
import timofey.entities.User;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRunner.class)
public class DataBaseUserTest {
    @Autowired
    private UserServiceImpl userService;

    @Test
    public void findUserTest(){
        User user = userService.findByUserName("timofey_panov");
        Assert.assertEquals(User.class, user.getClass());

    }
    @Test
    public void findAllUsersTest(){
        List<User> userList = userService.findAll();
        Assert.assertTrue(userList.size() > 0);
    }
}
