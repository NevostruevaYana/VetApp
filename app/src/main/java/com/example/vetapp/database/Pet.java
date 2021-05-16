package com.example.vetapp.database;

public class Pet {

    public final String clientId;
    public final String animal;
    public final String name;
    public final String gender;
    public final String dateOfBirth;
    public final String weight;

    public Pet(String clientId, String animal,
               String name, String gender,
               String dateOfBirth, String weight) {
        this.clientId = clientId;
        this.animal = animal;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.weight = weight;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAnimal() {
        return animal;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getWeight() {
        return weight;
    }
}
