package com.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoleController {

    @GetMapping("/student")
    public String student() {
        return "role/student";
    }

    @GetMapping("/parent")
    public String parent() {
        return "role/parent";
    }

    @GetMapping("/teacher")
    public String teacher() {
        return "role/teacher";
    }

    @GetMapping("/admin")
    public String admin() {
        return "role/admin";
    }
}