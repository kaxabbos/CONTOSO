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

import java.util.ArrayList;
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

    protected void OrderDelete(Long idOrder) {
        repoOrders.deleteById(idOrder);
        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrders(idOrder);
        for (OrderDetails i : orderDetailsList) {
            repoOrderDetails.deleteById(i.getId());
        }
    }

    protected void OrderAndStatProductDelete(Long idOrder) {
        repoOrders.deleteById(idOrder);
        List<OrderDetails> orderDetailsList = repoOrderDetails.findByIdOrders(idOrder);
        Products product;
        for (OrderDetails i : orderDetailsList) {
            repoOrderDetails.deleteById(i.getId());
            repoStatProducts.delete(repoStatProducts.findByIdOrderDetails(i.getId()));
            product = repoProducts.getById(i.getIdProduct());
            product.setQuantity(product.getQuantity() + i.getQuantity());
            List<OrderDetails> orderDetailsList1 = repoOrderDetails.findByIdProduct(product.getId());
            for (OrderDetails j : orderDetailsList1) {
                j.setQuantityMax(product.getQuantity());
                repoOrderDetails.save(j);
            }
        }
    }

    protected void AddAttributes(Model model) {
        model.addAttribute("role", getUserRole());
        model.addAttribute("avatar", getAvatar());
        model.addAttribute("fio", getFIO());
    }

    protected void AddAttributesDetails(Model model, Long idOrders) {
        AddAttributes(model);
        model.addAttribute("details", repoOrderDetails.findByIdOrders(idOrders));
        model.addAttribute("order", repoOrders.getById(idOrders));
    }

    protected void AddAttributesIndex(Model model) {
        AddAttributes(model);
        model.addAttribute("user", getUser());
    }

    protected void AddAttributesOrderDetails(Model model, Long idOrders) {
        AddAttributes(model);
        model.addAttribute("orderDetails", repoOrderDetails.findByIdOrders(idOrders));
        model.addAttribute("order", repoOrders.getById(idOrders));
        List<Products> temp = repoProducts.findAll();
        List<Products> products = new ArrayList<>();
        for (Products i : temp) if (i.getQuantity() != 0) products.add(i);
        model.addAttribute("products", products);
    }

    protected void AddAttributesOrders(Model model) {
        AddAttributes(model);
        List<Orders> ordersList = repoOrders.findByOrderStatus(OrderStatus.Не_зарезервировано);
        PriceQuantity(model, ordersList);
        model.addAttribute("orders", ordersList);
        model.addAttribute("clients", repoClients.findAll());
    }

    protected void AddAttributesPayments(Model model) {
        AddAttributes(model);
        List<Orders> ordersList = repoOrders.findByOrderStatus(OrderStatus.Зарезервировано);
        PriceQuantity(model, ordersList);
        model.addAttribute("payments", ordersList);
        model.addAttribute("paymentTypes", PaymentType.values());
    }

    protected void AddAttributesShipment(Model model) {
        AddAttributes(model);
        List<Orders> ordersList = repoOrders.findByOrderStatus(OrderStatus.В_отгрузке);
        PriceQuantity(model, ordersList);
        model.addAttribute("shipments", ordersList);
    }

    protected void AddAttributesShipped(Model model) {
        AddAttributes(model);
        List<Orders> ordersList = repoOrders.findByOrderStatus(OrderStatus.Отгружено);
        PriceQuantity(model, ordersList);
        model.addAttribute("shippeds", ordersList);
    }

    protected void AddAttributesOpen(Model model) {
        AddAttributes(model);
        List<Orders> ordersList = repoOrders.findByOrderStatus(OrderStatus.Открыто);
        PriceQuantity(model, ordersList);
        model.addAttribute("opens", ordersList);
    }

    protected void AddAttributesUnder(Model model) {
        AddAttributes(model);
        List<Orders> ordersList = repoOrders.findByOrderStatus(OrderStatus.Под_заказ);
        PriceQuantity(model, ordersList);
        model.addAttribute("unders", ordersList);
    }

    protected void AddAttributesStatProd(Model model, ProductStatus productStatus, String date) {
        AddAttributes(model);

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

    protected void AddAttributesStatOrders(Model model, OrderStatus orderStatus) {
        AddAttributes(model);

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

    protected void AddAttributesProducts(Model model) {
        AddAttributes(model);
        List<Products> products = repoProducts.findAll();
        Collections.reverse(products);
        model.addAttribute("products", products);
        model.addAttribute("productNameModel", ProductNameModel.values());
    }

    protected void AddAttributesClients(Model model) {
        AddAttributes(model);
        List<Clients> clients = repoClients.findAll();
        Collections.reverse(clients);
        model.addAttribute("clients", clients);
    }

    protected void AddAttributesProfiles(Model model) {
        AddAttributes(model);
        model.addAttribute("roles", Roles.values());
        model.addAttribute("users", getUsersList());
    }

    protected void AddAttributesAddUser(Model model) {
        AddAttributes(model);
        model.addAttribute("roles", Roles.values());
    }

    protected void PriceQuantity(Model model, List<Orders> ordersList) {
        int price = 0, quantity = 0;
        for (Orders i : ordersList) {
            price += i.getFullPrice();
            quantity += i.getFullQuantity();
        }
        model.addAttribute("price", price);
        model.addAttribute("quantity", quantity);
    }

    protected void FullPriceAndFullQuantity(Long idOrders) {
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