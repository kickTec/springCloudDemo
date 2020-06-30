package com.kenick.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kenick.mq.service.IRocketMqService;
import com.kenick.unusual.dao.UnusualMapper;
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
import java.util.Map;

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

    @Override
    public int saveUserByMsgTx(String userId, String name, int age) throws Exception{
        // 封装消息数据
        JSONObject userJson = new JSONObject();
        userJson.put("userId", userId);
        userJson.put("name", name);
        userJson.put("age", age);
        userJson.put("messageId", System.currentTimeMillis()+"user");
        userJson.put("serviceType", 1); // 业务类型 1 添加用户
        userJson.put("serviceId", userId); // 业务id
        Message message = new Message("kenick", "2020", "KEY2020", userJson.toJSONString().getBytes());

        // 发送半消息，在回调方法中收到broker的确认信息后，再执行业务操作
        rocketMqService.sendTransactionMessage(message, new TransactionListener() {
            // rocketmq broker回调方法，标识该半消息已收到，开始执行业务操作
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                logger.debug("rocketmq确认的半消息,信息{}", msg.toString());
                JSONObject msgJson = JSON.parseObject(new String(msg.getBody()));
                String messageId = msgJson.getString("messageId");
                try{
                    // 通过spring代理调用真正的业务操作方法，这样会触发方法上的本地事务注解
                    User saveUser = JSONObject.parseObject(msgJson.toJSONString(), User.class);
                    IUserService userService = applicationContext.getBean(IUserService.class);
                    userService.saveUser(saveUser.getUserId(), saveUser.getName(), saveUser.getAge()); // 保存用户业务方法
                    logger.debug("本地事务执行完毕!");

                    unusualMapper.updateMessageTxStatus(messageId, 2); // 修改消息状态为已发送

                    if(saveUser.getAge() == 28){ // 返回未知，触发后续回查本地事务状态
                        logger.debug("触发本地事务返回未知状态条件！");
                        return LocalTransactionState.UNKNOW;
                    }

                    return LocalTransactionState.COMMIT_MESSAGE; // 本地执行无误后，提交半消息
                }catch (Exception e){
                    logger.debug("本地事务执行异常,消息:{},异常:{}", msg.toString(), e.getMessage());
                    unusualMapper.updateMessageTxStatus(messageId, 3); // 修改消息状态为发送失败，本地事务异常，可后续查看及处理
                    return LocalTransactionState.ROLLBACK_MESSAGE; // 出现异常，回滚半消息
                }
            }

            // rocketmq回查本地事务  当rocketmq收到并确认半消息后，由于本地业务操作异常等情况，造成未commit/rollback该半消息
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                logger.debug("开始回查本地事务! 消息:{}", msg);
                JSONObject msgJson = JSON.parseObject(new String(msg.getBody()));
                String messageId = msgJson.getString("messageId");
                Map<String, Object> objectMap = unusualMapper.selectMessageTxById(messageId);
                logger.debug("查询本地消息表结果为:{}", objectMap);

                String messageStatus = objectMap.get("message_status").toString();
                if("2".equals(messageStatus)){
                    return LocalTransactionState.COMMIT_MESSAGE; // 本地事务正常完成，commit
                }else if("3".equals(messageStatus)){
                    return LocalTransactionState.ROLLBACK_MESSAGE; // 本地事务异常，rollback,消息会被撤销
                }else{
                    return LocalTransactionState.UNKNOW; // 本地执行失败等情况，rocketmq暂不处理消息，等待本地事务执行
                }
            }
        });
        return 1;
    }

}
