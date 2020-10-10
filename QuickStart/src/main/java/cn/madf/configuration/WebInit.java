package cn.madf.configuration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * @author dell
 * @date 2020/10/06
 * @copyright© 2020
 */
public class WebInit implements WebApplicationInitializer {
    // 项目启动时，onStartup 方法会被自动执行，可以在这个方法中做一些初始化工作，如加载 SpringMVC 容器，添加过滤器，添加Listener、Servlet等
    public void onStartup(ServletContext servletContext) throws ServletException {
        // 加载 SpringMVC 配置文件
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(SpringMVCConfig.class);
        // 添加 DispatcherServlet
        ServletRegistration.Dynamic springmvc = servletContext.addServlet("springmvc", new DispatcherServlet(ctx));
        // 配置 DispatcherServlet 的映射路径
        springmvc.addMapping("/");
        springmvc.setLoadOnStartup(1);
    }
}
