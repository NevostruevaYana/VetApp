package com.example.vetapp.database;

public class DrugRequest {

    public final String workerId;
    public final String drugId;
    public final String amount;
    public final String requestDate;
    public final String supplyDate;

    public DrugRequest(String workerId, String drugId,
                       String amount, String requestDate,
                       String supplyDate) {
        this.workerId = workerId;
        this.amount = amount;
        this.drugId = drugId;
        this.requestDate = requestDate;
        this.supplyDate = supplyDate;
    }

    public String getWorkerId() {
        return workerId;
    }

    public String getDrugId() {
        return drugId;
    }

    public String getAmount() {
        return amount;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getSupplyDate() {
        return supplyDate;
    }
}
