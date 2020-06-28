package com.kenick.user.service;

/**
 * author: zhanggw
 * 创建时间:  2020/6/18
 */
public interface IUserService {
    int saveUser(String userId,String name, int age);

    int saveUserByMsgTx(String userId, String name, int age) throws Exception;
}
