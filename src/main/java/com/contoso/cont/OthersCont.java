package com.contoso.cont;

import com.contoso.cont.general.AddAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OthersCont extends AddAttribute {

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

    @GetMapping("/open")
    public String Open(Model model) {
        AddAttributesOpen(model);
        return "open";
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

    @GetMapping("/under")
    public String Under(Model model) {
        AddAttributesUnder(model);
        return "under";
    }
}
