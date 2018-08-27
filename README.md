# Spring Boot with Spring Security

> Basic simple use Spring Security in Spring Boot application, Validate user from database with MyBatis, the page use Thymeleaf

-----------------------

[中文文档](https://github.com/helloworlde/SpringSecurity/blob/dev/README-zh.md)

[![Build Status](https://travis-ci.org/helloworlde/SpringSecurity.svg?branch=dev)](https://travis-ci.org/helloworlde/SpringSecurity)
[![CircleCI](https://circleci.com/gh/helloworlde/SpringSecurity/tree/dev.svg?style=svg)](https://circleci.com/gh/helloworlde/SpringSecurity/tree/dev)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a88bd270d44e46aabd9696d056d4de50)](https://www.codacy.com/app/helloworlde/SpringSecurity?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=helloworlde/SpringSecurity&amp;utm_campaign=Badge_Grade)
[![Coverage Status](https://coveralls.io/repos/github/helloworlde/SpringSecurity/badge.svg)](https://coveralls.io/github/helloworlde/SpringSecurity)
[![codecov](https://codecov.io/gh/helloworlde/SpringSecurity/branch/master/graph/badge.svg)](https://codecov.io/gh/helloworlde/SpringSecurity)
## Create Spring Boot Application

## Add Spring Security Dependence
````groovy
    compile('org.springframework.boot:spring-boot-starter-security')
    compile('org.springframework.boot:spring-boot-starter-web')
    compile('org.mybatis.spring.boot:mybatis-spring-boot-starter:1.3.0')
    compile('org.springframework.boot:spring-boot-starter-thymeleaf')
    runtime('mysql:mysql-connector-java')
    runtime('org.springframework.boot:spring-boot-starter-tomcat')
    testCompile('org.springframework.boot:spring-boot-starter-test')
    testCompile('org.springframework.security:spring-security-test')    
````

## Create table for user and insert data
````mysql
    CREATE TABLE user (
      id       INT                  AUTO_INCREMENT PRIMARY KEY,
      username VARCHAR(45) NOT NULL,
      password VARCHAR(45) NOT NULL,
      enabled  INT         NOT NULL DEFAULT 1
    );
    
    INSERT INTO user (username, password, enabled) VALUES ('uesrname', 'password', TRUE);
````
## Configure application 
````properties
    spring.datasource.url=jdbc:mysql://localhost:3306/security?useSSL=false
    spring.datasource.username=security
    spring.datasource.password=security
    spring.datasource.driver-class-name=com.mysql.jdbc.Driver
    mybatis.type-aliases-package=cn.com.hellowood.springsecurity.mapper
    mybatis.mapper-locations=mappers/**Mapper.xml
````

## Add Security Config 
````java
    
    import cn.com.hellowood.springsecurity.security.CustomAuthenticationProvider;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    
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
            // Use custom Authentication Provider
            auth.authenticationProvider(customAuthenticationProvider);
        }
    }

````

## Add Custom Authentication Provider class
````java
    import cn.com.hellowood.springsecurity.model.UserModel;
    import cn.com.hellowood.springsecurity.service.UserService;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.authentication.AccountExpiredException;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.stereotype.Component;
    
    import javax.servlet.http.HttpSession;
    import java.util.ArrayList;
    import java.util.List;
    
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
    
            // Check username and password is correct
            UserModel user = userService.loadUserByUsernameAndPassword(username, password);
            if (user == null) {
                logger.error("{} login failed, username or password is wrong", username);
                throw new BadCredentialsException("Username or password is not correct");
            } else if (!user.getEnabled()) {
                throw new AccountExpiredException("Account had expired");
            }
    
            // If user is valid put user info to session
            session.setAttribute("user", user);
            Authentication auth = new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
            return auth;
        }
    
    
        @Override
        public boolean supports(Class<?> authentication) {
            return authentication.equals(UsernamePasswordAuthenticationToken.class);
        }
    
    }
````

## Add Service for load user info 

- Add UserModel.java
````java

    public class UserModel {
    
        private Integer id;
    
        private String username;
    
        private String password;
    
        private Boolean enabled;
    
        /**
         * Instantiates a new User model.
         */
        public UserModel() {
        }
    
        /**
         * Instantiates a new User model.
         *
         * @param id       the id
         * @param username the username
         * @param password the password
         * @param enabled  the enabled
         */
        public UserModel(Integer id, String username, String password, Boolean enabled) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.enabled = enabled;
        }
    
        /**
         * Gets id.
         *
         * @return the id
         */
        public Integer getId() {
            return id;
        }
    
        /**
         * Sets id.
         *
         * @param id the id
         */
        public void setId(Integer id) {
            this.id = id;
        }
    
        /**
         * Gets username.
         *
         * @return the username
         */
        public String getUsername() {
            return username;
        }
    
        /**
         * Sets username.
         *
         * @param username the username
         */
        public void setUsername(String username) {
            this.username = username;
        }
    
        /**
         * Gets password.
         *
         * @return the password
         */
        public String getPassword() {
            return password;
        }
    
        /**
         * Sets password.
         *
         * @param password the password
         */
        public void setPassword(String password) {
            this.password = password;
        }
    
        /**
         * Gets enabled.
         *
         * @return the enabled
         */
        public Boolean getEnabled() {
            return enabled;
        }
    
        /**
         * Sets enabled.
         *
         * @param enabled the enabled
         */
        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    
        @Override
        public String toString() {
            return "UserModel{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", enabled=" + enabled +
                    '}';
        }
    }
````
- Add UserService.java
````java

    import cn.com.hellowood.springsecurity.mapper.UserMapper;
    import cn.com.hellowood.springsecurity.model.UserModel;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    
    @Service("userService")
    public class UserService {
    
        @Autowired
        private UserMapper userMapper;
    
        /**
         * Load user by username and password user model.
         *
         * @param username the username
         * @param password the password
         * @return the user model
         */
        public UserModel loadUserByUsernameAndPassword(String username, String password) {
            return userMapper.getUserByUsernameAndPassword(username, password);
        }
    }
````
- Add UserMapper.java
````java
    import cn.com.hellowood.springsecurity.model.UserModel;
    import org.apache.ibatis.annotations.Mapper;
    import org.apache.ibatis.annotations.Param;
    
    @Mapper
    public interface UserMapper {
    
        /**
         * Gets user by username and password.
         *
         * @param username the username
         * @param password the password
         * @return the user by username and password
         */
        UserModel getUserByUsernameAndPassword(@Param("username") String username,
                                               @Param("password") String password);
    }
````
- Add UserMapper.xml 
````xml
    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
    <mapper namespace="cn.com.hellowood.springsecurity.mapper.UserMapper">
    
        <resultMap id="baseResultMap" type="cn.com.hellowood.springsecurity.model.UserModel">
            <id column="id" property="id" javaType="java.lang.Integer" jdbcType="INTEGER"></id>
            <result column="username" property="username" javaType="java.lang.String" jdbcType="VARCHAR"></result>
            <result column="password" property="password" javaType="java.lang.String" jdbcType="VARCHAR"></result>
            <result column="enabled" property="enabled" javaType="java.lang.Boolean" jdbcType="INTEGER"></result>
        </resultMap>
    
        <select id="getUserByUsernameAndPassword" resultType="cn.com.hellowood.springsecurity.model.UserModel">
            SELECT
                id,
                username,
                password,
                enabled
            FROM user
            WHERE username = #{username, jdbcType=VARCHAR}
                  AND password = #{password, jdbcType=VARCHAR}
        </select>
    </mapper>
````
## Add Page file(User Thymeleaf as template)
- index.html
````html
    <!DOCTYPE html>
    <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
    <head>
        <title>Spring Security</title>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
        <link rel="stylesheet" href="/css/main.css" th:href="@{/css/main.css}"/>
        <link rel="stylesheet" href="/css/bootstrap.min.css" th:href="@{/css/bootstrap.min.css}"/>
    </head>
    <body>
    <div class="container">
        <form action="#" class="form-signin">
            <h2 class="form-signin-heading">Hello Spring Security</h2>
            <h5 class="form-signin-heading content-adjust">Anyone can access this page</h5>
            <div th:if="${session.user} != null">
                <h5 class="form-signin-heading content-adjust">Your username is <span th:text="${session.user.username}"></span></h5>
                <a href="/user/index" th:href="@{/user/index}" class="btn btn-success btn-block">To Security page</a>
            </div>
            <div th:if="${session.user} == null">
                <a href="/index" th:href="@{/login}" class="btn btn-primary btn-block">To Login page</a>
            </div>
        </form>
        <div th:fragment="logout" class="logout" th:if="${session.user} != null">
            <form action="#" th:action="@{/logout}" method="post" class="form-signin">
                <button class="btn btn-warning btn-block" type="submit">Log out</button>
            </form>
        </div>
    </div>
    </body>
    </html>
````
- login.html 
````html
    <!DOCTYPE html>
    <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
    <head>
        <title>Login page</title>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
        <link rel="stylesheet" href="/css/main.css" th:href="@{/css/main.css}"/>
        <link rel="stylesheet" href="/css/bootstrap.min.css" th:href="@{/css/bootstrap.min.css}"/>
    </head>
    <body>
    <div class="container">
        <form th:action="@{/login}" method="post" class="form-signin">
            <h2 class="form-signin-heading">Please sign in</h2>
            <div>
                <label for="username" class="sr-only">Username</label>
                <input type="text" id="username" name="username"
                       th:class="${loginError} ? 'form-control is-invalid' : 'form-control'" placeholder="Username"
                       required="required"
                       autofocus="autofocus"/>
                <div class="invalid-feedback" th:if="${loginError}">
                    Wrong username or password
                </div>
            </div>
            <div>
                <label for="password" class="sr-only">Password</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="Password"
                       required="required"/>
            </div>
            <button class="btn btn-success btn-block" type="submit">Sign in</button>
            <a href="/index" th:href="@{/index}" class="btn btn-primary btn-block">Back to Home page</a>
        </form>
    </div>
    </body>
    </html>
````
- user/index.html
````html
    <!DOCTYPE html>
    <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
    <head>
        <title>Spring Security</title>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
        <link rel="stylesheet" href="/css/main.css" th:href="@{/css/main.css}"/>
        <link rel="stylesheet" href="/css/bootstrap.min.css" th:href="@{/css/bootstrap.min.css}"/>
    </head>
    <body>
    <div class="container">
        <form action="#" class="form-signin">
            <h2 class="form-signin-heading">Hello Spring Security</h2>
            <h5 class="form-signin-heading content-adjust">Only logged in user can access this page</h5>
            <div th:if="${session.user} != null">
                <h5 class="form-signin-heading content-adjust">Logged user is <span th:text="${session.user.username}"></span></h5>
                <a href="/index" th:href="@{/index}" class="btn btn-primary btn-block">Back to Home page</a>
            </div>
        </form>
        <div th:substituteby="index::logout"></div>
    </div>
    </body>
    </html>
````

## Add Controller
````java
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.RequestMapping;
    
    @Controller
    public class MainController {
    
        /**
         * Root page.
         *
         * @return the index page url
         */
        @RequestMapping("/")
        public String root() {
            return "redirect:/index";
        }
    
        /**
         * Index page.
         *
         * @return the index page url
         */
        @RequestMapping("/index")
        public String index() {
            return "index";
        }
    
        /**
         * User index page.
         *
         * @return the user index page url
         */
        @RequestMapping("/user/index")
        public String userIndex() {
            return "user/index";
        }
    
        /**
         * Login page.
         *
         * @return the login page url
         */
        @RequestMapping("/login")
        public String login() {
            return "login";
        }
    
        /**
         * Login error page.
         *
         * @param model the model
         * @return the login error page url
         */
        @RequestMapping("/login-error")
        public String loginError(Model model) {
            model.addAttribute("loginError", true);
            return "login";
        }
    
    }
````
----------------

> Now, we can start up the application, then access in [http://localhost:8080/user/index](http://localhost:8080/user/index),
  will redirect to [http://localhost:8080/login](http://localhost:8080/login), then input username `username` and
  password `password`; After logged we can access [http://localhost:8080/user/index](http://localhost:8080/user/index)
  correct; If input wrong username or password, there will prompt error message, means security worked.
