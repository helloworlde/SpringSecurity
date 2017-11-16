package cn.com.hellowood.springsecurity.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * The type Custom authentication provider.
 *
 * @author HelloWood
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CustomUserDetailsService userDetailsService;

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

        // Check username and password is correct, if login is invalid, will throw AuthenticationException
        UserDetails userDetails = userDetailsService.loadUserByUsernameAndPassword(username, password);
        Authentication auth = new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        return auth;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
