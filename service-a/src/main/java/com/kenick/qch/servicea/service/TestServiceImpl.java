package com.kenick.qch.servicea.service;

import com.kenick.qch.servicea.feign.BFeign;
import com.kenick.qch.servicea.feign.CFeign;
import com.kenick.qch.servicea.pojo.TbDescription;
import com.kenick.qch.servicea.pojo.TbUser;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional//本地事务
public class TestServiceImpl implements TestService {

    @PersistenceContext //注入的是实体管理器,执行持久化操作
    EntityManager entityManager;

    @Autowired
    private BFeign bFeign;

    @Autowired
    private CFeign cFeign;

    @LcnTransaction//分布式事务
    @Override
    public String txlcn(String exFlag) {
        //先调用本地服务，新增一个用户user PS：调用EntityManager的merge，传进去的实体字段是什么就保存什么
        TbUser tbUser = new TbUser();
        tbUser.setUsername("tx-lcn-"+exFlag);
        tbUser.setPassword("123456");
        TbUser user = entityManager.merge(tbUser);
        System.out.println(user);

        //调用B服务，新增一个用户描述description
        TbDescription description1 = bFeign.txlcn(user.getId());
        System.out.println(description1);

        if("catchNull".equals(exFlag) && description1 == null){
            throw new RuntimeException("服务2异常！");
        }

        if("numExcepiton".equals(exFlag)){
            int num = 1/0;
        }

        //根据标识，是否抛出异常
        if (!StringUtils.isEmpty(exFlag)) {
            return "操作成功，请查看一下数据库验证！";
        } else {
            throw new RuntimeException("rollback transactional by exFlag");
        }
    }
}
