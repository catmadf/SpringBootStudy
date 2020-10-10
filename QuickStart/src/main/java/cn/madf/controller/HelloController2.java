package cn.madf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author dell
 * @date 2020/10/07
 * @copyright© 2020
 */
@Controller
public class HelloController2 {

    @GetMapping("/hello2")
    public String hello2() {
        return "hello";
    }
}
