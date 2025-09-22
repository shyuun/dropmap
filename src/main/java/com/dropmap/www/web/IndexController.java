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
        return mv;
    }

    @GetMapping("/info/{pageName}")
    public ModelAndView info(@PathVariable String pageName) {
        ModelAndView mv = new ModelAndView("info");
        mv.addObject("gbn",pageName);
        return mv;
    }
}
