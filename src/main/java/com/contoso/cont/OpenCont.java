package com.contoso.cont;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OpenCont extends Global {

    @GetMapping("/open")
    public String Open(Model model){
        addAttributesOpen(model);
        return "open";
    }

}
