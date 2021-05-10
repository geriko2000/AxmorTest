package com.axmor.axmortest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class Comment {
    private long id;
    private String text;
    private User author;
    private Issue issueId;
    private Timestamp date;
    private IssueStatus issueStatus;
}
