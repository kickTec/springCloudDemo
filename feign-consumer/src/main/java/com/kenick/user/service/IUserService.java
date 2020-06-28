package com.kenick.user.service;


import com.kenick.user.bean.User;

/**
 * author: zhanggw
 * 创建时间:  2020/6/18
 */
public interface IUserService {
    int saveUser(String userId, String name, int age);

    int updateUser(User user);
}
