# springCloudDemo
spring cloud项目（eureka/ribbon/feign/hystrix/zuul/config-server）  

组件说明：  
api-gateway：网关组件  
config-server：配置中心组件  
eureka-server：服务注册与发现组件  
feign-consumer：消费者组件，微服务主动调用方，feign方式  
hello：生产者组件，微服务被调用方  
ribbon-consumer：消费者组件，微服务主动调用方，ribbon方式  
spring_cloud_in_action/config-repo：配置中心所需配置存放路径，默认使用github,也可以自定义放到SVN中。  

同时该项目有以下几个分支：  
tx-lcn: 分布式事务,本身不创造事务，通过代理数据库连接，控制事务提交或回滚，强一致性，适用于实时性较高、高一致性的业务，性能不高。  
transaction-activemq: 分布式消息事务，最终一致性，弱一致性，性能较好。  
