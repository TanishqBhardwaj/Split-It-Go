package com.example.SplitItGo.Model;

public class PostExpense {

    private String bill_name;
    private String description;
    private String group_name;
    private String amount;
    private String payer;

    public PostExpense(String bill_name, String description, String group_name, String amount, String payer) {
        this.bill_name = bill_name;
        this.description = description;
        this.group_name = group_name;
        this.amount = amount;
        this.payer = payer;
    }
}
