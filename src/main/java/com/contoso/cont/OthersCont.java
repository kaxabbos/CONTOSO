package com.contoso.cont;

import com.contoso.cont.general.Attributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OthersCont extends Attributes {

    @GetMapping("/login")
    public String Login() {
        return "login";
    }

    @GetMapping("/orders/{id}/details")
    public String Details(Model model, @PathVariable Long id) {
        AddAttributesDetails(model, id);
        return "details";
    }

    @GetMapping("/about")
    public String About(Model model) {
        AddAttributes(model);
        return "about";
    }

    @GetMapping("/waiting")
    public String waiting(Model model) {
        AddAttributeswaiting(model);
        return "waiting";
    }

    @GetMapping("/shipment")
    public String Shipment(Model model) {
        AddAttributesShipment(model);
        return "shipment";
    }

    @GetMapping("/shipped")
    public String Shipped(Model model) {
        AddAttributesShipped(model);
        return "shipped";
    }
}
