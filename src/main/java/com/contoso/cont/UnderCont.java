package com.contoso.cont;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UnderCont extends Global{

    @GetMapping("/under")
    public String Under(Model model){
        AddAttributesUnder(model);
        return "under";
    }

}
