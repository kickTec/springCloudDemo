package com.kenick.user.service.impl;

import com.kenick.unusual.dao.UnusualMapper;
import com.kenick.user.bean.User;
import com.kenick.user.dao.UserMapper;
import com.kenick.user.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;

/**
 * author: zhanggw
 * 创建时间:  2020/6/18
 */
@Service
public class UserServiceImpl implements IUserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Autowired
    private ApplicationContext applicationContext;

    @Resource
    private UnusualMapper unusualMapper;

    @Transactional
    @Override
    public int saveUser(String userId, String name, int age) {
        logger.debug("currentTransactionName:{},level:{}", TransactionSynchronizationManager.getCurrentTransactionName(),
                TransactionSynchronizationManager.getCurrentTransactionIsolationLevel());

        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setAge(age);
        int lines = userMapper.insert(user);

        if(age == 29){
            int num = 1/0; // 本地事务抛出异常
        }
        return lines;
    }

}
