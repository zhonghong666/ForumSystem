package com.zhonghong.servlet;

import com.zhonghong.dao.TopicDao;
import com.zhonghong.dao.UserDao;
import com.zhonghong.entity.Topic;
import com.zhonghong.entity.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.List;


@WebServlet(urlPatterns = "/prepard", loadOnStartup = 1)
public class PreparedServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        System.err.println( "PreparedServlet 的 init 方法执行..." );

        // 获取当前 Servlet 对应的 上下文( context )对象 ( 与 JSP 中的内置对象 application 对应 )
        ServletContext application = this.getServletContext();

        // 创建 TopicDao 对象
        TopicDao td = new TopicDao();
        // 创建 UserDao 对象
        UserDao ud = new UserDao();

        // 查询所有的 Topic 并返回一个根据发布时间排序的 List 集合
        List<Topic> list = td.findAll() ;

        // 迭代 list 集合并处理每个 Topic 的提问者
        for( Topic t : list ) {
            User u = t.getUser();
            int uid = u.getId() ;
            User user = ud.find( uid );
            t.setUser( user );
        }

        // 将从数据库中查询返回的 Topic 集合设置到 application 对象中
        application.setAttribute( "topicList" , list );
    }
}
