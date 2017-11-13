package cn.com.hellowood.springsecurity.config;

import cn.com.hellowood.springsecurity.security.CustomAuthenticationFailureHandler;
import cn.com.hellowood.springsecurity.security.CustomAuthenticationProvider;
import cn.com.hellowood.springsecurity.security.CustomAuthenticationSuccessHandler;
import cn.com.hellowood.springsecurity.security.CustomLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

/**
 * The type Security config.
 *
 * @author HelloWood
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // This is permitted for all user
        http.authorizeRequests()
                .antMatchers("/", "/login", "/login-error", "/css/**", "/index")
                .permitAll();

        // Others url is need authenticate to access
        http.authorizeRequests()
                .anyRequest()
                .authenticated();

        http.authorizeRequests()
                .and()
                .rememberMe()
                .rememberMeServices(rememberMeServices("internalSecretKey"));


        //This config require login form action is '/login' and username and password parameter name is
        //'username' and 'password', and login fail url is 'login-error';
        // If successForwardurl and successHandler method is called here together, the success forward
        // url will not work, need to set forward url in success handler
        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successForwardUrl("/user/index")
                .successHandler(new CustomAuthenticationSuccessHandler())
                .failureUrl("/login-error")
                .failureHandler(new CustomAuthenticationFailureHandler());


        // The following configuration demonstrates how to enforce that only a single instance of a user is
        // authenticated at a time. If a user authenticates with the username "user" without logging out
        // and an attempt to authenticate with "user" is made the first session will be forcibly terminated
        // and sent to the "/login?expired" URL.
        http.sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/login")
                .sessionRegistry(sessionRegistry());

        // Logout handler for logout logic
        http.logout()
                .invalidateHttpSession(true)
                .logoutUrl("/logout")
                .addLogoutHandler(new CustomLogoutHandler());

        // Disables Security Cache Control for Cache static resource
        http.headers().cacheControl().disable();
    }

    private RememberMeServices rememberMeServices(String key) {
        InMemoryTokenRepositoryImpl rememberMeTokenRepository = new InMemoryTokenRepositoryImpl();
        PersistentTokenBasedRememberMeServices rememberMeServices =
                new PersistentTokenBasedRememberMeServices(key, userDetailsService(), rememberMeTokenRepository);
        rememberMeServices.setParameter("remember-me");
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
    }
}
