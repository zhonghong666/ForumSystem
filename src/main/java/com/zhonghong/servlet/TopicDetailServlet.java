package com.zhonghong.servlet;

import com.zhonghong.dao.TopicDao;
import com.zhonghong.dao.UserDao;
import com.zhonghong.entity.Topic;
import com.zhonghong.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/topic/detail")
public class TopicDetailServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        int id = Integer.parseInt(req.getParameter("id"));

        TopicDao td = new TopicDao();
        UserDao ud = new UserDao();

        Topic t = td.find(id);
        User u = t.getUser();
        int userId = u.getId();
        User user = ud.find(userId);
        t.setUser(user);

        session.setAttribute("topic",t);

        resp.sendRedirect("/pages/detail.vm");
    }
}
