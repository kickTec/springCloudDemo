package com.kenick.mq;

import com.alibaba.fastjson.JSON;
import com.kenick.user.bean.User;
import com.kenick.user.service.IUserService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
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

            defaultMQPushConsumer.registerMessageListener((MessageListenerOrderly) (messageExts, context) -> {
                context.setAutoCommit(true);

                for (MessageExt messageExt: messageExts) {
                    String jsonStr = new String(messageExt.getBody());
                    logger.debug("接收到消息: " + jsonStr);
                    User user = JSON.parseObject(jsonStr, User.class);
                    user.setName(user.getName()+"-consumer");
                    userService.updateUser(user);
                }
                return ConsumeOrderlyStatus.SUCCESS;
            });
            defaultMQPushConsumer.start();
        } catch (MQClientException e) {
            logger.debug("rocketmq消费者发生异常！");
        }
    }

}
