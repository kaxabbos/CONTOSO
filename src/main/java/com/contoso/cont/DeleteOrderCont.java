package com.contoso.cont;

import com.contoso.cont.general.Attributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DeleteOrderCont extends Attributes {

    @GetMapping("/orders/{idOrder}/delete")
    public String OrderDelete(@PathVariable Long idOrder) {
        DeleteOrderAndOrderDetails(idOrder);
        return "redirect:/orders";
    }

    @GetMapping("/payments/{idOrder}/delete")
    public String OrderDeletePayments(@PathVariable Long idOrder) {
        DeleteOrderAndOrderDetailsAndStatProduct(idOrder);
        return "redirect:/payments";
    }

    @GetMapping("/shipment/{idOrder}/delete")
    public String OrderDeleteShipment(@PathVariable Long idOrder) {
        DeleteOrderAndOrderDetailsAndStatProduct(idOrder);
        return "redirect:/shipment";
    }

    @GetMapping("/shipped/{idOrder}/delete")
    public String OrderDeleteShipped(@PathVariable Long idOrder) {
        DeleteOrderAndOrderDetails(idOrder);
        return "redirect:/shipped";
    }

    @GetMapping("/waiting/{idOrder}/delete")
    public String OrderDeleteWaiting(@PathVariable Long idOrder) {
        DeleteOrderAndOrderDetails(idOrder);
        return "redirect:/waiting";
    }
}
