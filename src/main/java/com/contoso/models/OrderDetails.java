package com.contoso.models;

import javax.persistence.*;

@Entity
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long idProduct;

    private Long idOrder;

    private String nameProduct;
    private int price;
    private int unitPrice;
    private int quantity;
    private int quantityMax;

    public OrderDetails() {
    }

    public OrderDetails(Products product, Long idOrder) {
        this.idOrder = idOrder;
        this.idProduct = product.getId();
        this.nameProduct = product.getName();
        this.unitPrice = product.getUnitPrice();
        this.quantityMax = product.getQuantity();
        this.price = 0;
        this.quantity = 0;
    }

    public void set(Products product) {
        this.idProduct = product.getId();
        this.nameProduct = product.getName();
        this.unitPrice = product.getUnitPrice();
        this.quantityMax = product.getQuantity();
        this.price = 0;
        this.quantity = 0;
    }

    public Long getId() {
        return id;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String name) {
        this.nameProduct = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantityMax() {
        return quantityMax;
    }

    public void setQuantityMax(int maxQuantity) {
        this.quantityMax = maxQuantity;
    }
}