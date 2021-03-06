package com.contoso.cont;

import com.contoso.cont.general.Attributes;
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
public class StatusesCont extends Attributes {

    @GetMapping("/orders/{idOrder}/reserved")
    public String OrderArrange(Model model, @PathVariable Long idOrder) {
        Orders order = repoOrders.getById(idOrder);

        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrder(idOrder);
        Products product;

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

        for (OrderDetails i : orderDetailsList) {
            product = repoProducts.getById(i.getIdProduct());
            product.setQuantity(product.getQuantity() - i.getQuantity());
            StatProducts statProducts = new StatProducts(i.getQuantity(), DateNow(), product.getId(), i.getId(), ProductStatus.Зарезервировано);
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

    @GetMapping("/waiting/{idOrder}/not_reserved")
    public String OrderNotArrange1(@PathVariable Long idOrder) {
        Orders order = repoOrders.getById(idOrder);
        order.setStatus(OrderStatus.Не_зарезервировано);
        repoOrders.save(order);
        return "redirect:/waiting";
    }

    @GetMapping("/orders/{idOrder}/shipment")
    public String OrderShipment(Model model, @PathVariable Long idOrder) {
        Orders order = repoOrders.getById(idOrder);

        if (order.getPaymentType() == PaymentType.Выберите) {
            AddAttributesPayments(model);
            model.addAttribute("message", "Выберите тип оплаты");
            return "payments";
        }

        order.setStatus(OrderStatus.В_отгрузке);
        repoOrders.save(order);
        return "redirect:/payments";
    }

    @GetMapping("/orders/{idOrder}/shipped")
    public String OrderShipped(@PathVariable Long idOrder) {
        Orders order = repoOrders.getById(idOrder);

        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrder(idOrder);

        StatProducts statProducts;
        for (OrderDetails i : orderDetailsList) {
            statProducts = repoStatProducts.findByIdOrderDetails(i.getId());
            statProducts.setProductStatus(ProductStatus.Отгружено);
            statProducts.setDate(DateNow());
            repoStatProducts.save(statProducts);
        }

        order.setStatus(OrderStatus.Отгружено);
        repoOrders.save(order);
        return "redirect:/shipment";
    }

    @GetMapping("/orders/{idOrder}/waiting")
    public String OrderWaiting(@PathVariable Long idOrder) {
        Orders orders = repoOrders.getById(idOrder);
        orders.setStatus(OrderStatus.Ожидание);
        repoOrders.save(orders);
        return "redirect:/orders";
    }
}
