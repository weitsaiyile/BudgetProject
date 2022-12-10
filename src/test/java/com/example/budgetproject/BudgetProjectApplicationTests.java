package com.example.budgetproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
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
                        new Budget("20231",3100),
                        new Budget("20232",280)) );
    }

    @Test
    public void budget_crossMonth(){

       double d =  budgetService.Query(LocalDate.of(2022,12,30),LocalDate.of(2023,2,10));
        Assertions.assertEquals(2+3100+100, d);

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
