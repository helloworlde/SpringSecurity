# Spring Security 中实现 Remember Me 记住密码功能

> 在 Spring Boot 应用中使用 Spring Security 并实现 Remember Me 记住密码功能，实现自动登录 

---------------

> 前置条件：在 Spring Boot 应用中已正确配置 Spring Security

## 在页面添加记住密码的复选框
```
   <input type="checkbox" name="remember-me"/> Remember me
```

## 在 Security Config 配置文件中启用记住密码功能(验证信息存放在内存中)
```java
    
    import cn.com.hellowood.springsecurity.security.CustomAuthenticationProvider;
    import cn.com.hellowood.springsecurity.security.CustomUserDetailsService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdater;
    
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
        @Autowired
        private CustomAuthenticationProvider customAuthenticationProvider;
    
        @Autowired
        private CustomUserDetailsService userDetailsService;
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // 任何用户都可以访问以下URI
            http.authorizeRequests()
                    .antMatchers("/", "/login", "/login-error", "/css/**", "/index")
                    .permitAll();
    
            // 其他URI均需要权限校验
            http.authorizeRequests()
                    .anyRequest()
                    .authenticated();
    
            // 只需要以下配置即可启用记住密码
            http.authorizeRequests()
                    .and()
                    .rememberMe();
    
            http.formLogin()
                    .loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successForwardUrl("/user/index")
                    .failureUrl("/login-error");
        }
    
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) {
            // 为了使用用户名密码校验实现了AuthenticationProvider和UserDetailsService类
            auth.authenticationProvider(customAuthenticationProvider);
            try {
                auth.userDetailsService(userDetailsService);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

```

> 这样就可以使用记住密码了，选择记住密码登录后会在本地保存 Cookie，下次登录的时候通过 Cookie 校验用户信息；用户登录的信息保存在内存中，当内存断电或被清除之后该 Cookie 
即使在有效期内也无法登录。


---------------------

## 通过数据库存放校验信息实现记住密码登录

### 创建保存校验信息的表(表名和字段值必须为以下内容)
````
    CREATE TABLE persistent_logins (
      username  VARCHAR(64) NOT NULL,
      series    VARCHAR(64) NOT NULL PRIMARY KEY,
      token     VARCHAR(64) NOT NULL,
      last_used TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );
````
### 配置 Security Config
```java
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
    
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
        private final Logger logger = LoggerFactory.getLogger(getClass());
    
        @Autowired
        private CustomAuthenticationProvider customAuthenticationProvider;
    
        @Autowired
        private CustomUserDetailsService userDetailsService;
    
        // 数据源是为了JdbcRememberMeImpl实例而注入的，如果不设置数据源会在登陆的时候抛空指针异常
        @Autowired
        @Qualifier("dataSource")
        DataSource dataSource;
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // 任何用户都可以访问以下URI
            http.authorizeRequests()
                    .antMatchers("/", "/login", "/login-error", "/css/**", "/index")
                    .permitAll();
    
            // 其他URI需要权限验证
            http.authorizeRequests()
                    .anyRequest()
                    .authenticated();
    
            // 当通过JDBC方式记住密码时必须设置 key，key 可以为任意非空(null 或 "")字符串，但必须和 RememberMeService 构造参数的
            // key 一致，否则会导致通过记住密码登录失败
            http.authorizeRequests()
                    .and()
                    .rememberMe()
                    .rememberMeServices(rememberMeServices())
                    .key("INTERNAL_SECRET_KEY");
    
            // 当登录成功后会被重定向到 /user/index, 所以 loginPage 和 loginProcessingUrl 相同
            http.formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successForwardUrl("/user/index")
                    .failureUrl("/login-error");
        }
    
    
        /**
         * 返回 RememberMeServices 实例
         *
         * @return the remember me services
         */
        @Bean
        public RememberMeServices rememberMeServices() {
            JdbcTokenRepositoryImpl rememberMeTokenRepository = new JdbcTokenRepositoryImpl();
            // 此处需要设置数据源，否则无法从数据库查询验证信息
            rememberMeTokenRepository.setDataSource(dataSource);
    
            // 此处的 key 可以为任意非空值(null 或 "")，单必须和起前面
            // rememberMeServices(RememberMeServices rememberMeServices).key(key)的值相同
            PersistentTokenBasedRememberMeServices rememberMeServices =
                    new PersistentTokenBasedRememberMeServices("INTERNAL_SECRET_KEY", userDetailsService, rememberMeTokenRepository);
    
            // 该参数不是必须的，默认值为 "remember-me", 但如果设置必须和页面复选框的 name 一致
            rememberMeServices.setParameter("remember-me");
            return rememberMeServices;
        }
        
        /**
         * Configure global.
         *
         * @param auth the auth
         * @throws Exception the exception
         */
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) {
            // 为了使用用户名密码校验实现了AuthenticationProvider和UserDetailsService类
            auth.authenticationProvider(customAuthenticationProvider);
            try {
                auth.userDetailsService(userDetailsService);
            } catch (Exception e) {
                logger.error("Set userDetailService failed, {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }

```
> 这样就可以实现将校验信息保存在数据库中，下次通过记住密码登录后再次访问页面会根据 Cookie 查找该用户的登录信息，如果登录信息有效则登录成功，
否则重定向到登录页面

---------------------

- 自定义实现 AuthenticationProvider 接口

```java

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.stereotype.Component;
    
    import java.util.ArrayList;
    import java.util.List;
    
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
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    
            logger.info("start validate user {} login", username);
            // 通过用户名和密码校验，如果校验不通过会抛出 AuthenticationException
            userDetailsService.loadUserByUsernameAndPassword(username, password);
            Authentication auth = new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
            return auth;
        }
    
    
        @Override
        public boolean supports(Class<?> authentication) {
            return authentication.equals(UsernamePasswordAuthenticationToken.class);
        }
    
    }

```

- 自定义实现 UserDetailsService 接口
```java

    import cn.com.hellowood.springsecurity.mapper.UserMapper;
    import cn.com.hellowood.springsecurity.model.UserModel;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.authentication.AccountExpiredException;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;
    
    import javax.servlet.http.HttpSession;
    import java.util.ArrayList;
    
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
        private HttpSession session;
    
        /**
         * 通过用户名和密码加载用户信息并校验
         *
         * @param username the username
         * @param password the password
         * @return the user model
         * @throws AuthenticationException the authentication exception
         */
        public UserModel loadUserByUsernameAndPassword(String username, String password) throws AuthenticationException {
            logger.info("user {} is login by username and password", username);
            UserModel user = userMapper.getUserByUsernameAndPassword(username, password);
            validateUser(username, user);
            return user;
        }
    
    
        /**
        * 通过用户名加载用户信息，重写该方法用于记住密码后通过 Cookie 登录
        * 
        * @param username
        * @param user
        */
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            logger.info("user {} is login by remember me cookie", username);
            UserModel user = userMapper.getUserByUsername(username);
            validateUser(username, user);
            return new User(user.getUsername(), user.getPassword(), new ArrayList<GrantedAuthority>());
        }
    
        /**
         * 校验用户信息并将用户信息放在 Session 中
         *
         * @param username
         * @param user
         */
        private void validateUser(String username, UserModel user) {
            if (user == null) {
                logger.error("user {} login failed, username or password is wrong", username);
                throw new BadCredentialsException("Username or password is not correct");
            } else if (!user.getEnabled()) {
                logger.error("user {} login failed, this account had expired", username);
                throw new AccountExpiredException("Account had expired");
            }
            // TODO There should add more logic to determine locked, expired and others status
    
            logger.info("user {} login success", username);
            // 当用户信息有效时放入 Session 中
            session.setAttribute("user", user);
        }
    }

```