package com.example.budgetproject;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class BudgetService {
    private  IBudgetRepo budgetRepo;
    public BudgetService(IBudgetRepo budgetRepo) {
     this.budgetRepo = budgetRepo;
    }
    public double Query(LocalDate startTime, LocalDate endTime){
        if(endTime.compareTo(startTime)<0){
            return 0.00;
        }

       List<Budget> budgets  = budgetRepo.getAll();
        if (budgets.isEmpty()){
            return 0.00;
        }


        if(startTime.getYear()==endTime.getYear()&&startTime.getMonth().equals(endTime.getMonth())){
            int durationDay = endTime.getDayOfMonth() - startTime.getDayOfMonth()+1;


            String pk = String.format("%d%d",startTime.getYear(),startTime.getMonth().getValue());

            Optional<Budget> budgetOpt = budgets.stream().filter(b ->
                b.getYearMonth().equals(pk)).findFirst();


            if(budgetOpt.isPresent()){
                 return calTotalBudget(startTime,budgetOpt.get().getAmount(),durationDay);
            }else{
                return 0.00;
            }

        }else{
            LocalDate tempStartTime =  startTime;
            int durationMonth=0;
            while (tempStartTime.compareTo(endTime)<=0){
                tempStartTime = tempStartTime.plusMonths(1);
                durationMonth++;
            }
            int em = durationMonth;
            double amount=0;
            for(int i=0;i<durationMonth;i++){

                LocalDate tempCalStartTime = startTime.plusMonths(i);
                LocalDate tempCalEndTime = endTime.plusMonths(i);
                int durationDay=0;
                if (tempCalStartTime.getMonth().getValue() == startTime.getMonth().getValue()) {
                     durationDay = YearMonth.from(tempCalStartTime).lengthOfMonth() - tempCalStartTime.getDayOfMonth()+1;
                }else if(tempCalStartTime.getMonth().getValue()==endTime.getMonth().getValue()){
                     durationDay = endTime.getDayOfMonth();
                }else{
                    durationDay = YearMonth.from(tempCalStartTime).lengthOfMonth();
                }



                String pk = String.format("%d%d",YearMonth.from(tempCalStartTime).getYear(),YearMonth.from(tempCalStartTime).getMonthValue());
                Optional<Budget> budgetOpt = budgets.stream().filter(b ->
                        b.getYearMonth().equals(pk)).findFirst();
                double monthAmount =calTotalBudget(tempCalStartTime,budgetOpt.get().getAmount(),durationDay);
                amount=amount+monthAmount;

            }
            return amount;
        }





    }
    private double calTotalBudget(TemporalAccessor startTime, int budget, int duration){


        YearMonth from = YearMonth.from(startTime);

        int days = from.lengthOfMonth();

        return ( (double)duration/ (double)days) *  (double)budget;
    }
}
