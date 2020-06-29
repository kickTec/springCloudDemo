package com.kenick.unusual.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UnusualMapper {

    public int insertMessageTx(@Param("messageId") String messageId, @Param("topics") String topics, @Param("tags") String tags, @Param("keys") String keys,
                               @Param("messageBody") String messageBody, @Param("serviceType") Integer serviceType, @Param("serviceId") String serviceId,
                               @Param("relateId") String relateId, @Param("messageStatus") Integer messageStatus);

    public int updateMessageTxStatus(@Param("messageId") String messageId, @Param("messageStatus") Integer messageStatus);

    public int updateMessageTxMqId(@Param("messageId") String messageId, @Param("mqId") String mqId);

    public Map<String, Object> selectMessageTxById(@Param("messageId") String messageId);

}