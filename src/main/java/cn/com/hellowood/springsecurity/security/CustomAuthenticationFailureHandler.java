package cn.com.hellowood.springsecurity.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        String message = "";
        if (exception instanceof LockedException) {
            message = exception.getMessage();
            logger.info("The account is locked");
        } else if (exception instanceof AccountExpiredException) {
            message = exception.getMessage();
            logger.info("The account is expired");
        } else if (exception instanceof SessionAuthenticationException) {
            message = exception.getMessage();
            logger.info("The current session is not valid");
        } else if (exception instanceof BadCredentialsException) {
            message = exception.getMessage();
            logger.info("The username and password is not match");
        } else {
            message = exception.getMessage();
            logger.info("Login failed, exception is {}", exception.getMessage());
        }
        //TODO here can add custom exception handler in future

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }
}
