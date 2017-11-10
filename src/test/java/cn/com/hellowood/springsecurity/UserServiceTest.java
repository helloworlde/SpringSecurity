package cn.com.hellowood.springsecurity;

import cn.com.hellowood.springsecurity.model.UserModel;
import cn.com.hellowood.springsecurity.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

/**
 * The type User service test.
 */
public class UserServiceTest extends SpringSecurityApplicationTests {

    /**
     * The User service.
     */
    @Autowired
    UserService userService;

    /**
     * Load user by username.
     */
    @Test
    public void loadUserByUsername() {
        UserModel userModel = userService.loadUserByUsername("admin");
        assertNotNull("if userModel is not null, the method is correct", userModel);
    }

    /**
     * Load user by username and password.
     */
    @Test
    public void loadUserByUsernameAndPassword() {
        UserModel userModel = userService.loadUserByUsernameAndPassword("admin", "admin");
        assertNotNull("if userModel is not null, the method is correct", userModel);
    }
}
