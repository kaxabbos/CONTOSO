package com.contoso.cont;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailsCont extends Global {

    @GetMapping("/orders/{id}/details")
    public String Details(Model model, @PathVariable Long id) {
        AddAttributesDetails(model,id);
        return "details";
    }
}
