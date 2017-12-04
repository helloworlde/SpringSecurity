package cn.com.hellowood.springsecurity.security;

import cn.com.hellowood.springsecurity.mapper.MenuMapper;
import cn.com.hellowood.springsecurity.mapper.RoleMapper;
import cn.com.hellowood.springsecurity.mapper.UserMapper;
import cn.com.hellowood.springsecurity.model.MenuModel;
import cn.com.hellowood.springsecurity.model.RoleModel;
import cn.com.hellowood.springsecurity.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static cn.com.hellowood.springsecurity.common.constant.CommonConstant.AUTHENTICATION_USER;
import static cn.com.hellowood.springsecurity.common.constant.CommonConstant.LOGIN_USER;

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
    private RoleMapper roleMapper;

    @Autowired
    private MenuMapper menuDao;

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
    public UserDetails loadUserByUsernameAndPassword(String username, String password) throws AuthenticationException {

        UserModel user = loadAndValidateUser(username);

        // Validate password
        if (!password.equals(user.getPassword())) {
            logger.error("user {} login failed, the password is not correct", username);
            throw new BadCredentialsException("Bad credentials");
        }

        UserDetails userDetails = handlerUserDetails(user);

        return userDetails;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        UserModel user = loadAndValidateUser(username);
        UserDetails userDetails = handlerUserDetails(user);
        return userDetails;
    }

    /**
     * Handler user details user details.
     *
     * @param user the user
     * @return the user details
     */
    private UserDetails handlerUserDetails(UserModel user) {

        // Load user role
        List<GrantedAuthority> authorityList = new ArrayList<>();
        RoleModel role = userMapper.getRoleByUserId(user.getId());
        user.setRole(role);

        // Load user menus
        List<MenuModel> menus = menuDao.getMenuByRoleId(role.getId());
        user.setMenus(menus);

        authorityList.add(new SimpleGrantedAuthority(role.getValue()));
        UserDetails userDetails = new User(user.getUsername(), user.getPassword(), authorityList);

        // Put user info to session
        session.setAttribute(AUTHENTICATION_USER, userDetails);
        session.setAttribute(LOGIN_USER, user);
        logger.info("user [{}] login success, the role is {}", user.getUsername(), authorityList.toString());

        return userDetails;
    }

    /**
     * Load and validate user user model.
     *
     * @param username the username
     * @return the user model
     */
    private UserModel loadAndValidateUser(String username) {
        UserModel user = userMapper.getUserByUsername(username);
        if (user == null) {
            logger.error("user [{}] login failed, the account not exist", username);
            throw new UsernameNotFoundException("Account not exist");
        } else if (!user.isEnabled()) {
            logger.error("user [{}] login failed, the account is disabled", username);
            throw new DisabledException("Account is disabled");
        } else if (user.isExpired()) {
            logger.error("user [{}] login failed, the account is expired", username);
            throw new AccountExpiredException("Account is expired");
        } else if (user.isLocked()) {
            throw new LockedException("Account is locked");
        }
        return user;
    }
}
