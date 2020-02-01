package edu.nju.mall.service;

import edu.nju.mall.dto.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(value = {"dev", "devConn"})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void test() {
        UserDTO user = UserDTO.builder()
                .openid("XXX")
                .nickname("test")
                .avatarUrl("http://test.com")
                .city("nanjing")
                .country("cn")
                .gender(0)
                .province("jiangsu")
                .sessionKey("session")
                .build();
        user = userService.register(user);
        System.out.println(user.toString());
    }
}
