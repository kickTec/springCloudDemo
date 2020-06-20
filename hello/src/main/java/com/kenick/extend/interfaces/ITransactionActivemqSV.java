package com.kenick.extend.interfaces;

import com.fangjia.mqclient.dto.TransactionMessage;
import com.fangjia.mqclient.query.MessageQuery;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * author: zhanggw
 * 创建时间:  2020/6/20
 */
@FeignClient(value = "transaction-mq-service", path = "/message")
public interface ITransactionActivemqSV {
    @PostMapping({"/send"})
    boolean sendMessage(@RequestBody TransactionMessage var1);

    @PostMapping({"/sends"})
    boolean sendMessage(@RequestBody List<TransactionMessage> var1);

    @PostMapping({"/incrSendCount"})
    boolean incrSendCount(@RequestParam("messageId") Long var1, @RequestParam("sendDate") String var2);

    @PostMapping({"/confirm/die"})
    boolean confirmDieMessage(@RequestParam("messageId") Long var1);

    @PostMapping({"/confirm/customer"})
    boolean confirmCustomerMessage(@RequestParam("customerSystem") String var1, @RequestParam("messageId") Long var2);

    @GetMapping({"/wating"})
    List<TransactionMessage> findByWatingMessage(@RequestParam("limit") int var1);

    @PostMapping({"/send/retry"})
    boolean retrySendDieMessage();

    @PostMapping({"/query"})
    List<TransactionMessage> findMessageByPage(@RequestBody MessageQuery var1);
}
