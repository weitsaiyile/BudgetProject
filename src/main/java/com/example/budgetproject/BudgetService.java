package com.example.budgetproject;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class BudgetService {
    private  IBudgetRepo budgetRepo;
    public BudgetService(IBudgetRepo budgetRepo) {
     this.budgetRepo = budgetRepo;
    }
    public double Query(LocalDateTime startTime,LocalDateTime endTime){
        return 0;
    }
}
