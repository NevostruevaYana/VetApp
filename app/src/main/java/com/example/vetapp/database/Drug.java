package com.example.vetapp.database;

public class Drug {

    public final String name;
    public final String amount;

    public Drug(String name, String amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }
}
