package com.kenick.mq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

// 测试使用，实际使用请参考feign-consumer项目
public class RocketMqConsumer {
    private static String customerGroup = "rocketMQCustomer";
    private static String namesrvAddr = "192.168.0.105:9876";

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(customerGroup);
        consumer.setNamesrvAddr(namesrvAddr);

        consumer.subscribe("kenick","2020-06-24");
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messageExts, ConsumeOrderlyContext context) {
                context.setAutoCommit(true);

                for (MessageExt messageExt: messageExts) {
                    System.out.println("consumer >>> " + new String(messageExt.getBody()));
                }

                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
        System.out.println("开始消费消息！！！");
    }

}
