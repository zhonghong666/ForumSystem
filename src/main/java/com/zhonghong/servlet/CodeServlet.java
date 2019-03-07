package com.zhonghong.servlet;

import com.zhonghong.helper.GraphicHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/code/*")
public class CodeServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        OutputStream out = resp.getOutputStream();
        String code = GraphicHelper.create(5,false,220,50,out,50);
        session.setAttribute("YZM_CODE",code);
    }
}
