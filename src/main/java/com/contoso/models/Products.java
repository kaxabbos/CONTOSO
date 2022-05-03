package com.contoso.models;

import com.contoso.models.enums.ProductNameModel;

import javax.persistence.*;

@Entity
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProductNameModel nameModel;

    private int unitPrice;
    private int quantity;

    public Products() {
    }

    public Products(ProductNameModel nameModel, int quantity, int unitPrice) {
        this.nameModel = nameModel;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Long getId() {
        return id;
    }

    public ProductNameModel getNameModel() {
        return nameModel;
    }

    public void setNameModel(ProductNameModel nameModel) {
        this.nameModel = nameModel;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
