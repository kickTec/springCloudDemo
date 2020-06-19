# springCloudDemo  
基于分布式事务tx-lcn的spring cloud项目  

关键组件（可用）：  
txlcn-tm：springcloud 2.1.0,tx-lcn的事务管理者。  
eureka-server：springcloud 2.1.0、或者springcloud 1.5.4都可以。  
service-a：springcloud 2.1.0，服务a,操作数据库及调用服务b1。  
service-b：springcloud 2.1.0，服务b,操作数据库。 
test.sql：数据库文件。

txlcn-tm管理网址：
http://192.168.0.105:7970/admin/index.html#/

下面这两个服务另一个版本的springcloud 1.5.4，本来想集成tx-lc的，暂未弄好，后续又看到阿里分布式事务seata，  
由于tx-lcn暂定维护及seata可能更优秀，暂时搁置集成了。  
feign-consumer:springcloud 1.5.,类似于服务a,操作数据库及调用hello服务，。  
hello：springcloud 1.5.4，类似与服务b,操作数据库。 t


参考资料：  
https://www.cnblogs.com/huanzi-qch/p/11057974.html  
https://blog.csdn.net/ningjiebing/article/details/89948050  
https://blog.csdn.net/lby0307/article/details/84551637  
https://juejin.im/post/5b5a0bf9f265da0f6523913b  
https://blog.csdn.net/qq_42556214/article/details/105796048  
