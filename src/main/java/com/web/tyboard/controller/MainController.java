package com.web.tyboard.controller;

import com.web.tyboard.question.Question1;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    @RequestMapping("/board")
    @ResponseBody
    public String index() {
        return "BOARD 프로젝트입니다.";
    }

    @RequestMapping("/")
    public String root() {

        return "index";
    }

}