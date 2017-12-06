package cn.com.hellowood.springsecurity.service.impl;

import cn.com.hellowood.springsecurity.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * The User service implementation.
 *
 * @author HelloWood
 */
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, timeout = 30)
public class UserServiceImpl extends BaseServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

}