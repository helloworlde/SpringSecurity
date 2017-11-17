package cn.com.hellowood.springsecurity.mapper;


import cn.com.hellowood.springsecurity.model.UserModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface User mapper.
 *
 * @author HelloWood
 */
@Mapper
public interface UserMapper {

    /**
     * Gets user by username.
     *
     * @param username the username
     * @return user by username
     */
    UserModel getUserByUsername(String username);

    /**
     * Gets user by username and password.
     *
     * @param username the username
     * @param password the password
     * @return the user by username and password
     */
    UserModel getUserByUsernameAndPassword(@Param("username") String username,
                                           @Param("password") String password);

    /**
     * Gets role by user id.
     *
     * @param userId the user id
     * @return the role by user id
     */
    List<String> getRoleByUserId(@Param("userId") Integer userId);
}
