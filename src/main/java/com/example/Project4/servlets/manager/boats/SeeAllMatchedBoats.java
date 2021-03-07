package com.example.Project4.servlets.manager.boats;

import com.example.Project4.system.Listener;
import com.google.gson.Gson;
import engine.HttpEngine;
import exception.EngineException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(name = "seeAllMatchedBoats", urlPatterns = "/seeAllMatchedBoats")
public class SeeAllMatchedBoats extends HttpServlet {

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

        BufferedReader in=request.getReader();
        String inJson=in.lines().collect(Collectors.joining());
        String inObject=gson.fromJson(inJson, String.class);

        PrintWriter out = response.getWriter();
        String[][] outObject;

        String password=null;
        String email=null;
        boolean flag=false;

        if(request.getSession(false)==null){
            response.sendRedirect("error.html");
        }
        else{
            try {
                email= (String) request.getSession().getAttribute("email");
                password= (String) request.getSession().getAttribute("password");

                this.engine.connect(email,password);
                flag=this.engine.isManagerConnected();
                outObject=this.engine.getMatchedBoats(Integer.parseInt(inObject));
                this.engine.disconnect();

                if(!flag){
                    response.sendRedirect("error.html");
                }
                else{
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
