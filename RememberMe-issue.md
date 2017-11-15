> 在使用 Spring Security 的 Remember Me 记住密码功能时遇到的问题和解决方法

--------------

## java.lang.IllegalStateException: UserDetailsService is required.
- 配置信息(`Security.java`)
```

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }
   
   //...
   
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(ROOT_URL, LOGIN_URL, LOGIN_ERROR_URL, CSS_WILDCARD_URL, INDEX_URL)
                .permitAll();

        http.authorizeRequests()
                .anyRequest()
                .authenticated();

        http.authorizeRequests()
                .and()
                .rememberMe()
                .rememberMeServices(rememberMeServices())
                .key(INTERNAL_SECRET_KEY);

        // ...
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        JdbcTokenRepositoryImpl rememberMeTokenRepository = new JdbcTokenRepositoryImpl();
        rememberMeTokenRepository.setDataSource(dataSource);

        PersistentTokenBasedRememberMeServices rememberMeServices =
                new PersistentTokenBasedRememberMeServices(INTERNAL_SECRET_KEY, userDetailsService(), rememberMeTokenRepository);

        rememberMeServices.setParameter(REMEMBER_ME);
        return rememberMeServices;
    }
    

```
> 错误信息如下，发生该错误的原因是因为没有提供 UserDetailsService 的实例而出错，虽然调用了 `userDetailsService()` 方法，
但实际上并没有起作用，所以需要提供自定义的 `UserDetailsService` 实例注入

```

java.lang.IllegalStateException: UserDetailsService is required.
	at org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter$UserDetailsServiceDelegator.loadUserByUsername(WebSecurityConfigurerAdapter.java:455) ~[spring-security-config-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices.processAutoLoginCookie(PersistentTokenBasedRememberMeServices.java:150) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices.autoLogin(AbstractRememberMeServices.java:130) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter.doFilter(RememberMeAuthenticationFilter.java:98) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.doFilter(SecurityContextHolderAwareRequestFilter.java:170) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:63) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.session.ConcurrentSessionFilter.doFilter(ConcurrentSessionFilter.java:155) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter.doFilter(AbstractAuthenticationProcessingFilter.java:200) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:116) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.csrf.CsrfFilter.doFilterInternal(CsrfFilter.java:100) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:64) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:105) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:56) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:214) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:177) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:347) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:263) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.springframework.web.filter.HttpPutFormContentFilter.doFilterInternal(HttpPutFormContentFilter.java:108) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:81) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:199) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:478) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:140) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:81) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:87) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:342) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:803) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:868) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1459) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149) [na:1.8.0_141]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624) [na:1.8.0_141]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_141]

```
###解决方法：实现 `UserDetailsService`

- UserDetailsService.java

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
    
    import static cn.com.hellowood.springsecurity.common.constant.CommonConstant.USER;
    
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
    
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            logger.info("user {} is login by remember me cookie", username);
            UserModel user = userMapper.getUserByUsername(username);
            if (user != null){
                return new User(user.getUsername(), user.getPassword(), new ArrayList<GrantedAuthority>());
            }else{
                return null;
            }
        }
}

```

- SecurityConfig.java
```
    // ...
    
    // 注入自定义的 UserDetailsService
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
        
        // 在这里将 UserDetailsSercie 实例注入
        try {
            auth.userDetailsService(userDetailsService);
        } catch (Exception e) {
            logger.error("Set userDetailService failed, {}", e.getMessage());
            e.printStackTrace();
        }
    }
     
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(ROOT_URL, LOGIN_URL, LOGIN_ERROR_URL, CSS_WILDCARD_URL, INDEX_URL)
                .permitAll();

        http.authorizeRequests()
                .anyRequest()
                .authenticated();

        http.authorizeRequests()
                .and()
                .rememberMe()
                .rememberMeServices(rememberMeServices())
                .key(INTERNAL_SECRET_KEY);

        // ...
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        JdbcTokenRepositoryImpl rememberMeTokenRepository = new JdbcTokenRepositoryImpl();
        rememberMeTokenRepository.setDataSource(dataSource);

        // 这里也注入了 UserDetailsSerice 实例
        PersistentTokenBasedRememberMeServices rememberMeServices =
                new PersistentTokenBasedRememberMeServices(INTERNAL_SECRET_KEY, userDetailsService, rememberMeTokenRepository);

        rememberMeServices.setParameter(REMEMBER_ME);
        return rememberMeServices;
    }
    

   // ...
```

----------------------

## 通过 Remember Me 的 Cookie 登录时空指针异常

- 异常信息如下

```
java.lang.NullPointerException: null
	at org.springframework.security.authentication.AccountStatusUserDetailsChecker.check(AccountStatusUserDetailsChecker.java:32) ~[spring-security-core-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices.autoLogin(AbstractRememberMeServices.java:131) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter.doFilter(RememberMeAuthenticationFilter.java:98) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.doFilter(SecurityContextHolderAwareRequestFilter.java:170) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:63) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.session.ConcurrentSessionFilter.doFilter(ConcurrentSessionFilter.java:155) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter.doFilter(AbstractAuthenticationProcessingFilter.java:200) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:116) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.csrf.CsrfFilter.doFilterInternal(CsrfFilter.java:100) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:64) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:105) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:56) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:331) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:214) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:177) ~[spring-security-web-4.2.3.RELEASE.jar:4.2.3.RELEASE]
	at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:347) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:263) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.springframework.web.filter.HttpPutFormContentFilter.doFilterInternal(HttpPutFormContentFilter.java:108) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:81) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:199) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:478) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:140) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:81) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:87) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:342) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:803) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:868) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1459) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149) [na:1.8.0_141]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624) [na:1.8.0_141]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_141]

```

> 该异常是因为实现 `UserDetailsService` 的 `loadUserByUsername(String name)` 方法时没有考虑查询结果为 `null` 的情况，导致了空指针异常，
如果用户不存在应当直接抛出 `UsernameNotFoundException`

- CustomUserDetailsService.java

```
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userMapper.getUserByUsername(username);
        if (user == null) {
            logger.error("user {} login failed, the account not exist", username);
            throw new UsernameNotFoundException("The acccount not exist");
        } else if (!user.getEnabled()) {
            logger.error("user {} login failed, this account had expired", username);
            throw new AccountExpiredException("Account had expired");
        }
        // TODO There should add more logic to determine locked, expired and others status

        logger.info("user {} login success", username);
        // If user is valid put user info to session
        session.setAttribute(USER, user);

        return new User(user.getUsername(), user.getPassword(), new ArrayList<GrantedAuthority>());
    }
```