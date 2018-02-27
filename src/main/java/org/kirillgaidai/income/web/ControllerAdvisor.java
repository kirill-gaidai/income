package org.kirillgaidai.income.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ControllerAdvisor {

    final private static Logger LOGGER = LoggerFactory.getLogger(ControllerAdvisor.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle(Exception ex) {
        LOGGER.debug("Entering method");
        return "redirect:/index.html";
    }

}
