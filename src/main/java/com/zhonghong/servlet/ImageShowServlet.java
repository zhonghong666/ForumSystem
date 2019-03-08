package com.zhonghong.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@WebServlet("/image/show")
public class ImageShowServlet extends HttpServlet {

    private static final String filePath = "D:/images";
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String filename = req.getParameter("filename");
        Path path = Paths.get(filePath,filename);

        if (Files.exists(path) && Files.isRegularFile(path)) {
            resp.setContentType("image/plain");
            OutputStream out = resp.getOutputStream();
            Files.copy(path,out);
        }
    }
}
