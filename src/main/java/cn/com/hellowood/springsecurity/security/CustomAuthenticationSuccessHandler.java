package cn.com.hellowood.springsecurity.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * If add custom authentication success handler, you should extends class AbstractAuthenticationTargetUrlRequestHandler
 * to set default success url, or there will not redirect to any url. the default target url is '/'
 *
 * @author HelloWood
 */
public class CustomAuthenticationSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // set redirect url when login success
        setDefaultTargetUrl("/user/index");
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    /**
     * Remove security attributes in session
     *
     * @param request
     */
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
