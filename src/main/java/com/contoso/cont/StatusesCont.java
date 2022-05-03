package com.contoso.cont;

import com.contoso.models.OrderDetails;
import com.contoso.models.Orders;
import com.contoso.models.Products;
import com.contoso.models.StatProducts;
import com.contoso.models.enums.OrderStatus;
import com.contoso.models.enums.PaymentType;
import com.contoso.models.enums.ProductStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class StatusesCont extends Global {

    @GetMapping("/orders/{id}/reserved")
    public String OrderArrange(Model model, @PathVariable Long id) {
        Orders order = repoOrders.getById(id);

        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrders(id);
        Products product;

        System.out.println(orderDetailsList.size());
        if (orderDetailsList.size() == 0) {
            AddAttributesOrders(model);
            model.addAttribute("message", "Не выбраны детали заказа: " + order.getId() + " - " + order.getIdFioClient());
            return "orders";
        }

        for (OrderDetails i : orderDetailsList) {
            product = repoProducts.getById(i.getIdProduct());
            if (i.getQuantity() > product.getQuantity()) {
                AddAttributesOrders(model);
                model.addAttribute("message", "Недостаточно продуктов для заказа: " + order.getId() + " - " + order.getIdFioClient());
                return "orders";
            }
            if (i.getQuantity() == 0) {
                AddAttributesOrders(model);
                model.addAttribute("message", "Неккоректный выбор деталей заказа: " + order.getId() + " - " + order.getIdFioClient());
                return "orders";
            }
        }

        String dateNow = LocalDateTime.now().toString();
        for (OrderDetails i : orderDetailsList) {
            product = repoProducts.getById(i.getIdProduct());
            product.setQuantity(product.getQuantity() - i.getQuantity());
            StatProducts statProducts = new StatProducts(i.getQuantity(), dateNow.substring(0, 10), product.getId(), i.getId(), ProductStatus.Зарезервировано);
            repoProducts.save(product);
            repoStatProducts.save(statProducts);
            List<OrderDetails> orderDetailsList1 = repoOrderDetails.findByIdProduct(product.getId());
            for (OrderDetails j : orderDetailsList1) {
                j.setQuantityMax(product.getQuantity());
                repoOrderDetails.save(j);
            }
        }

        order.setStatus(OrderStatus.Зарезервировано);
        repoOrders.save(order);
        return "redirect:/orders";
    }

    @GetMapping("/open/{id}/not_reserved")
    public String OrderNotArrange1(@PathVariable Long id) {
        Orders order = repoOrders.getById(id);
        order.setStatus(OrderStatus.Не_зарезервировано);
        repoOrders.save(order);
        return "redirect:/open";
    }

    @GetMapping("/under/{id}/not_reserved")
    public String OrderNotArrange2(@PathVariable Long id) {
        Orders order = repoOrders.getById(id);
        order.setStatus(OrderStatus.Не_зарезервировано);
        repoOrders.save(order);
        return "redirect:/under";
    }

    @GetMapping("/orders/{id}/shipment")
    public String OrderShipment(Model model, @PathVariable Long id) {
        Orders order = repoOrders.getById(id);

        if (order.getPaymentType() == PaymentType.Выберите) {
            AddAttributesPayments(model);
            model.addAttribute("message", "Выберите тип оплаты");
            return "payments";
        }

        order.setStatus(OrderStatus.В_отгрузке);
        repoOrders.save(order);
        return "redirect:/payments";
    }

    @GetMapping("/orders/{id}/shipped")
    public String OrderShipped(Model model, @PathVariable Long id) {
        Orders order = repoOrders.getById(id);

        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrders(id);

        String dateNow = LocalDateTime.now().toString();
        StatProducts statProducts;
        for (OrderDetails i : orderDetailsList) {
            statProducts = repoStatProducts.findByIdOrderDetails(i.getId());
            statProducts.setProductStatus(ProductStatus.Отгружено);
            statProducts.setDate(dateNow.substring(0, 10));
            repoStatProducts.save(statProducts);
        }

        order.setStatus(OrderStatus.Отгружено);
        repoOrders.save(order);
        return "redirect:/shipment";
    }

    @GetMapping("/orders/{id}/open")
    public String OrderOpen(@PathVariable Long id) {
        Orders orders = repoOrders.getById(id);
        orders.setStatus(OrderStatus.Открыто);
        repoOrders.save(orders);
        return "redirect:/shipment";
    }

    @GetMapping("/orders/{id}/under")
    public String OrderUnder(@PathVariable Long id) {
        Orders orders = repoOrders.getById(id);
        orders.setStatus(OrderStatus.Под_заказ);
        repoOrders.save(orders);
        return "redirect:/shipment";
    }
}
