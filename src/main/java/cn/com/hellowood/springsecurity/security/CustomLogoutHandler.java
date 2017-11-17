package cn.com.hellowood.springsecurity.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static cn.com.hellowood.springsecurity.common.constant.CommonConstant.LOGIN_USER;

/**
 * The type Custom logout handler.
 *
 * @author HelloWood
 */
public class CustomLogoutHandler implements LogoutHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // Clear information in session when the use logout
        if (authentication != null && authentication.getPrincipal() != null) {
            UserDetails user = (User) authentication.getDetails();
            String username = user.getUsername();
            HttpSession session = request.getSession();
            if (session.getAttribute(LOGIN_USER) != null) {
                session.removeAttribute(LOGIN_USER);
                session.invalidate();
                logger.info("user [{}] log out, the session [{}] is set as invalid", username, session.getId());
            }
        }
    }
}
