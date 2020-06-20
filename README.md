# springCloudDemo  
spring cloud项目 分布式消息事务 最终一致性 activemq 5.14.5  
eureka-server： 服务注册与发现，1.5.4.RELEASE，启动顺序:1。  
transaction-mq-service：分布式事务消息处理服务（对应书中的message service）,消息存储，启动顺序:2。    
transaction-mq-task:消息发送系统,实际发送消息给mq，启动顺序：3。  
feign-consumer:消息消费者(对应书中substitution service)，启动顺序：4。  
hello:消息生产者（对应书中house service），启动顺序：5。  
其它为依赖的一些服务。  

流程图：
![image](https://github.com/kickTec/springCloudDemo/blob/transaction-activemq/%E6%B5%81%E7%A8%8B%E5%9B%BE.png)

调用过程：  
1.请求hello服务的updateUser接口，此接口会更新用户信息（若无则创建）。  
2.hello服务本地操作完毕后，再将请求信息通过transaction-mq-service持久化到activemq中，状态待消费。  
3.transaction-mq-task服务查询到待消费消息，将消息发送到mq。  
4.feign-consume监听到消息，对消息进行消费，若成功，再手动确认消息，同时通过transaction-mq-servic确认消息，将消息状态修改为已确认。  
5.若消费时出现异常，或其它状态，activemq会重发消息，消费业务注意幂等性；超过一定次数，消息会进入到activemq的死信队列。  


参考资料：
《Spring Cloud微服务全栈技术与案例解析》 尹吉欢




