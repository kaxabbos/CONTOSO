package com.contoso.cont;

import com.contoso.cont.general.Attributes;
import com.contoso.models.OrderDetails;
import com.contoso.models.Orders;
import com.contoso.models.Products;
import com.contoso.models.StatProducts;
import com.contoso.models.enums.OrderStatus;
import com.contoso.models.enums.ProductStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ProductsCont extends Attributes {

    @GetMapping("/products")
    public String Products(Model model) {
        AddAttributesProducts(model);
        return "products";
    }

    @PostMapping("/product/add")
    public String ProductAdd(Model model, @RequestParam String name, @RequestParam int quantity, @RequestParam int unitPrice) {
        Products product = repoProducts.findByName(name);

        System.out.println(product);

        if (product == null) {
            product = repoProducts.saveAndFlush(new Products(name, quantity, unitPrice));
        } else {
            AddAttributesProducts(model);
            model.addAttribute("message", "Продукт с наименованием \"" + name + "\" уже существует");
            return "products";
        }

        StatProducts statProducts = new StatProducts(quantity, DateNow(), product.getId(), null, ProductStatus.Произведено);
        repoStatProducts.save(statProducts);
        return "redirect:/products";
    }

    @PostMapping("/product/{idProduct}/edit")
    public String ProductEdit(@PathVariable Long idProduct, @RequestParam String name, @RequestParam int quantity, @RequestParam int unitPrice) {
        boolean editName = false;

        Products product = repoProducts.getById(idProduct);

        if (quantity > product.getQuantity()) {
            StatProducts statProducts = new StatProducts(quantity - product.getQuantity(), DateNow(), idProduct, null, ProductStatus.Произведено);
            repoStatProducts.save(statProducts);
        } else if (quantity < product.getQuantity()) {
            StatProducts statProducts = new StatProducts(product.getQuantity() - quantity, DateNow(), idProduct, null, ProductStatus.Отгружено);
            repoStatProducts.save(statProducts);
        }

        product.setQuantity(quantity);
        product.setUnitPrice(unitPrice);

        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdProduct(idProduct);
        Orders order;
        if (!product.getName().equals(name)) editName = true;
        for (OrderDetails i : orderDetailsList) {
            if (editName) i.setNameProduct(name);
            i.setQuantityMax(product.getQuantity());
            order = repoOrders.getById(i.getIdOrder());
            if (order.getStatus() == OrderStatus.Не_зарезервировано || order.getStatus() == OrderStatus.Ожидание) {
                i.setUnitPrice(product.getUnitPrice());
                i.setPrice(i.getUnitPrice() * i.getQuantity());
                setFullPriceAndFullQuantity(order.getId());
            }
            repoOrderDetails.save(i);
        }

        product.setName(name);

        repoProducts.save(product);
        return "redirect:/products";
    }

    @GetMapping("/product/{idProduct}/delete")
    public String ProductDelete(Model model, @PathVariable Long idProduct) {
        List<OrderDetails> orderDetails = repoOrderDetails.findByIdProduct(idProduct);
        if (orderDetails.size() != 0) {
            Products products = repoProducts.getById(idProduct);
            AddAttributesProducts(model);
            model.addAttribute("message", "Продукт \"" + products.getId() + " - " + products.getName() + "\" используется");
            return "products";
        }
        if (repoProducts.getById(idProduct).getQuantity() != 0) {
            AddAttributesProducts(model);
            Products products = repoProducts.getById(idProduct);
            model.addAttribute("message", "Продукт \"" + products.getId() + " - " + products.getName() + "\" еще не отгружен");
            return "products";
        }
        repoProducts.deleteById(idProduct);
        return "redirect:/products";
    }
}
