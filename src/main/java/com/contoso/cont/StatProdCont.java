package com.contoso.cont;

import com.contoso.models.enums.ProductStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StatProdCont extends Global {

    @GetMapping("/statProd")
    public String statProd(Model model) {
        addAttributesStatProd(model, ProductStatus.Все, null);
        return "statProd";
    }

    @PostMapping("/statProd")
    public String statProdSearch(Model model, @RequestParam ProductStatus productStatus, @RequestParam String date) {
        addAttributesStatProd(model, productStatus, date);
        System.out.println(date);
        return "statProd";
    }

}
