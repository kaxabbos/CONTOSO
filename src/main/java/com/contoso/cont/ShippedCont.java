package com.contoso.cont;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShippedCont extends Global {

    @GetMapping("/shipped")
    public String Shipped(Model model) {
        addAttributesShipped(model);
        return "shipped";
    }

}
