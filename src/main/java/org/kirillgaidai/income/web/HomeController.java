package org.kirillgaidai.income.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showIndexPage() {
        return "home";
    }

    @RequestMapping("/{page}")
    public String getCategory(@PathVariable("page") String page) {
        return page;
    }

}
