package com.zhonghong.dao;

import com.zhonghong.entity.Topic;
import com.zhonghong.entity.User;
import com.zhonghong.helper.JdbcHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopicDao {

    public boolean save(Topic t) {

        String SQL = "insert into t_topic (title, content, author, issue_ip) values (?,?,?,?)";

        JdbcHelper h = JdbcHelper.getInstance();
        int count = h.update(SQL, t.getTitle(), t.getContent(), t.getUser().getId(), t.getIssueIp());
        h.release();

        return count == 1;
    }

    public List<Topic> findAll() {
        List<Topic> list = new ArrayList<>();

        String SQL = "select id, title, content, author, issue_time, issue_ip from t_topic order by issue_time desc";

        JdbcHelper h = JdbcHelper.getInstance();
        ResultSet rs = h.query(SQL);
        try{
            while (rs.next()){
                Topic t = this.lookUp(rs);
                list.add(t);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        h.release(rs);
        h.release();

        return list;
    }

    public Topic find(int id) {
        Topic t = new Topic();

        String SQL = "select id, title, content, author, issue_time, issue_ip from t_topic where id = ?";

        JdbcHelper h = JdbcHelper.getInstance();
        ResultSet rs = h.query(SQL);
        try{
            if (rs.next()){
                t = this.lookUp(rs);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        h.release(rs);
        h.release();

        return t;
    }

    private Topic lookUp(ResultSet rs) {
        Topic t = new Topic();

        try{
            t.setId(rs.getInt("id"));
            t.setTitle(rs.getString("title"));
            t.setContent(rs.getString("content"));
            t.setIssueTime(rs.getTimestamp("issue_time"));
            t.setIssueIp(rs.getString("issue_ip"));

            int userId = rs.getInt("author");

            User u = new User();
            u.setId(userId);

            t.setUser(u);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return t;

    }

}