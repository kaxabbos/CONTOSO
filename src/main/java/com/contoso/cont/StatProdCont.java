package com.contoso.cont;

import com.contoso.cont.general.AddAttributes;
import com.contoso.cont.general.General;
import com.contoso.models.enums.ProductStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StatProdCont extends AddAttributes {

    @GetMapping("/statProd")
    public String statProd(Model model) {
        AddAttributesStatProd(model, ProductStatus.Все, null);
        return "statProd";
    }

    @PostMapping("/statProd")
    public String statProdSearch(Model model, @RequestParam ProductStatus productStatus, @RequestParam String date) {
        AddAttributesStatProd(model, productStatus, date);
        return "statProd";
    }
}
