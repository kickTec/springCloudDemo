# springCloudDemo  
spring cloud项目 分布式消息事务 最终一致性 activemq 5.14.5  
eureka-server： 服务注册与发现，1.5.4.RELEASE，启动顺序:1。  
transaction-mq-service：分布式事务消息处理服务,消息存储，启动顺序:2。    
transaction-mq-task:分布式事务消息处理服务,实际发送消息给mq，启动顺序：3。  
feign-consumer:消息消费者，启动顺序：4。  
hello:消息生产者，启动顺序：5。  
其它为依赖的一些服务。  

参考资料：
《Spring Cloud微服务全栈技术与案例解析》 尹吉欢




