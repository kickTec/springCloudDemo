package com.kenick.mq.service;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * author: zhanggw
 * 创建时间:  2020/6/28
 */
public interface IRocketMqService {

    public SendResult sendMessage(Message message);

    public TransactionSendResult sendTransactionMessage(Message message, TransactionListener transactionListener);
}
