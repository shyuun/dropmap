package com.dropmap.www.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IndexController {

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "welcome");
        return mv;
    }

    @GetMapping("/{num}")
    public ModelAndView goNumber(@PathVariable String num) {
        return new ModelAndView("/error/"+num);
    }
}
