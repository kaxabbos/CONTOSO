package com.contoso.cont;

import com.contoso.cont.general.Attributes;
import com.contoso.models.OrderDetails;
import com.contoso.models.Products;
import com.contoso.models.ProductsCSV;
import com.contoso.models.StatProducts;
import com.contoso.models.enums.ProductNameModel;
import com.contoso.models.enums.ProductStatus;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
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
    public String ProductAdd(@RequestParam ProductNameModel nameModel, @RequestParam int quantity, @RequestParam int unitPrice) {
        Products product = repoProducts.saveAndFlush(new Products(nameModel, quantity, unitPrice));
        String dateNow = LocalDateTime.now().toString();
        StatProducts statProducts = new StatProducts(quantity, dateNow.substring(0, 10), product.getId(), null, ProductStatus.Произведено);
        repoStatProducts.save(statProducts);
        return "redirect:/products";
    }

    @PostMapping("/product/csv")
    public String ProductCSV(Model model, @RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            AddAttributesProducts(model);
            model.addAttribute("message", "Пустой файл!!!");
            return "products";
        }

        List<ProductsCSV> productsList;

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("windows-1251")))) {
            CsvToBean productsCsvToBean = new CsvToBeanBuilder(reader)
                    .withType(ProductsCSV.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            productsList = productsCsvToBean.parse();
            boolean flag;
            for (ProductsCSV i : productsList) {
                flag = true;
                for (ProductNameModel j : ProductNameModel.values()) {
                    if (i.getNameModel().equals(j.name())) {
                        flag = false;
                        break;
                    }
                }
                if (i.getQuantity() <= 0 || i.getUnitPrice() <= 0) flag = true;
                if (flag) {
                    AddAttributesProducts(model);
                    model.addAttribute("message", "Некорректный файл!!!");
                    return "products";
                }
            }
        } catch (Exception e) {
            AddAttributesProducts(model);
            model.addAttribute("message", "Некорректный файл!!!");
            return "products";
        }

        Products temp;
        String dateNow = LocalDateTime.now().toString();
        for (ProductsCSV i : productsList) {
            for (ProductNameModel j : ProductNameModel.values()) {
                if (i.getNameModel().equals(j.name())) {
                    temp = repoProducts.saveAndFlush(new Products(j, i.getQuantity(), i.getUnitPrice()));
                    repoStatProducts.save(new StatProducts(temp.getQuantity(), dateNow.substring(0, 10), temp.getId(), null, ProductStatus.Произведено));
                    break;
                }
            }
        }

        return "redirect:/products";
    }

    @PostMapping("/product/{idProduct}/edit")
    public String ProductEdit(@PathVariable Long idProduct, @RequestParam ProductNameModel nameModel, @RequestParam int quantity, @RequestParam int unitPrice) {
        String dateNow = LocalDateTime.now().toString();
        Products product = repoProducts.getById(idProduct);

        if (quantity > product.getQuantity()) {
            StatProducts statProducts = new StatProducts(quantity - product.getQuantity(), dateNow.substring(0, 10), idProduct, null, ProductStatus.Произведено);
            repoStatProducts.save(statProducts);
        } else if (quantity < product.getQuantity()) {
            StatProducts statProducts = new StatProducts(product.getQuantity() - quantity, dateNow.substring(0, 10), idProduct, null, ProductStatus.Отгружено);
            repoStatProducts.save(statProducts);
        }

        product.setNameModel(nameModel);
        product.setQuantity(quantity);
        product.setUnitPrice(unitPrice);

        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdProduct(idProduct);
        for (OrderDetails i : orderDetailsList) {
            i.setQuantityMax(product.getQuantity());
            repoOrderDetails.save(i);
        }

        repoProducts.save(product);
        return "redirect:/products";
    }

    @GetMapping("/product/{idProduct}/delete")
    public String ProductDelete(Model model, @PathVariable Long idProduct) {
        List<OrderDetails> orderDetails = repoOrderDetails.findByIdProduct(idProduct);
        if (orderDetails.size() != 0) {
            Products products = repoProducts.getById(idProduct);
            AddAttributesProducts(model);
            model.addAttribute("message", "Продукт \"" + products.getId() + " - " + products.getNameModel() + "\" используется");
            return "products";
        }
        if (repoProducts.getById(idProduct).getQuantity() != 0){
            AddAttributesProducts(model);
            Products products = repoProducts.getById(idProduct);
            model.addAttribute("message", "Продукт \"" + products.getId() + " - " + products.getNameModel() + "\" еще не отгружен");
            return "products";
        }
        repoProducts.deleteById(idProduct);
        return "redirect:/products";
    }
}
