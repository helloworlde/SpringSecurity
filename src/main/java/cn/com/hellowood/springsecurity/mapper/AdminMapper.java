package cn.com.hellowood.springsecurity.mapper;

import cn.com.hellowood.springsecurity.model.UserModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * The interface Admin mapper.
 *
 * @author HelloWood
 */
@Mapper
public interface AdminMapper {
    /**
     * Gets all users.
     *
     * @return the all users
     */
    List<UserModel> getAllUsers();

}
