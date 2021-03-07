package com.example.Project4.servlets.regs.types;

import com.example.Project4.system.Listener;
import com.google.gson.Gson;

import data.Boat;
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

@WebServlet(name = "removeType", urlPatterns = "/removeType")
public class RemoveType extends HttpServlet {

    private HttpEngine engine;

     public class Type {
        int id;
         Boat.BoatType type;
    }

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
        java.lang.String inJson=in.lines().collect(Collectors.joining());
        Type inObject=gson.fromJson(inJson, Type.class);

        PrintWriter out = response.getWriter();
        java.lang.String outObject;

        java.lang.String password=null;
        java.lang.String email=null;

        if(request.getSession(false)==null){
            response.sendRedirect("error.html");
        }
        else{
            try {
                email= (java.lang.String) request.getSession().getAttribute("email");
                password= (java.lang.String) request.getSession().getAttribute("password");

                this.engine.connect(email,password);
                this.engine.removeType(inObject.id, inObject.type);
                this.engine.disconnect();

                outObject="removed";
                java.lang.String outJson = gson.toJson(outObject);
                out.println(outJson);
            }
            catch (EngineException e) {
                outObject=e.getError();
                java.lang.String outJson = gson.toJson(outObject);
                out.println(outJson);
            }
        }
    }

    public void destroy() {

    }
}
