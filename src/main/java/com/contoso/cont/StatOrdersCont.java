package com.contoso.cont;

import com.contoso.cont.general.AddAttributes;
import com.contoso.cont.general.General;
import com.contoso.models.enums.OrderStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StatOrdersCont extends AddAttributes {

    @GetMapping("/statOrders")
    public String statOrders(Model model) {
        AddAttributesStatOrders(model, OrderStatus.Все);
        return "statOrders";
    }

    @PostMapping("/statOrders/status")
    public String statOrdersStatus(Model model, @RequestParam OrderStatus orderStatus) {
        AddAttributesStatOrders(model, orderStatus);
        return "statOrders";
    }


}
