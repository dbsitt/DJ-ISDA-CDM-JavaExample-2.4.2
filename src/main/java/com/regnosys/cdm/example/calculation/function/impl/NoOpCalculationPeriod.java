package com.regnosys.cdm.example.calculation.function.impl;

import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.CalculationPeriodData;
import org.isda.cdm.CalculationPeriodDates;
import org.isda.cdm.functions.CalculationPeriod;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class NoOpCalculationPeriod extends CalculationPeriod {

    private final LocalDate startDate;
    private final LocalDate endDate;

    public NoOpCalculationPeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    protected CalculationPeriodData doEvaluate(CalculationPeriodDates calculationPeriodDates) {
        return CalculationPeriodData.builder()
                                    .setStartDate(new DateImpl(startDate))
                                    .setEndDate(new DateImpl(endDate))
                                    .setDaysInPeriod((int) ChronoUnit.DAYS.between(startDate, endDate))
                                    .build();
    }
}
