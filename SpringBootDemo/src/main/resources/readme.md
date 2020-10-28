1.Spring Boot 中 parent 的作用
    (1) 定义了 java 的编译版本为1.8；
    (2) 使用了 UTF-8 格式编码；
    (3) 继承自 spring-boot-dependencies ，定义了依赖的版本，也正是因为继承了这个依赖，所以在写依赖时不需要写版本号
    (4) 执行打包操作的配置
    (5) 自动化资源过滤和插件配置
    (6) 针对 application.properties 和 application.yml 的资源过滤，包括通过 profile 定义的不同环境的配置文件，包括 application-dev.properties 和 application-dev.yml

2.源码分析
通过阅读 spring-boot-starter-parent-2.1.8.RELEASE.pom 可以发现
~~~xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-dependencies</artifactId>    <!-- 继承自 spring-boot-dependencies -->
    <version>2.1.8.RELEASE</version>                     <!-- 统一版本 -->
    <relativePath>../../spring-boot-dependencies</relativePath>
</parent>
<artifactId>spring-boot-starter-parent</artifactId>
<packaging>pom</packaging>
<name>Spring Boot Starter Parent</name>
<description>Parent pom providing dependency and plugin management for applications
    built with Maven</description>
<url>https://projects.spring.io/spring-boot/#/spring-boot-starter-parent</url>
<properties>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <java.version>1.8</java.version>
    <resource.delimiter>@</resource.delimiter>   <!-- maven 过滤占位符 -->
    <maven.compiler.source>${java.version}</maven.compiler.source>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.target>${java.version}</maven.compiler.target>
</properties>
~~~
继承自 spring-boot-dependencies，内保存了基本的依赖信息
~~~xml
<properties>
    <activemq.version>5.15.10</activemq.version>
    <antlr2.version>2.7.7</antlr2.version>
    <appengine-sdk.version>1.9.76</appengine-sdk.version>
    <artemis.version>2.6.4</artemis.version>
    <aspectj.version>1.9.4</aspectj.version>
    ...
    </properties>
    <dependencyManagement>
        <dependencies>
          <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot</artifactId>
            <version>2.1.8.RELEASE</version>
          </dependency>
          ...
          <dependency>
              <groupId>xml-apis</groupId>
              <artifactId>xml-apis</artifactId>
              <version>${xml-apis.version}</version>
           </dependency>
        </dependencies>
    </dependencyManagement>
~~~
故而 Spring Boot 项目中部分依赖不需要写版本号

3.不用 Spring 自带 parent
有时公司会要求继承自公司内部的 parent，只需要向上边提到的 xml 一样自定义 dependencyManagement 节点即可，例如
~~~xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>2.1.8.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
~~~
版本问题解决后，还需要去定义打包的插件、编译的 jdk 版本、文件编码格式等等。