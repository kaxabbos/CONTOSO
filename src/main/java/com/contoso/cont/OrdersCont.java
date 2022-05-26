package com.contoso.cont;

import com.contoso.cont.general.AddAttribute;
import com.contoso.models.Orders;
import com.contoso.models.enums.OrderStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OrdersCont extends AddAttribute {

    @GetMapping("/orders")
    public String Orders(Model model) {
        AddAttributesOrders(model);
        return "orders";
    }

    @PostMapping("/orders/add")
    public String OrderAdd(Model model, @RequestParam String idFioClient, @RequestParam String date) {
        List<Orders> ordersList = repoOrders.findAll();
        for (Orders i : ordersList) {
            if (i.getIdFioClient().equals(idFioClient) && i.getDate().equals(date) && i.getStatus() == OrderStatus.Не_зарезервировано) {
                AddAttributesOrders(model);
                model.addAttribute("message", "Заказ для клиента \"" + idFioClient + "\" с датой \"" + date + "\" в оформлении");
                return "orders";
            }
        }
        Orders orders = new Orders(idFioClient, date);
        repoOrders.save(orders);
        return "redirect:/orders";
    }

    @PostMapping("/orders/{idOrder}/edit")
    public String OrderEdit(Model model, @RequestParam String idFioClient, @RequestParam String date, @PathVariable Long idOrder) {
        List<Orders> ordersList = repoOrders.findAll();
        for (Orders i : ordersList) {
            if (i.getId().equals(idOrder)) continue;
            if (i.getIdFioClient().equals(idFioClient) && i.getDate().equals(date) && i.getStatus() == OrderStatus.Не_зарезервировано) {
                AddAttributesOrders(model);
                model.addAttribute("message", "Заказ для клиента \"" + idFioClient + "\" с датой \"" + date + "\" в оформлении");
                return "orders";
            }
        }
        Orders orders = repoOrders.getById(idOrder);
        orders.setIdFioClient(idFioClient);
        orders.setDate(date);
        repoOrders.save(orders);
        return "redirect:/orders";
    }
}
