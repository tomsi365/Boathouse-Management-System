package com.example.Project4.servlets.general;

import com.example.Project4.system.Listener;
import com.google.gson.Gson;
import engine.LocalEngine;
import exception.EngineException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(name = "login", urlPatterns = "/login")
public class Login extends HttpServlet {

    private LocalEngine engine;

    public class Connection
    {
        String email;
        String password;
    }

    public void init() {
        this.engine= (LocalEngine) getServletContext().getAttribute(Listener.ENGINE);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        Gson gson=new Gson();

        BufferedReader in=request.getReader();
        String inJson=in.lines().collect(Collectors.joining());
        Connection inObject=gson.fromJson(inJson, Connection.class);

        PrintWriter out = response.getWriter();
        String outObject;

        if(request.getSession(false)!=null){
            outObject = "connected";
            String outJson = gson.toJson(outObject);
            out.println(outJson);
        }
        else
        {
            try {
                this.engine.connect(inObject.email,inObject.password);
                this.engine.disconnect();

                request.getSession().setAttribute("email",inObject.email);
                request.getSession().setAttribute("password",inObject.password);

                outObject = "menu";
                String outJson = gson.toJson(outObject);
                out.println(outJson);
            }
            catch (EngineException e) {
                outObject = e.getError();
                String outJson = gson.toJson(outObject);
                out.println(outJson);
            }
        }
    }

    public void destroy() {

    }
}
