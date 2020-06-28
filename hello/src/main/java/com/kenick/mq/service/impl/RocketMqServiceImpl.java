package com.kenick.mq.service.impl;

import com.kenick.mq.RocketMqProducer;
import com.kenick.mq.service.IRocketMqService;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * author: zhanggw
 * 创建时间:  2020/6/28
 */
@Service
public class RocketMqServiceImpl implements IRocketMqService {

    private Logger logger = LoggerFactory.getLogger(RocketMqServiceImpl.class);

    @Resource
    private RocketMqProducer rocketMqProducer;

    @Override
    public SendResult sendMessage(Message message) {
        SendResult sendResult = null;
        try{
            DefaultMQProducer defaultMQProducer = rocketMqProducer.getDefaultMQProducer();
            sendResult = defaultMQProducer.send(message);
        }catch (Exception e){
            logger.debug("rocketmq发送消息异常:", e);
        }
        return sendResult;
    }

    @Override
    public TransactionSendResult sendTransactionMessage(Message message, TransactionListener transactionListener) {
        TransactionSendResult transactionSendResult = null;
        try{
            TransactionMQProducer transactionMQProducer = rocketMqProducer.getTransactionMqProducer(transactionListener);
            transactionSendResult = transactionMQProducer.sendMessageInTransaction(message, transactionMQProducer.getTransactionListener());
        }catch (Exception e){
            logger.debug("rocketmq发送事务消息异常:{}", e);
        }
        return transactionSendResult;
    }

}
