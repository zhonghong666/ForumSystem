package com.zhonghong.servlet;

import com.zhonghong.dao.TopicDao;
import com.zhonghong.dao.UserDao;
import com.zhonghong.entity.Topic;
import com.zhonghong.entity.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


@WebServlet("/topic/list")
public class TopicListServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext application = req.getServletContext();
        TopicDao td = new TopicDao();
        UserDao ud = new UserDao();

        List<Topic> list = td.findAll();

        for(Iterator<Topic> it = list.iterator(); it.hasNext();)
        {
            Topic t = it.next();
            User u = t.getUser();
            int userId = u.getId();
            User user = ud.find(userId);
            t.setUser(user);

        }

        application.setAttribute("topicList",list);
        resp.sendRedirect("/pages/show.vm");

    }
}
