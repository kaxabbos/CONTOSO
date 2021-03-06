package com.contoso.cont.general;

import com.contoso.models.OrderDetails;
import com.contoso.models.Orders;
import com.contoso.models.Products;
import com.contoso.models.Users;
import com.contoso.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

public class General {

    @Autowired
    protected RepoUsers repoUsers;
    @Autowired
    protected RepoProducts repoProducts;
    @Autowired
    protected RepoStatProducts repoStatProducts;
    @Autowired
    protected RepoClients repoClients;
    @Autowired
    protected RepoOrders repoOrders;
    @Autowired
    protected RepoOrderDetails repoOrderDetails;

    @Value("${upload.img}")
    protected String uploadImg;

    protected String defAvatar = "def.jpeg";

    protected Users getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            return repoUsers.findByUsername(userDetail.getUsername());
        }
        return null;
    }

    protected String getUserRole() {
        Users user = getUser();
        if (user != null) return String.valueOf(user.getRole());
        return "NOT";
    }

    protected Long getUserID() {
        Users user = getUser();
        if (user != null) return user.getId();
        return 0L;
    }

    protected String getAvatar() {
        Users user = getUser();
        if (user != null) return user.getAvatar();
        return defAvatar;
    }

    protected String getFIO() {
        Users user = getUser();
        if (user != null) return user.getFio();
        return "?????????? ????????????????????";
    }

    protected void DeleteOrderAndOrderDetails(Long idOrder) {
        repoOrders.deleteById(idOrder);
        repoOrderDetails.deleteAll(repoOrderDetails.findByIdOrder(idOrder));
    }

    protected void DeleteOrderAndOrderDetailsAndStatProduct(Long idOrder) {
        repoOrders.deleteById(idOrder);
        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrder(idOrder);
        Products product;
        for (OrderDetails i : orderDetailsList) {
            repoStatProducts.delete(repoStatProducts.findByIdOrderDetails(i.getId()));
            product = repoProducts.getById(i.getIdProduct());
            product.setQuantity(product.getQuantity() + i.getQuantity());
            List<OrderDetails> orderDetailsList1 = repoOrderDetails.findByIdProduct(product.getId());
            for (OrderDetails j : orderDetailsList1) {
                j.setQuantityMax(product.getQuantity());
                repoOrderDetails.save(j);
            }
        }
        repoOrderDetails.deleteAll(orderDetailsList);
    }

    protected void getPriceAndQuantity(Model model, List<Orders> ordersList) {
        int price = 0, quantity = 0;
        for (Orders i : ordersList) {
            price += i.getFullPrice();
            quantity += i.getFullQuantity();
        }
        model.addAttribute("price", price);
        model.addAttribute("quantity", quantity);
    }

    protected void setFullPriceAndFullQuantity(Long idOrders) {
        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrder(idOrders);
        int fullPrice = 0, fullQuantity = 0;

        for (OrderDetails i : orderDetailsList) {
            fullPrice += i.getPrice();
            fullQuantity += i.getQuantity();
        }

        Orders orders = repoOrders.getById(idOrders);
        orders.setFullPrice(fullPrice);
        orders.setFullQuantity(fullQuantity);
        repoOrders.save(orders);
    }

    protected String DateNow() {
        return LocalDateTime.now().toString().substring(0, 10);
    }

}