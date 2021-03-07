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
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "seeAllTypes", urlPatterns = "/seeAllTypes")
public class SeeAllTypes extends HttpServlet {

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
        java.lang.String[] outObject;

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
                outObject=new java.lang.String[7];
                outObject[0]= java.lang.String.valueOf(Boat.BoatType.SINGLE);
                outObject[1]= java.lang.String.valueOf(Boat.BoatType.DOUBLE);
                outObject[2]= java.lang.String.valueOf(Boat.BoatType.DOUBLE_ONE_PADDLE);
                outObject[3]= java.lang.String.valueOf(Boat.BoatType.QUARTET);
                outObject[4]= java.lang.String.valueOf(Boat.BoatType.QUARTET_ONE_PADDLE);
                outObject[5]= java.lang.String.valueOf(Boat.BoatType.EIGHT);
                outObject[6]= java.lang.String.valueOf(Boat.BoatType.EIGHT_ONE_PADDLE);
                this.engine.disconnect();

                java.lang.String outJson = gson.toJson(outObject);
                out.println(outJson);
            }
            catch (EngineException e) {
                response.sendRedirect("error.html");
            }
        }
    }

    public void destroy() {

    }
}
