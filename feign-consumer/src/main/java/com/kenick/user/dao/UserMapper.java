package com.kenick.user.dao;

import com.kenick.user.bean.User;
import com.kenick.user.bean.UserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(String userId);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(String userId);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    List<User> selectFieldByExample(@Param("filedList") List<String> filedList, @Param("example") UserExample example);

    User selectFieldByPrimaryKey(@Param("filedList") List<String> filedList, @Param("userId") String userId);


/* field added */}
