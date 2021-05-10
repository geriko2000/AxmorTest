package com.axmor.axmortest.dao;

import com.axmor.axmortest.model.Comment;
import com.axmor.axmortest.model.Issue;
import com.axmor.axmortest.model.User;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("Select * from Comments where issue_id = #{id}")
    @Results( value = {
            @Result(property = "id", column = "id"),
            @Result(property = "text", column = "text"),
            @Result(property = "issueId", column = "issue_id", one = @One(select = "com.axmor.axmortest.dao.IssueMapper.findById")),
            @Result(property = "date", column = "date"),
            @Result(property = "issueStatus", column = "issue_status"),
            @Result(property = "author", column = "author_id", one = @One(select = "com.axmor.axmortest.dao.UserMapper.findById"))
    })
    List<Comment> findAllByIssueId(long id);

    @Insert("Insert into Comments (author_id, text, issue_id, date, issue_status) " +
            "values (#{user.id}, #{text}, #{issue.id}, #{date}, #{status})")
    void insert(User user, String text, Issue issue, Date date, String status);
}
