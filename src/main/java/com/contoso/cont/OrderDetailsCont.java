package com.contoso.cont;

import com.contoso.cont.general.Attributes;
import com.contoso.models.OrderDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OrderDetailsCont extends Attributes {

    @GetMapping("/orders/{idOrder}/orderDetails")
    public String Order(Model model, @PathVariable Long idOrder) {
        AddAttributesOrderDetails(model, idOrder);
        return "orderDetails";
    }

    @PostMapping("/orders/{idOrder}/orderDetails/addProduct")
    public String OrderAddProduct(Model model, @PathVariable Long idOrder, @RequestParam Long idProduct) {
        if (repoProducts.getById(idProduct).getQuantity() == 0) {
            AddAttributesOrderDetails(model, idOrder);
            model.addAttribute("message", "Этого продукта нету в наличии");
            return "orderDetails";
        }

        for (OrderDetails i : repoOrderDetails.findByIdOrder(idOrder)) {
            if (i.getIdProduct().equals(idProduct)) {
                AddAttributesOrderDetails(model, idOrder);
                model.addAttribute("message", "Вы не можете добавить несколько раз один и тот же продукт");
                return "orderDetails";
            }
        }

        OrderDetails orderDetails = new OrderDetails(repoProducts.getById(idProduct), idOrder);
        repoOrderDetails.save(orderDetails);
        setFullPriceAndFullQuantity(idOrder);
        return "redirect:/orders/{idOrder}/orderDetails";
    }

    @PostMapping("/orders/{idOrder}/orderDetails/{idOrderDetail}/edit")
    public String OrderEditProduct(Model model, @PathVariable Long idOrder, @PathVariable Long idOrderDetail, @RequestParam Long idProduct, @RequestParam int quantity) {
        OrderDetails orderDetails = repoOrderDetails.getById(idOrderDetail);
        if (!orderDetails.getIdProduct().equals(idProduct)) {
            List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrder(idOrder);

            for (OrderDetails i : orderDetailsList) {
                if (i.getIdProduct().equals(idProduct)) {
                    AddAttributesOrderDetails(model, idOrder);
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

        setFullPriceAndFullQuantity(idOrder);

        return "redirect:/orders/{idOrder}/orderDetails";
    }

    @GetMapping("/orders/{idOrder}/orderDetails/{idOrderDetail}/delete")
    public String OrderDeleteProduct(@PathVariable Long idOrder, @PathVariable Long idOrderDetail) {
        repoOrderDetails.deleteById(idOrderDetail);
        return "redirect:/orders/{idOrder}/orderDetails";
    }
}