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

    @PostConstruct
    public void initMethod(){
        try {
            DefaultMQPushConsumer defaultMQPushConsumer = rocketMqProducer.getDefaultMQPushConsumer();
            defaultMQPushConsumer.setConsumerGroup("hello-consumer");
            defaultMQPushConsumer.subscribe("kenick","2020");
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(10); // 批量消费 先启动生产者，再启动消费者

            defaultMQPushConsumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                for (MessageExt messageExt: msgs) { // 批量消费
                    String jsonStr = new String(messageExt.getBody());
                    logger.debug("接收到消息: " + jsonStr);
                    JSONObject msgJson = JSON.parseObject(jsonStr);
                    String messageId = msgJson.getString("messageId");
                    try{
                        // 消费业务 修改名称
                        User user = JSON.parseObject(jsonStr, User.class);
                        user.setName(user.getName()+"-consumer");
                        if(user.getAge() == 30){
                            throw new RuntimeException("age 30消费发生异常!");
                        }
                        userService.updateUser(user);

                        // 修改
                        unusualMapper.updateMessageTxStatus(messageId, 4); // 消费成功
                    }catch (Exception e){
                        logger.debug("消费发生异常!", e);
                        unusualMapper.updateMessageTxStatus(messageId, 5); // 消费成功
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER; // 只要有一个失败了就停止
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            defaultMQPushConsumer.start();
        } catch (Exception e) {
            logger.debug("rocketmq消费者订阅发生异常！");
        }
    }

}
