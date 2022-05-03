package com.contoso.cont;

import com.contoso.models.OrderDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class DeleteOrderCont extends Global {

    @GetMapping("/orders/{id}/delete")
    public String OrderDelete1(@PathVariable Long id) {
        repoOrders.deleteById(id);
        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrders(id);
        for (OrderDetails i : orderDetailsList) {
            repoOrderDetails.deleteById(i.getId());
        }
        return "redirect:/orders";
    }

    @GetMapping("/payments/{id}/delete")
    public String OrderDelete2(@PathVariable Long id) {
        repoOrders.deleteById(id);
        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrders(id);
        for (OrderDetails i : orderDetailsList) {
            repoOrderDetails.deleteById(i.getId());
        }
        return "redirect:/payments";
    }

    @GetMapping("/shipment/{id}/delete")
    public String OrderDelete3(@PathVariable Long id) {
        repoOrders.deleteById(id);
        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrders(id);
        for (OrderDetails i : orderDetailsList) {
            repoOrderDetails.deleteById(i.getId());
        }
        return "redirect:/shipment";
    }

    @GetMapping("/shipped/{id}/delete")
    public String OrderDelete4(@PathVariable Long id) {
        repoOrders.deleteById(id);
        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrders(id);
        for (OrderDetails i : orderDetailsList) {
            repoOrderDetails.deleteById(i.getId());
        }
        return "redirect:/shipped";
    }

    @GetMapping("/open/{id}/delete")
    public String OrderDelete5(@PathVariable Long id) {
        repoOrders.deleteById(id);
        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrders(id);
        for (OrderDetails i : orderDetailsList) {
            repoOrderDetails.deleteById(i.getId());
        }
        return "redirect:/open";
    }

    @GetMapping("/under/{id}/delete")
    public String OrderDelete6(@PathVariable Long id) {
        repoOrders.deleteById(id);
        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrders(id);
        for (OrderDetails i : orderDetailsList) {
            repoOrderDetails.deleteById(i.getId());
        }
        return "redirect:/under";
    }

}
