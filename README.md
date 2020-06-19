# springCloudDemo
基于分布式事务tx-lcn的spring cloud项目

关键组件（可用）：
txlcn-tm：springcloud 2.1.0,tx-lcn的事务管理者。
eureka-server：springcloud 2.1.0、或者springcloud 1.5.4都可以。
service-a：springcloud 2.1.0，服务a,操作数据库及调用服务b1。
service-b：springcloud 2.1.0，服务b,操作数据库。

下面这两个服务另一个版本的springcloud 1.5.4，本来想集成tx-lc的，暂未弄好，后续又看到阿里分布式事务seata，
由于tx-lcn暂定维护及seata可能更优秀，暂时搁置集成了。
feign-consumer:springcloud 1.5.,类似于服务a,操作数据库及调用hello服务，。
hello：springcloud 1.5.4，类似与服务b,操作数据库，
