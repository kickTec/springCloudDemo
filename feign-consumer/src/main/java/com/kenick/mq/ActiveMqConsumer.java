package com.kenick.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fangjia.common.util.JsonUtils;
import com.kenick.extend.interfaces.ITransactionActivemqSV;
import com.kenick.user.bean.User;
import com.kenick.user.dao.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.TextMessage;

/**
 * 可靠消息服务消费类
 * @author yinjihuan
 *
 */
@Component
public class ActiveMqConsumer {
	private final Logger logger = LoggerFactory.getLogger(ActiveMqConsumer.class);

	@Autowired
	private ITransactionActivemqSV transactionActivemqSV;

	@Resource
	private UserMapper userMapper;
	
	// hello队列消息处理
	@JmsListener(destination = "hello_queue")
	public void receiveQueue(TextMessage text) {
		try {
			logger.debug("可靠消息服务消费消息："+text.getText());
			JSONObject transactionMsgJson = JSON.parseObject(text.getText());

			// 开始进行消息处理
			User user =  JsonUtils.toBean(User.class, transactionMsgJson.getString("message"));
			if("exception".equals(user.getName())){
				int num = 1/0; // 产生异常
			}else{
				User updateUser = new User();
				updateUser.setUserId(user.getUserId());
				updateUser.setName("activemq"+user.getName());
				userMapper.updateByPrimaryKeySelective(updateUser);
			}

			//修改成功后调用消息确认消费接口，确认该消息已被消费
			boolean result = transactionActivemqSV.confirmCustomerMessage("feign-consumer", Long.parseLong(transactionMsgJson.getString("messageId")));
			//手动确认ACK
			if (result) {
				text.acknowledge();
			}
		} catch (Exception e) {
			logger.error("消费异常!", e);
			// 异常时会触发重试机制，重试次数完成之后还是错误，消息会进入DLQ队列中
			throw new RuntimeException(e);
		}
	}

	// 死信队列消息处理
	@JmsListener(destination = "ActiveMQ.DLQ")
	public void receiveDLQQueue(TextMessage text) {
		try {
			logger.debug("死信队列消息："+text.getText());
		} catch (Exception e) {
			logger.error("消费异常!", e);
		}
	}
}
