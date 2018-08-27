package cn.com.hellowood.springsecurity;

import cn.com.hellowood.springsecurity.model.UserModel;
import cn.com.hellowood.springsecurity.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

/**
 * The type User service test.
 */
public class CustomUsersDetailsServiceTest extends SpringSecurityApplicationTests {

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
        UserModel user = userService.loadUserByUsername("admin");
        assertNotNull("if user is not null, the method is correct", user);
    }

    /**
     * Load user by username and password.
     */
    @Test
    public void loadUserByUsernameAndPassword() {
        UserModel user = userService.loadUserByUsernameAndPassword("admin", "admin");
        assertNotNull("if user is not null, the method is correct", user);
    }
}
