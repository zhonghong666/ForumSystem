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
import java.sql.Timestamp;


@WebServlet("/topic/update")
public class TopicUpdateServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        int id = Integer.parseInt(req.getParameter("id"));
        Object o = session.getAttribute("USER");
        if (o instanceof User) {
            User u = (User)o;
            String title = req.getParameter("title").trim();
            String content = req.getParameter("content");

            if (title == null || title.isEmpty()) {
                session.setAttribute("fail", "请输入标题!!!");
                resp.sendRedirect("/pages/change.vm");
                return;
            }

            if (content == null || content.isEmpty()) {
                session.setAttribute("fail", "请输入正文!!!");
                resp.sendRedirect("/pages/change.vm");
                return;
            }
            TopicDao td = new TopicDao();

            if (td.find(id).getUser().getId() != u.getId()){
                session.setAttribute("fail", "你不是作者，无权修改!!!");
                resp.sendRedirect("/pages/detail.vm");
                return;
            }
            String ip = req.getRemoteAddr();
            Topic t = new Topic();
            t.setTitle(title);
            t.setContent(content);
            t.setIssueIp(ip);
            t.setId(id);
            td.update(t,id);
            resp.sendRedirect("/topic/list");

//            resp.sendRedirect("/topic/list");

//            resp.sendRedirect("/pages/detail.vm");
        }else {
            session.setAttribute("fail", "先登录再进行操作!!!");
            resp.sendRedirect("/pages/signin.vm");
        }
    }
}
