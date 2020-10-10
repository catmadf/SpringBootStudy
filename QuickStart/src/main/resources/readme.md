
1.添加Spring配置

~~~java
@Configuration
@ComponentScan(basePackages = "cn.madf", useDefaultFilters = true,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Controller.class)})
public class SpringConfig {
}
~~~

2.添加SpringMVC配置

~~~java
@Configuration
@ComponentScan(basePackages = "cn.madf", useDefaultFilters = false, 
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Controller.class)})
public class SpringMVCConfig {
}
~~~

3.添加web.xml配置，用java代码代替
~~~java
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
~~~
> 在 WebInit 中只添加了 SpringMVC 的配置，因此不会去加载 Spring 容器，如果一定要加载 Spring 容器，则需要修改 SpringMVC 配置，在 SpringMVC 配置的包扫描中也去扫描 @Configuration 注解，直接将所有配置放到 SpringMVC 配置中来完成。实际开发中通常使用如下做法：
  ~~~java
  @Configuration
  @ComponentScan(basePackages = "cn.madf")
  public class SpringMVCConfig {
  }
  ~~~
4.配置 tomcat 启动项目后输入 http://localhost:8080/hello 可以显示结果
~~~java
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
~~~
5.静态资源过滤

(1)在 xml 中静态资源过滤配置如下：
 ~~~xml
 <mvc:resources mapping="/**" location="/" />
 ~~~
 
(2)在 java 中配置 SSM 环境中，要配置静态资源过滤，需要让 SpringMVC 继承 WebMvcConfigurationSupport ，进而重写 WebMvcConfigurationSupport 中的方法，如下
 ~~~java
 @Configuration
 @ComponentScan(basePackages = "cn.madf")
 public class SpringMVCConfig extends WebMvcConfigurationSupport {
     @Override
     protected void addResourceHandlers(ResourceHandlerRegistry registry) {
         registry.addResourceHandler("/js/**").addResourceLocations("classpath:/");
     }
 }
 ~~~
 如果采用 java 来配置 SSM 环境，一般来说，可以不必使用 webapp 目录，除非要使用 JSP 做页面模板，否则可以忽略 webapp，目录

6.视图解析器

(1)在 xml 中视图解析器配置如下：
~~~xml
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/jsp/" />
    <property name="suffix" value=".jsp" />
</bean>
~~~

(2)通过 java 类配置视图解析器
添加 webapp 目录和 jsp 子目录，jsp 子目录中存放 jsp 文件：
~~~shell
___ webapp
  |__ jsp
     |__ hello.jsp
~~~
Project Structure -> modules : 添加 web 目录 (web resource directory)
引入 jsp 依赖：
~~~xml
<dependency>
    <groupId>javax.servlet.jsp</groupId>
    <artifactId>javax.servlet.jsp-api</artifactId>
    <version>2.3.1</version>
</dependency>
~~~
在配置类中重写方法：
~~~java
@Configuration
@ComponentScan(basePackages = "cn.madf")
public class SpringMVCConfig extends WebMvcConfigurationSupport {
    @Override
    protected void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/jsp/", ".jsp");
    }
}
~~~
Controller 中添加控制器访问 jsp 页面
~~~java
@Controller
public class HelloController2 {
    @GetMapping("/hello2")
    public String hello2() {
        return "hello";
    }
}
~~~

7.路径映射
在6中的控制器实际上只是一个跳转操作，没有任何业务逻辑，对于这种情况，可以直接通过路径映射来实现页面访问。在 xml 中配置路径映射，如下：
~~~xml
<mvc:view-controller path="/hello" view-name="hello" status-code="200" />
~~~

同样的，也可以直接使用java代码进行配置：
~~~java
@Configuration
@ComponentScan(basePackages = "cn.madf")
public class SpringMVCConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/hello3").setViewName("hello");
    }
}
~~~

8. JSON 配置
SpringMVC 可以接受返回 JSON参数，依赖于 HttpMessageConverter。
它可以实现 JSON 字符串和对象之间的相互转化，实际上底层依然依赖与具体的 JSON 库
所有的 JSON 库要在 SpringMVC 中自动返回和接受，都必须提供和自己相关的 HttpMessageConverter。
SpringMVC 中，默认提供了 Jackson 和 gson 的 HttpMessageConverter，分别是 MappingJackson2HttpMessageConverter 和 GsonHttpMessageConverter。
添加依赖后即可使用，具体配置在 AllEncompassingFormHttpMessageConverter 类中完成。
如果使用 fastjson，默认不提供 HttpMessageConverter ，需要自己提供。

在 xml 配置中，fastjson 除了添加相关依赖外，需要显示指明 HttpMessageConverter，如下：
~~~xml
<mvc:annotation-driven>
    <mvc:message-converters>
        <bean class="com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter" >
        </bean>
    </mvc:message-converters>
</mvc:annotation-driven>
~~~

在 java 代码中配置，如下：
 ~~~java
 @Configuration
 @ComponentScan(basePackages = "cn.madf")
 public class SpringMVCConfig extends WebMvcConfigurationSupport {
     @Override
         protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
             FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
             converter.setDefaultCharset(Charset.forName("UTF-8"));
             FastJsonConfig fastJsonConfig = new FastJsonConfig();
             fastJsonConfig.setCharset(Charset.forName("UTF-8"));
             converter.setFastJsonConfig(fastJsonConfig);
             converters.add(converter);
         }
 }
 ~~~