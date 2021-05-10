package com.axmor.axmortest.service;

import com.axmor.axmortest.dao.CommentMapper;
import com.axmor.axmortest.dao.IssueMapper;
import com.axmor.axmortest.model.Issue;
import com.axmor.axmortest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class IssueService {

    @Autowired
    private IssueMapper issueMapper;

    @Autowired
    private CommentMapper commentMapper;

    public void createIssue(User user, String name, String description) {
        Issue issue = new Issue(user, name, description);
        issueMapper.insert(issue);
    }

    public void addComment(User user, Issue issue, String status, String text) {
        Date commentDate = new Date();
        commentMapper.insert(user, text, issue, commentDate, status);
        String currentIssueStatus = issue.getStatus().toString();
        if (!currentIssueStatus.equals(status)) {
            issueMapper.updateIssueStatus(issue, status);
        }
    }
}
