package com.contoso.cont;

import com.contoso.cont.general.Attributes;
import com.contoso.models.Orders;
import com.contoso.models.enums.PaymentType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentsCont extends Attributes {

    @GetMapping("/payments")
    public String Payments(Model model) {
        AddAttributesPayments(model);
        return "payments";
    }

    @PostMapping("/payments/{id}/edit")
    public String PaymentEdit(@PathVariable Long id, @RequestParam PaymentType paymentType) {
        Orders orders = repoOrders.getById(id);
        orders.setPaymentType(paymentType);
        repoOrders.save(orders);
        return "redirect:/payments";
    }


}
