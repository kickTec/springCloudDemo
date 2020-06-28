package com.kenick.mq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component("rocketMqProducer")
public class RocketMqProducer {

    private Logger logger = LoggerFactory.getLogger(RocketMqProducer.class);

    // 生产组
    @Value("${rocketmq.producer.group.name}")
    private String producerGroup;

    // RocketMq server 地址，多个地址之间用分号分割：192.0.0.1:9876;192.0.0.2:9876
    @Value("${rocketmq.producer.namesrvAddr}")
    private String namesrvAddr;

    private DefaultMQProducer defaultMQProducer; // 默认生产者
    private TransactionMQProducer transactionMQProducer; // 事务生产者

    public DefaultMQProducer getDefaultMQProducer() throws Exception{
        if(defaultMQProducer == null){
            defaultMQProducer = new DefaultMQProducer(producerGroup);
            defaultMQProducer.setNamesrvAddr(namesrvAddr);
            defaultMQProducer.start();
        }
        return defaultMQProducer;
    }

    public DefaultMQPushConsumer getDefaultMQPushConsumer(){
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);
        return defaultMQPushConsumer;
    }

    public TransactionMQProducer getTransactionMqProducer(TransactionListener transactionListener) throws Exception{
        if(transactionMQProducer == null){
            transactionMQProducer = new TransactionMQProducer(producerGroup);
            transactionMQProducer.setTransactionListener(transactionListener);
            transactionMQProducer.setNamesrvAddr(namesrvAddr);
            transactionMQProducer.start();
        }
        return transactionMQProducer;
    }

    public static void main(String[] args) throws IOException {
        try {
            DefaultMQProducer producer = new DefaultMQProducer("hello-producer");
            producer.setNamesrvAddr("192.168.0.105:9876");
            producer.start();

            String body = "测试消息"+System.currentTimeMillis();
            Message message = new Message("kenick", "2020-06-28", "KEY02", body.getBytes());
            SendResult sendResult = producer.send(message);

            System.out.println("发送结果:" + sendResult.toString());
            producer.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
