package org.kirillgaidai.income.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rest/home")
public class HomeRest {

    @RequestMapping("/{page}")
    public String getCategory(@PathVariable("page") String page) {
        return page;
    }

}
