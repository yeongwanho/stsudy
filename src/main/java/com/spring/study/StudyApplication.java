package com.spring.study;

import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@SpringBootApplication
public class StudyApplication {

    public static void main(String[] args) {
//		SpringApplication.run(StudyApplication.class, args);

        GenericApplicationContext applicationContext = new GenericApplicationContext();

        applicationContext.registerBean(HelloController.class);
        applicationContext.registerBean(SimpleHelloService.class);
        //초기화
        applicationContext.refresh();
        System.out.println("Hello controller");
        ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
        WebServer webServer = serverFactory.getWebServer(
                servletContext -> {

                    servletContext.addServlet("frontController", new HttpServlet() {
                        @Override
                        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

                            if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())) {

                                String name = req.getParameter("name");

                                HelloController helloController = applicationContext.getBean(HelloController.class);
                                String ret = helloController.hello(name);

                                // 웹 응답의 3가지 요소
                                // 1 상태코드
//							resp.setStatus(200);
                                resp.setStatus(HttpStatus.OK.value());
                                // 2 헤더  3 바디
//                                resp.setHeader("Content-Type", "text/plain");
//                                resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
                                resp.setContentType(MediaType.TEXT_PLAIN_VALUE);
                                resp.getWriter().println(ret);
                            } else if (req.getRequestURI().equals("/user")) {
                                //
                            } else {
                                resp.setStatus(HttpStatus.NOT_FOUND.value());
                            }

                        }
                    }).addMapping("/*");
                }
        );// 서버를 만드는 생성 함수
        webServer.start();

    }

}
