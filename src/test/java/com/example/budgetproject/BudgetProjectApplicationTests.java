package com.example.budgetproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.format.datetime.DateFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class BudgetProjectApplicationTests {

    BudgetService budgetService;
    @BeforeEach
    public void setUp(){
        IBudgetRepo mock = mock(IBudgetRepo.class);

        budgetService = new BudgetService(mock);

        when(mock.getAll()).thenReturn(Arrays.asList
                (new Budget("202212",31),
                        new Budget("202301",3100),
                        new Budget("202302",280)) );
    }

    @Test
    public void budget_sameDay(){
        double actualAmount = selectActualAmount("20221230","20221230");
        amountShouldBe(1,actualAmount);
    }
    @Test
    public void budget_not_found_in_DB(){
        double actualAmount = selectActualAmount("20221030","20221130");
        amountShouldBe(0,actualAmount);
    }
    @Test
    public void budget_different_Day_same_month(){
        double actualAmount = selectActualAmount("20221201","20221210");
        amountShouldBe(10,actualAmount);
    }
    @Test
    public void budget_cross_three_month(){
        double actualAmount = selectActualAmount("20221231","20230202");
        amountShouldBe(1+3100+20,actualAmount);
    }
    @Test
    public void budget_cross_two_month(){
        double actualAmount = selectActualAmount("20221231","20230103");
        amountShouldBe(1+300,actualAmount);
    }

    @Test
    public void endTime_greaterThen_startTime(){
        double actualAmount = selectActualAmount("20220202","20220201");
        amountShouldBe(0,actualAmount);
    }



    private double selectActualAmount(String startTime,String endTime) {
        DateTimeFormatter dateFormatter  = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = LocalDate.parse(startTime, dateFormatter);
        LocalDate endDate = LocalDate.parse(endTime,dateFormatter);
        return budgetService.Query(startDate,endDate);
    }

    private static void amountShouldBe(double expect,double actual) {
        Assertions.assertEquals(expect,actual);
    }


}

class MockBudgetRepo implements  IBudgetRepo{
    List<Budget> budgets = new ArrayList<>();

    public MockBudgetRepo(){
        Budget budget = new Budget("202212 ",3100);
    }

    @Override
    public List<Budget> getAll() {

        return null;
    }
}
