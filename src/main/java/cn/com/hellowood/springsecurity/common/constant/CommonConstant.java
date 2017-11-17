package cn.com.hellowood.springsecurity.common.constant;

/**
 * Common constant
 *
 * @author HelloWood
 */
public class CommonConstant {

    /**
     * URI
     */
    public static final String ROOT_URL = "/";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String LOGIN_ERROR_URL = "/login-error";
    public static final String INDEX_URL = "/index";
    public static final String ADMIN_INDEX_URL = "/admin/index";
    public static final String ADMIN_WILDCARD_URL = "/admin/**";
    public static final String USER_INDEX_URL = "/user/index";
    public static final String USER_WILDCARD_URL = "/user/**";
    public static final String CSS_WILDCARD_URL = "/css/**";

    /**
     * User role
     */
    public static final String ADMIN = "ADMIN";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String USER = "USER";
    public static final String ROLE_USER = "ROLE_USER";


    /**
     * Parameter key
     */
    public static final String LOGIN_ERROR_MSG = "loginErrorMsg";
    public static final String LOGIN_USER = "user";

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final String INTERNAL_SECRET_KEY = "internalSecretKey";
    public static final String REMEMBER_ME = "remember-me";

}
