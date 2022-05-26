package com.contoso.cont;

import com.contoso.cont.general.AddAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DeleteOrderCont extends AddAttribute {

    @GetMapping("/orders/{id}/delete")
    public String OrderDelete1(@PathVariable Long id) {
        OrderDelete(id);
        return "redirect:/orders";
    }

    @GetMapping("/payments/{id}/delete")
    public String OrderDelete2(@PathVariable Long id) {
        OrderAndStatProductDelete(id);
        return "redirect:/payments";
    }

    @GetMapping("/shipment/{id}/delete")
    public String OrderDelete3(@PathVariable Long id) {
        OrderAndStatProductDelete(id);
        return "redirect:/shipment";
    }

    @GetMapping("/shipped/{id}/delete")
    public String OrderDelete4(@PathVariable Long id) {
        OrderDelete(id);
        return "redirect:/shipped";
    }

    @GetMapping("/open/{id}/delete")
    public String OrderDelete5(@PathVariable Long id) {
        OrderDelete(id);
        return "redirect:/open";
    }

    @GetMapping("/under/{id}/delete")
    public String OrderDelete6(@PathVariable Long id) {
        OrderDelete(id);
        return "redirect:/under";
    }
}
