package com.kenick.mq;

import com.alibaba.fastjson.JSON;
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

    @PostConstruct
    public void initMethod(){
        try {
            DefaultMQPushConsumer defaultMQPushConsumer = rocketMqProducer.getDefaultMQPushConsumer();
            defaultMQPushConsumer.setConsumerGroup("hello-consumer");
            defaultMQPushConsumer.subscribe("kenick","2020");

            defaultMQPushConsumer.registerMessageListener((MessageListenerConcurrently) (messageExts, context) -> {
                try{
                    for (MessageExt messageExt: messageExts) {
                        String jsonStr = new String(messageExt.getBody());
                        logger.debug("接收到消息: " + jsonStr);
                        User user = JSON.parseObject(jsonStr, User.class);
                        user.setName(user.getName()+"-consumer");
                        if(user.getAge() == 30){
                            throw new RuntimeException("age 30消费发生异常!");
                        }
                        userService.updateUser(user);
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }catch (Exception e){
                    logger.debug("消费发生异常!", e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            });
            defaultMQPushConsumer.start();
        } catch (Exception e) {
            logger.debug("rocketmq消费者订阅发生异常！");
        }
    }

}
