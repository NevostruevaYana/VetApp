package com.example.vetapp.database;

public class Schedule {

    public final String petId;
    public final String clientId;
    public final String officeId;
    public final String workerId;
    public final String serviceId;
    public final String reason;
    public final String dateAndTimeOfVisit;

    public Schedule(String petId, String clientId,
               String officeId, String serviceId,
               String workerId,
               String reason, String dateAndTimeOfVisit) {
        this.clientId = clientId;
        this.petId = petId;
        this.officeId = officeId;
        this.serviceId = serviceId;
        this.workerId = workerId;
        this.reason = reason;
        this.dateAndTimeOfVisit = dateAndTimeOfVisit;
    }

    public String getPetId() {
        return petId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getWorkerId() {
        return workerId;
    }

    public String getReason() {
        return reason;
    }

    public String getDateAndTimeOfVisit() {
        return dateAndTimeOfVisit;
    }
}
