package com.contoso.cont;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutCont extends Global {

    @GetMapping("/about")
    public String About(Model model) {
        AddAttributes(model);
        return "about";
    }
}
