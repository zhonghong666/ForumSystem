package com.zhonghong.servlet;

import com.zhonghong.dao.TopicDao;
import com.zhonghong.entity.Topic;
import com.zhonghong.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/topic/issue")
public class TopicIssueServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Object o = session.getAttribute("USER");
        if (o instanceof User){
            User u = (User)o;
            String title = req.getParameter("title").trim();
            String content =req.getParameter("content");

            if (title == null || title.isEmpty()) {
                session.setAttribute("fail", "请输入标题!!!");
                resp.sendRedirect("/pages/issue.vm");
                return;
            }

            if (content == null || content.isEmpty()) {
                session.setAttribute("fail", "请输入正文!!!");
                resp.sendRedirect("/pages/issue.vm");
                return;
            }
            String ip = req.getRemoteAddr();
            Topic t = new Topic();
            t.setTitle(title);
            t.setContent(content);
            t.setIssueIp(ip);
            t.setUser(u);

            TopicDao td = new TopicDao();
            td.save(t);
            resp.sendRedirect("/topic/list");

        } else {
            session.setAttribute("fail", "请先登录！！！");
            resp.sendRedirect("/pages/signin.vm");
        }
    }
}
