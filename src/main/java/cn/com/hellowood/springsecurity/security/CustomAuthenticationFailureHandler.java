package cn.com.hellowood.springsecurity.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static cn.com.hellowood.springsecurity.common.constant.CommonConstant.LOGIN_ERROR_MSG;
import static cn.com.hellowood.springsecurity.common.constant.CommonConstant.LOGIN_ERROR_URL;

/**
 * The type Custom authentication failure handler.
 *
 * @author HelloWood
 */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        // return failure message
        String message = exception.getMessage();

        // Set error message into session, for view in error page
        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_ERROR_MSG, message);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.sendRedirect(LOGIN_ERROR_URL);
    }
}
