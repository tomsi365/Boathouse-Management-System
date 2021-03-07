package com.example.Project4.servlets.general;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "logout", urlPatterns = "/logout")
public class Logout extends HttpServlet {

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        if(request.getSession(false)==null){
            response.sendRedirect("error.html");
        }
        else
        {
            request.getSession().invalidate();
            response.sendRedirect("index.html");
        }
    }

    public void destroy() {

    }
}
