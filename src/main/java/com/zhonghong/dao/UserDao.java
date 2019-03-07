package com.zhonghong.dao;


import com.zhonghong.entity.User;
import com.zhonghong.helper.JdbcHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 负责勾搭 User 和 t_users 的桥梁
 */
public class UserDao {

    public static void main(String[] args) {

        UserDao ud = new UserDao();

        User u = ud.find( "lishi" );

        System.out.println( u.getId() + " , " + u.getUsername() + " , " + u.getPassword() );

        System.out.println( u.getTel() + " , " + u.getEmail() );

        User user = new User();
        user.setUsername( "lishi" );
        user.setPassword( "world" );
//        user.setTel("15579872216");
//        user.setEmail("1789592159@qq.com");
        ud.save( user );

    }

    /**
     * 保存用户对象中的数据到数据库表中
     * @param u 被保存的用户对象
     * @return 当保存成功时返回 true 否则返回 false
     */
    public boolean save( User u ) {
        // 1、调用 JdbcHelper 类的 getInstance 方法来获取 JdbcHelper 实例
        JdbcHelper h = JdbcHelper.getInstance();
        // 2、执行操作 ( 插入)
        String SQL = "insert into t_user ( username , password ) values ( ? , ? )" ;
        int count = h.update( SQL , u.getUsername() ,  u.getPassword() );
        // 3、释放资源
        h.release();
        return count == 1 ;
    }

    public User find( String username ) {
        String SQL = "SELECT id , username , password , tel , email FROM t_user WHERE username = ? " ;
        User u = this.lookUp(SQL, username);
        return u ;
    }

    public User find( int id ) {
        String SQL = "SELECT id , username , password , tel , email FROM t_user WHERE id = ? " ;
        User u = this.lookUp(SQL, id);
        return u ;
    }

    private User warp(ResultSet rs){
        User u = new User();
        try{
            u.setId(rs.getInt("id"));
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            u.setTel(rs.getString("tel"));
            u.setEmail(rs.getString("email"));

        } catch (SQLException e){
            e.printStackTrace();
        }
        return u;
    }

    private User lookUp(String SQL, Object param){
        User u = null;
        JdbcHelper h = JdbcHelper.getInstance();
        ResultSet rs = h.query( SQL , param );
        try{
            if (rs.next()){
                u = this.warp(rs);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        h.release(rs);
        h.release();

        return u;
    }

    public boolean exists(String username){
        // 1、调用 JdbcHelper 类的 getInstance 方法来获取 JdbcHelper 实例
        JdbcHelper h = JdbcHelper.getInstance();
        // 2、执行操作 ( 插入)
        String SQL = "select 1 from t_user where username = ?" ;
        ResultSet rs = h.query( SQL , username );
        boolean res = false;
        try {
            res = rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // 3、释放资源
        h.release(rs);
        h.release();
        return res;
    }

}
