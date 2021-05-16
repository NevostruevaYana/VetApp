package com.example.vetapp.database;

public class Client {

    public final String fullName;
    public final String address;
    public final String phone;
    public final String regular;

    public Client(String fullName, String address,
                  String phone, String number) {
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.regular = number;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getRegular() {
        return regular;
    }
}
