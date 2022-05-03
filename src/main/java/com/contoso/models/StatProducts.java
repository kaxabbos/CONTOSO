package com.contoso.models;

import com.contoso.models.enums.ProductStatus;

import javax.persistence.*;

@Entity
public class StatProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int quantity;
    private String date;
    private Long idProduct;
    private Long idOrderDetails;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    public StatProducts() {
    }

    public StatProducts(int quantity, String date, Long idProduct, Long idOrderDetails, ProductStatus productStatus) {
        this.quantity = quantity;
        this.date = date;
        this.idProduct = idProduct;
        this.idOrderDetails = idOrderDetails;
        this.productStatus = productStatus;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long productId) {
        this.idProduct = productId;
    }

    public Long getIdOrderDetails() {
        return idOrderDetails;
    }

    public void setIdOrderDetails(Long idOrderDetails) {
        this.idOrderDetails = idOrderDetails;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }
}
