package com.example.Project4.servlets.general;

import com.example.Project4.system.Listener;
import com.google.gson.Gson;
import engine.HttpEngine;
import engine.LocalEngine;
import exception.EngineException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "status", urlPatterns = "/status")
public class Status extends HttpServlet {

    private HttpEngine engine;

    public void init() {
        this.engine= (HttpEngine) getServletContext().getAttribute(Listener.ENGINE);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        Gson gson=new Gson();

        PrintWriter out = response.getWriter();
        String outObject;

        String password=null;
        String email=null;
        boolean flag=false;

        if(request.getSession(false)==null){
            outObject = "not connected";
            String outJson = gson.toJson(outObject);
            out.println(outJson);
        }
        else{
            try {
                email = (String) request.getSession().getAttribute("email");
                password = (String) request.getSession().getAttribute("password");

                this.engine.connect(email, password);
                flag=this.engine.isManagerConnected();
                this.engine.disconnect();

                if(flag){
                    outObject = "manager";
                    String outJson = gson.toJson(outObject);
                    out.println(outJson);
                }
                else{
                    outObject = "user";
                    String outJson = gson.toJson(outObject);
                    out.println(outJson);
                }
            }
            catch (EngineException e) {
                response.sendRedirect("error.html");
            }
        }
    }

    public void destroy() {

    }
}
