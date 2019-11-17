package com.example.SplitItGo.Model;

public class ExpensesResponse {

    private int id;
    private String bill_name;
    private String description;
    private Double amount;
    private String created_at;
    private int group_name;
    private int payer;

    public ExpensesResponse(int id, String bill_name, String description, Double amount,
                            String created_at, int group_name, int payer) {
        this.id = id;
        this.bill_name = bill_name;
        this.description = description;
        this.amount = amount;
        this.created_at = created_at;
        this.group_name = group_name;
        this.payer = payer;
    }

    public int getId() {
        return id;
    }

    public String getBill_name() {
        return bill_name;
    }

    public String getDescription() {
        return description;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getGroup_name() {
        return group_name;
    }

    public int getPayer() {
        return payer;
    }
}
