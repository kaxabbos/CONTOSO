package com.contoso.cont;

import com.contoso.models.OrderDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OrderDetailsCont extends Global {

    @GetMapping("/orders/{idOrders}/orderDetails")
    public String Order(Model model, @PathVariable Long idOrders) {
        AddAttributesOrderDetails(model, idOrders);
        return "orderDetails";
    }

    @PostMapping("/orders/{idOrders}/orderDetails/addProduct")
    public String OrderAddProduct(Model model, @PathVariable Long idOrders, @RequestParam Long idProduct) {
        if (repoProducts.getById(idProduct).getQuantity() == 0){
            AddAttributesOrderDetails(model, idOrders);
            model.addAttribute("message", "Этого продукта нету в наличии");
            return "orderDetails";
        }

        for (OrderDetails i : repoOrderDetails.findByIdOrders(idOrders)) {
            if (i.getIdProduct().equals(idProduct)) {
                AddAttributesOrderDetails(model, idOrders);
                model.addAttribute("message", "Вы не можете добавить несколько раз один и тот же продукт");
                return "orderDetails";
            }
        }

        OrderDetails orderDetails = new OrderDetails(repoProducts.getById(idProduct), idOrders);
        repoOrderDetails.save(orderDetails);

        FullPriceAndFullQuantity(idOrders);

        return "redirect:/orders/{idOrders}/orderDetails";
    }

    @PostMapping("/orders/{idOrders}/orderDetails/{idOrderDetails}/edit")
    public String OrderEditProduct(Model model, @PathVariable Long idOrders, @PathVariable Long idOrderDetails, @RequestParam Long idProduct, @RequestParam int quantity) {
        OrderDetails orderDetails = repoOrderDetails.getById(idOrderDetails);
        if (!orderDetails.getIdProduct().equals(idProduct)) {
            List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrders(idOrders);

            for (OrderDetails i : orderDetailsList) {
                if (i.getIdProduct().equals(idProduct)) {
                    AddAttributesOrderDetails(model, idOrders);
                    model.addAttribute("message", "Вы не можете добавить несколько раз один и тот же продукт");
                    return "orderDetails";
                }
            }
            orderDetails.set(repoProducts.getById(idProduct));
        }

        if (orderDetails.getQuantityMax() < quantity) orderDetails.setQuantity(orderDetails.getQuantityMax());
        else orderDetails.setQuantity(quantity);

        orderDetails.setPrice(orderDetails.getUnitPrice() * orderDetails.getQuantity());
        
        repoOrderDetails.save(orderDetails);

        FullPriceAndFullQuantity(idOrders);

        return "redirect:/orders/{idOrders}/orderDetails";
    }

    @GetMapping("/orders/{idOrders}/orderDetails/{idOrderDetails}/delete")
    public String OrderDeleteProduct(@PathVariable Long idOrders, @PathVariable Long idOrderDetails) {
        repoOrderDetails.deleteById(idOrderDetails);
        return "redirect:/orders/{idOrders}/orderDetails";
    }
}