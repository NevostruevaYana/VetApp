package com.example.vetapp.database;

public class Worker {

    public final String fullName;
    public final String position;
    public final String dateOfBirth;
    public final String phone;

    public Worker(String fullName, String position,
                  String dateOfBirth, String phone) {
        this.fullName = fullName;
        this.position = position;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPosition() {
        return position;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }
}
