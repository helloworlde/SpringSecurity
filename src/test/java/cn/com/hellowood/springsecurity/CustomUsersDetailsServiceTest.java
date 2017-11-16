package cn.com.hellowood.springsecurity;

import cn.com.hellowood.springsecurity.security.CustomUserDetailsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.Assert.assertNotNull;

/**
 * The type User service test.
 */
public class CustomUsersDetailsServiceTest extends SpringSecurityApplicationTests {

    /**
     * The User service.
     */
    @Autowired
    CustomUserDetailsService userService;

    /**
     * Load user by username.
     */
    @Test
    public void loadUserByUsername() {
        UserDetails user = userService.loadUserByUsername("admin");
        assertNotNull("if user is not null, the method is correct", user);
    }

    /**
     * Load user by username and password.
     */
    @Test
    public void loadUserByUsernameAndPassword() {
        UserDetails user = userService.loadUserByUsernameAndPassword("admin", "admin");
        assertNotNull("if user is not null, the method is correct", user);
    }
}
