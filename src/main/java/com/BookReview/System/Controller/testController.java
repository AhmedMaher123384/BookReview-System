package com.BookReview.System.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    @GetMapping("/hi")
    public String hi(){return "holla";}
}
