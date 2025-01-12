package com.groot.boot_practice;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

public class BootTomcat {
    
    public static void main(String[] args) {
        // Spring Container = Application Context 생성
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.registerBean(HelloController.class);
        applicationContext.registerBean(SimpleHelloService.class);
        applicationContext.refresh();
        
        ServletWebServerFactory servletWebServerFactory = new TomcatServletWebServerFactory();
        WebServer webServer = servletWebServerFactory.getWebServer(servletContext -> servletContext.addServlet("hello"
                , new HttpServlet() {
            @Override
            protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())) {
                    HelloController helloController = applicationContext.getBean(HelloController.class);
                    String ret = helloController.hello(req.getParameter("name"));
                    
                    resp.setContentType(MediaType.TEXT_PLAIN_VALUE);
                    resp.getWriter().println(ret);
                } else if (req.getRequestURI().equals("/hello")) {
                    resp.setStatus(HttpStatus.NOT_IMPLEMENTED.value());
                } else {
                    resp.setStatus(HttpStatus.NOT_FOUND.value());
                }
                
            }
        }).addMapping("/*"));
        webServer.start();
    }
    
}