package com.contoso.cont.general;

import com.contoso.models.Orders;
import com.contoso.models.Products;
import com.contoso.models.StatProducts;
import com.contoso.models.enums.*;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

public class AddAttributes extends General {

    protected void AddAttributes(Model model) {
        model.addAttribute("role", getUserRole());
        model.addAttribute("avatar", getAvatar());
        model.addAttribute("fio", getFIO());
    }

    protected void AddAttributesDetails(Model model, Long idOrder) {
        AddAttributes(model);
        model.addAttribute("details", repoOrderDetails.findByIdOrders(idOrder));
        model.addAttribute("order", repoOrders.getById(idOrder));
    }

    protected void AddAttributesIndex(Model model) {
        AddAttributes(model);
        model.addAttribute("user", getUser());
    }

    protected void AddAttributesOrderDetails(Model model, Long idOrder) {
        AddAttributes(model);
        model.addAttribute("orderDetails", repoOrderDetails.findByIdOrders(idOrder));
        model.addAttribute("order", repoOrders.getById(idOrder));
        List<Products> temp = repoProducts.findAllByOrderByNameModel();
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
            statProducts = repoStatProducts.findAllByOrderByIdDesc();
        } else if (productStatus == ProductStatus.Все) {
            statProducts = repoStatProducts.findByDateOrderByIdDesc(date);
        } else if (date == null || date.equals("")) {
            statProducts = repoStatProducts.findByProductStatusOrderByIdDesc(productStatus);
        } else {
            statProducts = repoStatProducts.findByProductStatusAndDateOrderByIdDesc(productStatus, date);
        }

        int max = 0;
        for (StatProducts i : statProducts) {
            if (i.getProductStatus() == ProductStatus.Произведено) max += i.getQuantity();
            if (i.getProductStatus() == ProductStatus.Отгружено || i.getProductStatus() == ProductStatus.Зарезервировано)
                max -= i.getQuantity();
        }

        model.addAttribute("selectedStatus", productStatus);
        model.addAttribute("selectedDate", date);
        model.addAttribute("statProducts", statProducts);
        model.addAttribute("max", max);
        model.addAttribute("ProductStatus", ProductStatus.values());
    }

    protected void AddAttributesStatOrders(Model model, OrderStatus orderStatus) {
        AddAttributes(model);

        List<Orders> ordersList;

        if (orderStatus == OrderStatus.Все) {
            ordersList = repoOrders.findAllByOrderByIdDesc();
        } else {
            ordersList = repoOrders.findByOrderStatusOrderByIdDesc(orderStatus);
        }

        model.addAttribute("orders", ordersList);
        model.addAttribute("unselectPaymentType", PaymentType.Выберите);
        model.addAttribute("selectedStatus", orderStatus);
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("productStats", ProductStatus.values());
    }

    protected void AddAttributesProducts(Model model) {
        AddAttributes(model);
        List<Products> products = repoProducts.findAllByOrderByIdDesc();
        int quantity = 0;
        for (Products i : products) {
            quantity += i.getQuantity();
        }
        model.addAttribute("quantity", quantity);
        model.addAttribute("products", products);
        model.addAttribute("productNameModel", ProductNameModel.values());
    }

    protected void AddAttributesClients(Model model) {
        AddAttributes(model);
        model.addAttribute("clients", repoClients.findAllByOrderByIdDesc());
    }

    protected void AddAttributesProfiles(Model model) {
        AddAttributes(model);
        model.addAttribute("roles", Roles.values());
        model.addAttribute("users", repoUsers.findAllByOrderByRole());
    }

    protected void AddAttributesAddUser(Model model) {
        AddAttributes(model);
        model.addAttribute("roles", Roles.values());
    }
}
