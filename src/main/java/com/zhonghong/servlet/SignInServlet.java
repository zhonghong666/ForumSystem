package com.zhonghong.servlet;


import com.zhonghong.dao.UserDao;
import com.zhonghong.entity.User;
import com.zhonghong.helper.EncryptHelper;
import com.zhonghong.helper.EncryptType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/signIn")
public class SignInServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        String username = req.getParameter("username");
        UserDao ud = new UserDao();

        if (username == null || username.trim().isEmpty()) {
            session.setAttribute("fail", "请输入用户名!!!");
            resp.sendRedirect("/pages/signin.vm");
            return;
        }

        User us = ud.find(username.trim());
        if (!us.getUsername().equals(username.trim())){
            session.setAttribute("fail", "用户名错误,请重新输入");
            resp.sendRedirect("/pages/signin.vm");
            return;
        }

        String pwd = req.getParameter("pwd");
        if (pwd == null || pwd.trim().isEmpty()) {
            session.setAttribute("fail", "请输入密码!!!");
            resp.sendRedirect("/pages/signin.vm");
            return;
        }else if (!us.getPassword().equals(EncryptHelper.encrypt(pwd.trim(), EncryptType.SHA1))) {
            session.setAttribute("fail", "密码错误，请重新输入");
            resp.sendRedirect("/pages/signin.vm");
            return;
        }

        String code = req.getParameter("code");
        if (code == null || code.trim().isEmpty()) {
            session.setAttribute("fail", "请输入验证码!!!");
            resp.sendRedirect("/pages/signin.vm");
            return;
        }
        String yzmCode = (String) session.getAttribute("YZM_CODE");
//        System.out.println(yzmCode);
//        System.out.println(code);
        if (!code.trim().equalsIgnoreCase(yzmCode)) {
            session.setAttribute("fail", "输入的验证码不正确，请重新输入");
            resp.sendRedirect("/pages/signin.vm");
            return;
        }

//        resp.setContentType("text/html;charset=UTF-8");
//        PrintWriter w = resp.getWriter();
//        w.println("<h1 align='center'>你好," + username + "</h1>");
        session.setAttribute("USER",us);
        resp.sendRedirect("/pages/index.vm");

    }
}
