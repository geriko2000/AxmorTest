package com.axmor.axmortest.dao;

import com.axmor.axmortest.model.Issue;
import com.axmor.axmortest.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IssueMapper {

    @Select("select i.id, i.name, i.author_id as author, i.description, i.start_date as startDate, i.status from Issues i")
    List<Issue> findAll();

    @Select("select * from Issues " +
            "where id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "startDate", column = "start_date"),
            @Result(property = "description", column = "description"),
            @Result(property = "status", column = "status"),
            @Result(property = "author", column = "author_id", one = @One(select = "com.axmor.axmortest.dao.UserMapper.findById"))
    })
    Issue findById(long id);

    @Select("Select * from Issues where lower(name) like lower('%${name}%')")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "startDate", column = "start_date"),
            @Result(property = "description", column = "description"),
            @Result(property = "status", column = "status"),
            @Result(property = "author", column = "author_id", one = @One(select = "com.axmor.axmortest.dao.UserMapper.findById"))
    })
    List<Issue> findByName(String name);

    @Insert("Insert into Issues (name, author_id, description, start_date, status) " +
            "values (#{name}, #{author.id}, #{description}, #{startDate}, #{status})")
    void insert(Issue issue);

    @Update("Update Issues set status = #{status} " +
            "where id = #{issue.id}")
    void updateIssueStatus(Issue issue, String status);
}
