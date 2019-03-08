package com.zhonghong.servlet;

import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/file/upload")
@MultipartConfig(location = "D:/images")
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter w = resp.getWriter();
        w.println("不支持GET上传图片");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part part = req.getPart("uploadImage");
        Map<String,Object> map = new HashMap<>();
        String imageName = part.getSubmittedFileName();
        if (imageName == null || imageName.isEmpty() || part.getSize() == 0){
            map.put("success",false);
            map.put("msg","文件无效");
        } else {
            part.write(imageName);
            map.put("success",true);
            map.put("file_path","/image/show?filename=" + URLEncoder.encode(imageName,"UTF-8"));
        }

        String str = JSON.toJSONString(map);
        resp.setContentType("text/plain:charset=UTF-8");
        PrintWriter w = resp.getWriter();
        w.println(str);

    }
}
