package com.kenick.qch.serviceb1.service;

import com.kenick.qch.serviceb1.pojo.TbDescription;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional//本地事务
public class TestServiceImpl implements TestService {

    @PersistenceContext //注入的是实体管理器,执行持久化操作
    EntityManager entityManager;

    @LcnTransaction//分布式事务
    @Override
    public TbDescription txlcn(Integer userId){
        TbDescription tbDescription = null;
        try{
            tbDescription = entityManager.find(TbDescription.class, userId);
            if(tbDescription == null){
                tbDescription = new TbDescription();
                tbDescription.setId(userId);
                tbDescription.setUserId(userId);
                tbDescription.setDescription("服务B设置的描述");
            }else{
                tbDescription.setDescription(tbDescription.getDescription()+",服务B设置的描述");
            }
            tbDescription = entityManager.merge(tbDescription);
            int num = 1/0;
        }catch (Exception e){
            tbDescription = null;
            System.out.println(e.getMessage());
        }
        return tbDescription;
    }
}
