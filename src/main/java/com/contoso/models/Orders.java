package com.contoso.models;

import com.contoso.models.enums.OrderStatus;
import com.contoso.models.enums.PaymentType;

import javax.persistence.*;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String idFioClient;

    private int fullPrice;
    private int fullQuantity;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private String date;

    public Orders() {
    }

    public Orders(String idFioClient, String date) {
        this.paymentType = PaymentType.Выберите;
        this.fullPrice = 0;
        this.fullQuantity = 0;
        this.idFioClient = idFioClient;
        this.orderStatus = OrderStatus.Не_зарезервировано;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getIdFioClient() {
        return idFioClient;
    }

    public void setIdFioClient(String idFioClient) {
        this.idFioClient = idFioClient;
    }

    public int getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(int fullPrice) {
        this.fullPrice = fullPrice;
    }

    public int getFullQuantity() {
        return fullQuantity;
    }

    public void setFullQuantity(int fullQuantity) {
        this.fullQuantity = fullQuantity;
    }

    public OrderStatus getStatus() {
        return orderStatus;
    }

    public void setStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
