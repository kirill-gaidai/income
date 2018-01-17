package org.kirillgaidai.income.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showIndexPage() {
        return "home";
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public String getAccountsPage() {
        return "accounts";
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public String getCategoriesPage() {
        return "categories";
    }

    @RequestMapping(value = "/currencies", method = RequestMethod.GET)
    public String getCurrenciesPage() {
        return "currencies";
    }

    @RequestMapping(value = "/summaries", method = RequestMethod.GET)
    public String getSummariesPage() {
        return "summaries";
    }

}
