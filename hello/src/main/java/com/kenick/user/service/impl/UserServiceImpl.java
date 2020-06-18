package com.kenick.user.service.impl;

import com.kenick.user.bean.User;
import com.kenick.user.dao.UserMapper;
import com.kenick.user.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * author: zhanggw
 * 创建时间:  2020/6/18
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Transactional
    @Override
    public int saveUser(String userId,String name, int age) {
        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setAge(age);
        int lines = userMapper.insert(user);
        return lines;
    }

}
