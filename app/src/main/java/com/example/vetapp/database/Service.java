package com.example.vetapp.database;

public class Service {

    public final String name;
    public final String deliveryTime;
    public final String amountOfMoney;

    public Service(String name, String delivery_time, String amountOfMoney) {
        this.name = name;
        this.deliveryTime = delivery_time;
        this.amountOfMoney = amountOfMoney;
    }

    public String getName() {
        return name;
    }

    public String getDelivery_time() {
        return deliveryTime;
    }

    public String getAmountOfMoney() {
        return amountOfMoney;
    }
}
