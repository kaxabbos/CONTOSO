package com.contoso.cont;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShipmentCont extends Global {

    @GetMapping("/shipment")
    public String Shipment(Model model) {
        addAttributesShipment(model);
        return "shipment";
    }

}
