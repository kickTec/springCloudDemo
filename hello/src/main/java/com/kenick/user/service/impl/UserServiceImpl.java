package com.kenick.user.service.impl;

import com.fangjia.common.util.JsonUtils;
import com.fangjia.mqclient.dto.TransactionMessage;
import com.kenick.extend.interfaces.ITransactionActivemqSV;
import com.kenick.user.bean.User;
import com.kenick.user.dao.UserMapper;
import com.kenick.user.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;

/**
 * author: zhanggw
 * 创建时间:  2020/6/18
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ITransactionActivemqSV transactionActivemqSV;

    @Transactional
    @Override
    public int saveUser(String userId,String name, int age) {
        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setAge(age);
        int lines = userMapper.insert(user);
        // 产生异常
        // int num = 1/0;
        return lines;
    }

    @Transactional
    @Override
    public Object update(String userId, String name) {
        // 本地修改或插入用户
        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setAge(new Random().nextInt(100));

        int ret = userMapper.updateByPrimaryKeySelective(user);
        if(ret == 0){
            userMapper.insert(user);
        }

        // 修改之后发送消息给消费者进行业务处理，最终一致性
        TransactionMessage message = new TransactionMessage();
        message.setQueue("hello_queue");
        message.setCreateDate(new Date());
        message.setSendSystem("hello-service");
        message.setMessage(JsonUtils.toJson(user));

        boolean result = transactionActivemqSV.sendMessage(message);
        if (!result) {
            throw new RuntimeException("回滚事务");
        }
        return result;
    }

}
