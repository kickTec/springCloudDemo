package com.kenick.user.service.impl;

import com.kenick.extend.interfaces.IHelloService;
import com.kenick.user.bean.User;
import com.kenick.user.dao.UserMapper;
import com.kenick.user.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * author: zhanggw
 * 创建时间:  2020/6/18
 */
@Service
public class UserServiceImpl implements IUserService {
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private IHelloService helloService;

    @Transactional
    @Override
    public int saveUser(String userId,String name, int age) {
        logger.debug("本地开始保存用户！");
        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setAge(age);
        int ret = userMapper.insert(user);
        logger.debug("本地开始保存用户结果:" + ret);

        logger.debug("开始远程调用helloservice!");
        String remoteRet = helloService.addUser("remote_hello" + userId, name, age);
        logger.debug("远程调用helloservice结果:" + remoteRet);
        return ret;
    }

}
