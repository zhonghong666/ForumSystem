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


@WebServlet("/signUp")
public class SignUpServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        String username = req.getParameter("username");
        UserDao ud = new UserDao();
        if (username == null || username.trim().isEmpty()) {
            session.setAttribute("fail", "请输入用户名!!!");
            resp.sendRedirect("/pages/signup.vm");
            return;
        }
        if (ud.exists(username.trim())){
            session.setAttribute("fail", "用户名 " + username + " 已经被占用！！！");
            resp.sendRedirect("/pages/signup.vm");
            return;
        }

        String pwd = req.getParameter("pwd");
        if (pwd == null || pwd.trim().isEmpty()) {
            session.setAttribute("fail", "请输入密码!!!");
            resp.sendRedirect("/pages/signup.vm");
            return;
        }


        String confirm = req.getParameter("confirm");
        if (confirm == null || confirm.trim().isEmpty()){
            session.setAttribute("fail","请再次输入密码!!!");
            resp.sendRedirect("/pages/signup.vm");
            return;
        }
        else if(!confirm.equals(pwd)){
            session.setAttribute("fail","两次密码不一致!!!");
            resp.sendRedirect("/pages/signup.vm");
            return;
        }

        String code = req.getParameter("code");
        if (code == null || code.trim().isEmpty()) {
            session.setAttribute("fail", "请输入验证码!!!");
            resp.sendRedirect("/pages/signup.vm");
            return;
        }
        String yzmCode = (String) session.getAttribute("YZM_CODE");
//        System.out.println(yzmCode);
//        System.out.println(code);
        if (!code.trim().equalsIgnoreCase(yzmCode)) {
            session.setAttribute("fail", "输入的验证码不正确，请重新输入");
            resp.sendRedirect("/pages/signup.vm");
            return;
        }

        UserDao conect = new UserDao();
        User u = new User();

        u.setUsername(username.trim());

        pwd = EncryptHelper.encrypt(pwd.trim(), EncryptType.SHA1);
        u.setPassword(pwd);
        boolean e = conect.save(u);
        if (e) {
            session.setAttribute("success", "注册成功，欢迎登录");
            resp.sendRedirect("/pages/signin.vm");
        } else {
            session.setAttribute("fail", "注册失败，请重试");
            resp.sendRedirect("/pages/signup.vm");
        }


//        resp.setContentType("text/html;charset=UTF-8");
//        PrintWriter w = resp.getWriter();
//        w.println("<h1 align='center'>你好," + username + "注册成功！！！</h1>");

    }
}
