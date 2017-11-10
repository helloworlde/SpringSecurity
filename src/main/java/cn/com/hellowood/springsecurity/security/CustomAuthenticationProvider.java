package cn.com.hellowood.springsecurity.security;

import cn.com.hellowood.springsecurity.model.UserModel;
import cn.com.hellowood.springsecurity.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Custom authentication provider.
 *
 * @author hehuimin
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpSession session;

    @Autowired
    private UserService userService;

    /**
     * Validate user info is correct form database
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        UserModel user = userService.loadUserByUsernameAndPassword(username, password);
        if (user == null) {
            logger.error("{} login failed, username or password is wrong", username);
            throw new UsernameNotFoundException("Username or password wrong");
        }

        session.setAttribute("user", user);
        Authentication auth = new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
        return auth;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
