package com.example.Project4.system;

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

@WebServlet(name = "init", urlPatterns = "/init")
public class Init extends HttpServlet {

    private HttpEngine engine;

    public void init() {
        this.engine= (HttpEngine) getServletContext().getAttribute(Listener.ENGINE);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        try {
            engine.connect(HttpEngine.MASTER_EMAIL,HttpEngine.MASTER_PASSWORD);
            engine.init();
            engine.disconnect();
        }
        catch (EngineException e) {
            response.sendRedirect("menu.html");
        }
        response.sendRedirect("menu.html");
    }

    public void destroy() {

    }
}
