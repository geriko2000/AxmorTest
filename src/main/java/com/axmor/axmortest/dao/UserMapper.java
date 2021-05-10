package com.axmor.axmortest.dao;

import com.axmor.axmortest.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("Select * from Users where username = #{username}")
    User findByUsername(String username);

    @Select("Select id, username from Users where id = #{id}")
    User findById(long id);
}
