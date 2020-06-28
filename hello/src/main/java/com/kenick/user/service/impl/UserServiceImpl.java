package com.kenick.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kenick.mq.service.IRocketMqService;
import com.kenick.user.bean.User;
import com.kenick.user.dao.UserMapper;
import com.kenick.user.service.IUserService;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
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

    @Resource
    private IRocketMqService rocketMqService;

    @Autowired
    private ApplicationContext applicationContext;

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

        // int num = 1/0;
        return lines;
    }

    @Override
    public int saveUserByMsgTx(String userId, String name, int age) throws Exception{
        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setAge(age);

        String body = JSON.toJSONString(user);
        Message message = new Message("kenick", "2020", "KEY2020", body.getBytes());
        rocketMqService.sendTransactionMessage(message, new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                logger.debug("开始执行本地事务,{}", msg.toString());
                try{
                    String jsonString = new String(msg.getBody());
                    User saveUser = JSONObject.parseObject(jsonString, User.class);
                    // saveUser(saveUser.getUserId(), saveUser.getName(), saveUser.getAge()); 这种方式调用方法，不会使用到原来的事务
                    IUserService userService = applicationContext.getBean(IUserService.class);
                    userService.saveUser(saveUser.getUserId(), saveUser.getName(), saveUser.getAge());
                    logger.debug("本地事务执行完毕!");
                    return LocalTransactionState.COMMIT_MESSAGE;
                }catch (Exception e){
                    logger.debug("本地事务执行异常,消息:{},异常:{}", msg.toString(), e.getMessage());
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                logger.debug("开始检查本地事务! msg:{}", msg);
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });
        return 1;
    }

}
