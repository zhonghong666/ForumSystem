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


@WebServlet("/topic/change")
public class TopicChangeServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        int id = Integer.parseInt(req.getParameter("id"));

        TopicDao td = new TopicDao();
        User u = (User)session.getAttribute("USER");

        if (u == null){
            session.setAttribute("fail", "先登录，再进行操作!!!");
            resp.sendRedirect("/pages/signin.vm");
        }

        if (td.find(id).getUser().getId() != u.getId()){
            session.setAttribute("fail", "你不是作者，无权修改!!!");
            resp.sendRedirect("/pages/detail.vm");
            return;
        }
        UserDao ud = new UserDao();

        Topic t = td.find(id);
        u = t.getUser();
        int userId = u.getId();
        User user = ud.find(userId);
        t.setUser(user);

        session.setAttribute("change",t);

        resp.sendRedirect("/pages/change.vm");
    }
}
