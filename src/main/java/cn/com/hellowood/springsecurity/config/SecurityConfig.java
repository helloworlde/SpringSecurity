package cn.com.hellowood.springsecurity.config;

import cn.com.hellowood.springsecurity.security.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import javax.sql.DataSource;

import static cn.com.hellowood.springsecurity.common.constant.CommonConstant.*;

/**
 * The type Security config.
 *
 * @author HelloWood
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // The DataSource is required for set JdbcRememberMeImpl
    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // This is permitted for all user
        http.authorizeRequests()
                .antMatchers(ROOT_URL, LOGIN_URL, LOGIN_ERROR_URL, CSS_WILDCARD_URL, INDEX_URL)
                .permitAll();

        // Role USER can access '/user/**' url
        http.authorizeRequests()
                .antMatchers(USER_WILDCARD_URL)
                .hasRole(USER);

        // Role ADMIN can access '/admin/**' url
        http.authorizeRequests()
                .antMatchers(ADMIN_WILDCARD_URL)
                .hasRole(ADMIN);

        // Others url is need authenticate to access
        http.authorizeRequests()
                .anyRequest()
                .authenticated();

        // If don't need save remember me authentication to database, 'http.authorizeRequests().and().rememberMe()'
        // is enough; if you want save authentication into database, need provide RememberMeService instance and
        // a key, the key must be same as RememberMeService instance key, or there will be fail when login by
        // remember me cookie.
        http.authorizeRequests()
                .and()
                .rememberMe()
                .rememberMeServices(rememberMeServices())
                .key(INTERNAL_SECRET_KEY);


        //This config require login form action is '/login' and username and password parameter name is
        //'username' and 'password', and login fail url is 'login-error';
        // If successForwardurl and successHandler method is called here together, the success forward
        // url will not work, need to set forward url in success handler. The failure handler is same.
        http.formLogin()
                .loginPage(LOGIN_URL)
                .loginProcessingUrl(LOGIN_URL)
                .usernameParameter(USERNAME)
                .passwordParameter(PASSWORD)
                .successForwardUrl(USER_INDEX_URL)
                .successHandler(new CustomAuthenticationSuccessHandler())
                .failureUrl(LOGIN_ERROR_URL)
                .failureHandler(new CustomAuthenticationFailureHandler());


        // The following configuration demonstrates how to enforce that only a single instance of a user is
        // authenticated at a time. If a user authenticates with the username "user" without logging out
        // and an attempt to authenticate with "user" is made the first session will be forcibly terminated
        // and sent to the "/login?expired" URL.
        http.sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl(LOGIN_URL)
                .sessionRegistry(sessionRegistry());

        // Logout handler for logout logic
        http.logout()
                .invalidateHttpSession(true)
                .logoutUrl(LOGOUT_URL)
                .addLogoutHandler(new CustomLogoutHandler());

        // Disables Security Cache Control for Cache static resource
        http.headers().cacheControl().disable();
    }


    /**
     * To provide a RememberMeService instance for RememberMe function
     * The @Bean annotation is not necessary
     *
     * @return the remember me services
     */
    @Bean
    public RememberMeServices rememberMeServices() {
        // There is need for DataSource if there is InMemoryTokenRepositoryImpl instance.
        // but JdbcTokenRepositoryImpl need DataSource to query RememberMe token for Database
        JdbcTokenRepositoryImpl rememberMeTokenRepository = new JdbcTokenRepositoryImpl();
        rememberMeTokenRepository.setDataSource(dataSource);

        // The INTERNAL_SECRET_KEY can be any String not null or blank, but must be same as the
        // key set in method configure(HttpSecurity http) after
        // rememberMeServices(RememberMeServices rememberMeServices)
        PersistentTokenBasedRememberMeServices rememberMeServices =
                new PersistentTokenBasedRememberMeServices(INTERNAL_SECRET_KEY, userDetailsService, rememberMeTokenRepository);

        // The parameter set is not necessary. default is "remember-me", if set, it must be same
        // as RememberMe checkbox parameter name in login page.
        rememberMeServices.setParameter(REMEMBER_ME);
        return rememberMeServices;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * Configure global.
     *
     * @param auth the auth
     * @throws Exception the exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
        try {
            auth.userDetailsService(userDetailsService);
        } catch (Exception e) {
            logger.error("Set userDetailService failed, {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
