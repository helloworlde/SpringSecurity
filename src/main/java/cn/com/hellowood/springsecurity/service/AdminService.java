package cn.com.hellowood.springsecurity.service;

import cn.com.hellowood.springsecurity.model.UserModel;

import java.util.List;

/**
 * The interface Admin service.
 *
 * @author HelloWood
 */
public interface AdminService {
    List<UserModel> getAllUsers();

}
