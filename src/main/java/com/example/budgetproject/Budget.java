package com.example.budgetproject;

import lombok.Data;

@Data
public class Budget {
    private final String yearMonth;
    private final int amount;
//    private String YearMonth;
//    private int Amount;

    public Budget(String yearMonth, int amount) {

        this.yearMonth = yearMonth;
        this.amount = amount;
    }


}
