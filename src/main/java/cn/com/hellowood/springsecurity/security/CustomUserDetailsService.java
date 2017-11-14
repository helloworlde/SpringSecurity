package cn.com.hellowood.springsecurity.security;

import cn.com.hellowood.springsecurity.mapper.UserMapper;
import cn.com.hellowood.springsecurity.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import static cn.com.hellowood.springsecurity.common.constant.CommonConstant.USER;

/**
 * The type Custom user details service.
 *
 * @author HelloWood
 */
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HttpSession session;

    /**
     * Load user by username and password.
     *
     * @param username the username
     * @param password the password
     * @return the user model
     * @throws AuthenticationException the authentication exception
     */
    public UserModel loadUserByUsernameAndPassword(String username, String password) throws AuthenticationException {
        logger.info("user {} is login by username and password", username);
        UserModel user = userMapper.getUserByUsernameAndPassword(username, password);
        validateUser(username, user);
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("user {} is login by remember me cookie", username);
        UserModel user = userMapper.getUserByUsername(username);
        validateUser(username, user);
        return new User(user.getUsername(), user.getPassword(), new ArrayList<GrantedAuthority>());
    }

    /**
     * Validate user and handler permission and parameter
     *
     * @param username
     * @param user
     */
    private void validateUser(String username, UserModel user) {
        if (user == null) {
            logger.error("user {} login failed, username or password is wrong", username);
            throw new BadCredentialsException("Username or password is not correct");
        } else if (!user.getEnabled()) {
            logger.error("user {} login failed, this account had expired", username);
            throw new AccountExpiredException("Account had expired");
        }
        // TODO There should add more logic to determine locked, expired and others status

        logger.info("user {} login success", username);
        // If user is valid put user info to session
        session.setAttribute(USER, user);
    }
}
