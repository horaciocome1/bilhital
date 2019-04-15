package com.company.model.bean;

import java.time.LocalDateTime;

public class Ticket {

    private int id;
    private String orderName;
    private LocalDateTime date;

    public Ticket() {
        date = LocalDateTime.now();
    }

    public Ticket(String orderName) {
        this();
        this.orderName = orderName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return orderName + ", " + date;
    }

}
