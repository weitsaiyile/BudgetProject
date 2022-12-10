package com.example.budgetproject;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;


public class BudgetService {
    private IBudgetRepo budgetRepo;

    public BudgetService(IBudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public double Query(LocalDate startTime, LocalDate endTime) {
        if(endTime.compareTo(startTime)<0){
            return 0;
        }


        if (startTime.equals(endTime)) {
            long durationDay = endTime.toEpochDay() - startTime.toEpochDay() + 1;
            return calAmount(startTime, durationDay);
        } else {
            long durationMonth = YearMonth.from(startTime).until(YearMonth.from(endTime), ChronoUnit.MONTHS);
            if (durationMonth == 0) {
                long durationDay = endTime.toEpochDay() - startTime.toEpochDay() + 1;
                return calAmount(startTime, durationDay);
            } else {
                LocalDate tempDate = startTime;
                double currentAmount = 0;
                for (int i = 0; i <= durationMonth; i++) {
                    long durationDay = 0;
                    if (i == 0) {
                        durationDay = tempDate.lengthOfMonth() - tempDate.getDayOfMonth() + 1;
                    } else if (i == durationMonth) {
                        LocalDate endTimeLastDay = endTime.with(TemporalAdjusters.firstDayOfMonth());
                        durationDay = endTime.toEpochDay() - endTimeLastDay.toEpochDay()+1;
                    } else {
                        durationDay = tempDate.lengthOfMonth();
                    }
                    currentAmount = currentAmount + calAmount(tempDate, durationDay);
                    tempDate = tempDate.plusMonths(1);
                }
                return currentAmount;
            }

        }

    }

    private double calAmount(LocalDate calDate, long durationDay) {
        return getTotalBudget(calDate) / YearMonth.from(calDate).lengthOfMonth() * durationDay;
    }


    private double getTotalBudget(LocalDate startTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMM");
        String lookUpKey = startTime.format(dateFormatter);
        List<Budget> budgetList = budgetRepo.getAll() ;
        if(budgetList.isEmpty()){
            return 0;
        }
        for (Budget budget : budgetRepo.getAll()) {
            if (budget.getYearMonth().equals(lookUpKey)) {
                return budget.getAmount();
            }
        }
        return 0;
    }

}
