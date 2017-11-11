package cn.com.hellowood.springsecurity.config;

import cn.com.hellowood.springsecurity.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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

        // This config require login form action is '/login' and username and password parameter name is
        // 'username' and 'password', and login fail url is 'login-error'
        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureUrl("/login-error");
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
