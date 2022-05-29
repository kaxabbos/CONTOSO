package com.contoso.cont;

import com.contoso.cont.general.Attributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DeleteOrderCont extends Attributes {

    @GetMapping("/orders/{idOrder}/delete")
    public String OrderDelete1(@PathVariable Long idOrder) {
        DeleteOrderAndOrderDetails(idOrder);
        return "redirect:/orders";
    }

    @GetMapping("/payments/{idOrder}/delete")
    public String OrderDelete2(@PathVariable Long idOrder) {
        DeleteOrderAndOrderDetailsAndStatProduct(idOrder);
        return "redirect:/payments";
    }

    @GetMapping("/shipment/{idOrder}/delete")
    public String OrderDelete3(@PathVariable Long idOrder) {
        DeleteOrderAndOrderDetailsAndStatProduct(idOrder);
        return "redirect:/shipment";
    }

    @GetMapping("/shipped/{idOrder}/delete")
    public String OrderDelete4(@PathVariable Long idOrder) {
        DeleteOrderAndOrderDetails(idOrder);
        return "redirect:/shipped";
    }

    @GetMapping("/open/{idOrder}/delete")
    public String OrderDelete5(@PathVariable Long idOrder) {
        DeleteOrderAndOrderDetails(idOrder);
        return "redirect:/open";
    }

    @GetMapping("/under/{idOrder}/delete")
    public String OrderDelete6(@PathVariable Long idOrder) {
        DeleteOrderAndOrderDetails(idOrder);
        return "redirect:/under";
    }
}
