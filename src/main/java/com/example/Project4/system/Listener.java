package com.example.Project4.system;

import engine.HttpEngine;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Listener implements ServletContextListener {

    public static final java.lang.String ENGINE="engine";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HttpEngine engine=new HttpEngine();
        sce.getServletContext().setAttribute(Listener.ENGINE,engine);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HttpEngine engine= (HttpEngine) sce.getServletContext().getAttribute(Listener.ENGINE);
        engine.exit();
    }
}
