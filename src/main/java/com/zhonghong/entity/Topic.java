package com.zhonghong.entity;

import java.sql.Timestamp;

public class Topic {

    private Integer id;
    private String title;
    private String content;
    private String issueIp;
    private Timestamp issueTime;
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIssueIp() {
        return issueIp;
    }

    public void setIssueIp(String issueIp) {
        this.issueIp = issueIp;
    }

    public Timestamp getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Timestamp issueTime) {
        this.issueTime = issueTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
