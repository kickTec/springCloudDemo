package com.kenick.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kenick.unusual.dao.UnusualMapper;
import com.kenick.user.bean.User;
import com.kenick.user.service.IUserService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * author: zhanggw
 * 创建时间:  2020/6/28
 */
@Component
public class RocketHelloConsumer {
    private static Logger logger = LoggerFactory.getLogger(RocketHelloConsumer.class);

    @Resource
    private RocketMqProducer rocketMqProducer;

    @Resource
    private IUserService userService;

    @Resource
    private UnusualMapper unusualMapper;

    // spring创建该类后，立刻开始订阅消息
    @PostConstruct
    public void initMethod(){
        try {
            DefaultMQPushConsumer defaultMQPushConsumer = rocketMqProducer.getDefaultMQPushConsumer();
            defaultMQPushConsumer.setConsumerGroup("hello-consumer"); // 设置消费组
            defaultMQPushConsumer.subscribe("kenick","2020"); // 订阅指定topic tag的消息
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(10); // 批量消费最大数量

            // 注册消息消费监听器
            defaultMQPushConsumer.registerMessageListener((MessageListenerConcurrently) (msgList, context) -> {
                logger.debug("本次共收到{}条消息", msgList.size());
                for (MessageExt messageExt: msgList) { // 批量消费
                    String jsonStr = new String(messageExt.getBody());
                    logger.debug("接收到消息: " + jsonStr);
                    JSONObject msgJson = JSON.parseObject(jsonStr);
                    String messageId = msgJson.getString("messageId");
                    try{
                        // 重复消息处理 或 幂等处理
                        Map<String, Object> msgMap = unusualMapper.selectMessageTxById(messageId);
                        String messageStatus = msgMap.get("message_status").toString();
                        if("4".equals(messageStatus)){
                            continue; // 已消费成功，跳过
                        }

                        // 消费业务 修改名称
                        User user = JSON.parseObject(jsonStr, User.class);
                        user.setName(user.getName()+"-consumer");
                        if(user.getAge() == 30){
                            throw new RuntimeException("age 30消费发生异常!");
                        }
                        userService.updateUser(user);

                        // 消费完成后，修改消费记录状态
                        unusualMapper.updateMessageTxStatus(messageId, 4); // 修改消费记录状态 成功
                    }catch (Exception e){
                        logger.debug("消费发生异常!", e);
                        unusualMapper.updateMessageTxStatus(messageId, 5); // 修改消费记录状态 失败
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER; // 只要有一个失败了就停止
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; // 全部成功，才算成功
            });
            defaultMQPushConsumer.start();
        } catch (Exception e) {
            logger.debug("rocketmq消费者订阅发生异常！");
        }
    }

}
