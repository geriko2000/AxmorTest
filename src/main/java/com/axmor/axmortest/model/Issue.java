package com.axmor.axmortest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Issue {
    private long id;
    private String name;
    private User author;
    private String description;
    private Date startDate;
    private IssueStatus status;

    public Issue() {
    }

    public Issue(User user, String name, String description) {
        this.name = name;
        this.author = user;
        this.description = description;
        this.startDate = new Date();
        this.status = IssueStatus.Created;
    }
}
