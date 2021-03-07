package com.example.Project4.servlets.manager.members;

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
import java.util.Date;
import java.util.stream.Collectors;

@WebServlet(name = "editMember", urlPatterns = "/editMember")
public class EditMember extends HttpServlet {

    private HttpEngine engine;

     public class Member {
         int id;
         String name;
         int age;
         data.Member.Level level;
         Date expiryDate;
         String phone;
         String email;
         String password;
         boolean isManager;
         String notes;
         int serialNumber;
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
        String inJson=in.lines().collect(Collectors.joining());
        Member inObject=gson.fromJson(inJson, Member.class);

        PrintWriter out = response.getWriter();
        String outObject;

        String password=null;
        String email=null;

        if(request.getSession(false)==null){
            response.sendRedirect("error.html");
        }
        else{
            try {
                email= (String) request.getSession().getAttribute("email");
                password= (String) request.getSession().getAttribute("password");

                this.engine.connect(email,password);
                this.engine.editMember(inObject.id,inObject.name,inObject.age,inObject.level,inObject.expiryDate,inObject.phone,inObject.email,inObject.password,inObject.isManager,inObject.serialNumber);
                while(this.engine.getNotes(inObject.id).length>0){
                    this.engine.removeNote(inObject.id,0);
                }
                System.out.println(inObject.notes);
                this.engine.addNote(inObject.email,inObject.notes);
                this.engine.disconnect();

                outObject="edited";
                String outJson = gson.toJson(outObject);
                out.println(outJson);
            }
            catch (EngineException e) {
                outObject=e.getError();
                String outJson = gson.toJson(outObject);
                out.println(outJson);
            }
        }
    }

    public void destroy() {

    }
}
