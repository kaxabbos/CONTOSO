package com.contoso.cont;

import com.contoso.models.*;
import com.contoso.models.enums.*;
import com.contoso.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

public class Global {
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

    protected void addAttributes(Model model) {
        model.addAttribute("role", getUserRole());
        model.addAttribute("avatar", getAvatar());
        model.addAttribute("fio", getFIO());
    }

    protected void addAttributesDetails(Model model, Long idOrders) {
        addAttributes(model);
        model.addAttribute("details", repoOrderDetails.findByIdOrders(idOrders));
        model.addAttribute("order", repoOrders.getById(idOrders));
    }

    protected void addAttributesIndex(Model model) {
        addAttributes(model);
        model.addAttribute("user", getUser());
    }

    protected void addAttributesOrderDetails(Model model, Long idOrders) {
        addAttributes(model);
        model.addAttribute("orderDetails", repoOrderDetails.findByIdOrders(idOrders));
        model.addAttribute("order", repoOrders.getById(idOrders));
        model.addAttribute("products", repoProducts.findAll());
    }

    protected void addAttributesOrders(Model model) {
        addAttributes(model);
        List<Orders> ordersList = repoOrders.findByOrderStatus(OrderStatus.Не_зарезервировано);
        priceQuantity(model, ordersList);
        model.addAttribute("orders", ordersList);
        model.addAttribute("clients", repoClients.findAll());
    }

    protected void addAttributesPayments(Model model) {
        addAttributes(model);
        List<Orders> ordersList = repoOrders.findByOrderStatus(OrderStatus.Зарезервировано);
        priceQuantity(model, ordersList);
        model.addAttribute("payments", ordersList);
        model.addAttribute("paymentTypes", PaymentType.values());
    }

    protected void addAttributesShipment(Model model) {
        addAttributes(model);
        List<Orders> ordersList = repoOrders.findByOrderStatus(OrderStatus.В_отгрузке);
        priceQuantity(model, ordersList);
        model.addAttribute("shipments", ordersList);
    }

    protected void addAttributesShipped(Model model) {
        addAttributes(model);
        List<Orders> ordersList = repoOrders.findByOrderStatus(OrderStatus.Отгружено);
        priceQuantity(model, ordersList);
        model.addAttribute("shippeds", ordersList);
    }

    protected void addAttributesOpen(Model model) {
        addAttributes(model);
        List<Orders> ordersList = repoOrders.findByOrderStatus(OrderStatus.Открыто);
        priceQuantity(model, ordersList);
        model.addAttribute("opens", ordersList);
    }

    protected void addAttributesUnder(Model model) {
        addAttributes(model);
        List<Orders> ordersList = repoOrders.findByOrderStatus(OrderStatus.Под_заказ);
        priceQuantity(model, ordersList);
        model.addAttribute("unders", ordersList);
    }

    protected void addAttributesStatProd(Model model, ProductStatus productStatus, String date) {
        addAttributes(model);

        List<StatProducts> statProducts;

        if (productStatus == ProductStatus.Все && (date == null || date.equals(""))) {
            statProducts = repoStatProducts.findAll();
        } else if (productStatus == ProductStatus.Все) {
            statProducts = repoStatProducts.findByDate(date);
        } else if (date == null || date.equals("")) {
            statProducts = repoStatProducts.findByProductStatus(productStatus);
        } else {
            statProducts = repoStatProducts.findByProductStatusAndDate(productStatus, date);
        }

        Collections.reverse(statProducts);

        int max = 0;

        for (StatProducts i : statProducts) {
            if (i.getProductStatus() == ProductStatus.Произведено) max += i.getQuantity();
            if (i.getProductStatus() == ProductStatus.Отгружено || i.getProductStatus() == ProductStatus.Зарезервировано)
                max -= i.getQuantity();
        }

        model.addAttribute("statProducts", statProducts);
        model.addAttribute("max", max);
        model.addAttribute("ProductStatus", ProductStatus.values());
    }

    protected void addAttributesStatOrders(Model model, OrderStatus orderStatus) {
        addAttributes(model);

        List<Orders> ordersList;

        if (orderStatus == OrderStatus.Все) {
            ordersList = repoOrders.findAll();
        } else {
            ordersList = repoOrders.findByOrderStatus(orderStatus);
        }

        model.addAttribute("orders", ordersList);
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("productStats", ProductStatus.values());
    }

    protected void addAttributesProducts(Model model) {
        addAttributes(model);
        List<Products> products = repoProducts.findAll();
        Collections.reverse(products);
        model.addAttribute("products", products);
        model.addAttribute("productNameModel", ProductNameModel.values());
    }

    protected void addAttributesClients(Model model) {
        addAttributes(model);
        List<Clients> clients = repoClients.findAll();
        Collections.reverse(clients);
        model.addAttribute("clients", clients);
    }

    protected void addAttributesProfiles(Model model) {
        addAttributes(model);
        model.addAttribute("roles", Roles.values());
        model.addAttribute("users", getUsersList());
    }

    protected void addAttributesAddUser(Model model) {
        addAttributes(model);
        model.addAttribute("roles", Roles.values());
    }

    protected void priceQuantity(Model model, List<Orders> ordersList) {
        int price = 0, quantity = 0;
        for (Orders i : ordersList) {
            price += i.getFullPrice();
            quantity += i.getFullQuantity();
        }
        model.addAttribute("price", price);
        model.addAttribute("quantity", quantity);
    }

    protected void fullPriceAndFullQuantity(Long idOrders) {
        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrders(idOrders);
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

    protected List<Users> getUsersList() {
        List<Users> temp = repoUsers.findByRole(Roles.Руководитель);
        temp.addAll(repoUsers.findByRole(Roles.Менеджер));
        temp.addAll(repoUsers.findByRole(Roles.Админ));
        temp.addAll(repoUsers.findByRole(Roles.Сотрудник));
        return temp;
    }

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
        return "Добро пожаловать";
    }
}