package com.kenick.mq.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kenick.mq.RocketMqProducer;
import com.kenick.mq.service.IRocketMqService;
import com.kenick.unusual.dao.UnusualMapper;
import org.apache.commons.lang3.StringUtils;
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

    @Resource
    private UnusualMapper unusualMapper;

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
            // 发送半消息前，持久化消息数据，若发送失败，可尝试重新发送
            String messageBody = new String(message.getBody());
            JSONObject msgBody = JSON.parseObject(messageBody);
            String messageId = msgBody.getString("messageId");
            if(StringUtils.isNotBlank(messageId)){
                unusualMapper.insertMessageTx(messageId, message.getTopic(), message.getTags(),
                        message.getKeys(), messageBody, msgBody.getInteger("serviceType"), msgBody.getString("serviceId"),
                        msgBody.getString("relateId"), 1);
            }

            // 向rocketmq发送消息
            TransactionMQProducer transactionMQProducer = rocketMqProducer.getTransactionMqProducer(transactionListener);
            transactionSendResult = transactionMQProducer.sendMessageInTransaction(message, transactionMQProducer.getTransactionListener());
            logger.debug("发送消息结果:{}", transactionSendResult);

            // 发送完消息后，将本地消息关联mq消息id
            if(transactionSendResult != null && StringUtils.isNotBlank(transactionSendResult.getMsgId())){
                unusualMapper.updateMessageTxMqId(messageId, transactionSendResult.getMsgId());
            }
        }catch (Exception e){
            logger.debug("rocketmq发送事务消息异常:{}", e);
        }
        return transactionSendResult;
    }

}
