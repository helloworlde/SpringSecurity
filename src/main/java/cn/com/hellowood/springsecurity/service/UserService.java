package cn.com.hellowood.springsecurity.service;

import cn.com.hellowood.springsecurity.mapper.UserMapper;
import cn.com.hellowood.springsecurity.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type User service.
 *
 * @author HelloWood
 */
@Service("userService")
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * Load user by username user model.
     *
     * @param username the username
     * @return the user model
     */
    public UserModel loadUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

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
