package cn.com.hellowood.springsecurity.service.impl;

import cn.com.hellowood.springsecurity.mapper.AdminMapper;
import cn.com.hellowood.springsecurity.model.UserModel;
import cn.com.hellowood.springsecurity.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The Admin service implementation.
 *
 * @author HelloWood
 */
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, timeout = 30)
public class AdminServiceImpl extends BaseServiceImpl implements AdminService {

    /**
     * The Admin mapper.
     */
    @Autowired
    AdminMapper adminMapper;

    @Override
    public List<UserModel> getAllUsers() {
        return adminMapper.getAllUsers();
    }
}
