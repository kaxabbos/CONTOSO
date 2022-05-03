package com.contoso.cont;

import com.contoso.models.OrderDetails;
import com.contoso.models.Orders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OrdersCont extends Global {

    @GetMapping("/orders")
    public String Orders(Model model) {
        addAttributesOrders(model);
        return "orders";
    }

    @PostMapping("/orders/add")
    public String OrderAdd(Model model, @RequestParam String idFioClient, @RequestParam String date) {
        List<Orders> ordersList = repoOrders.findAll();
        for (Orders i : ordersList) {
            if (i.getIdFioClient().equals(idFioClient) && i.getDate().equals(date)) {
                addAttributesOrders(model);
                model.addAttribute("message", "Заказ для этого клиента с этой датой уже существует");
                return "orders";
            }
        }

        Orders orders = new Orders(idFioClient, date);
        repoOrders.save(orders);
        return "redirect:/orders";
    }

    @PostMapping("/orders/{id}/edit")
    public String OrderEdit(Model model, @RequestParam String idFioClient, @RequestParam String date, @PathVariable Long id) {
        List<Orders> ordersList = repoOrders.findAll();
        for (Orders i : ordersList) {
            if (i.getId().equals(id)) continue;
            if (i.getIdFioClient().equals(idFioClient) && i.getDate().equals(date)) {
                addAttributesOrders(model);
                model.addAttribute("message", "Заказ для этого клиента с этой датой уже существует");
                return "orders";
            }
        }

        Orders orders = repoOrders.getById(id);
        orders.setIdFioClient(idFioClient);
        orders.setDate(date);
        repoOrders.save(orders);
        return "redirect:/orders";
    }

}
