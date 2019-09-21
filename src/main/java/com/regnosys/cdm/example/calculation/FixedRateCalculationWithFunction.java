package com.regnosys.cdm.example.calculation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.regnosys.cdm.example.calculation.function.impl.NoOpCalculationPeriod;
import com.regnosys.cdm.example.InterestRatePayoutCreation;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.functions.IResult;

import org.isda.cdm.*;
import org.isda.cdm.calculation.FixedAmount;
import org.isda.cdm.functions.CalculationPeriod;
import org.isda.cdm.metafields.FieldWithMetaDayCountFractionEnum;
import org.isda.cdm.metafields.FieldWithMetaString;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

/**
 * An illustration of how to pass pre-defined inputs into a calculation via custom function implementations
 */
public class FixedRateCalculationWithFunction {

    public static void main(String[] args) throws Exception {

        // Pre-defined inputs to be used in fixed amount calculation
        //
        LocalDate fixedLegPeriodStart = LocalDate.of(2018, 7, 3);
        LocalDate fixedLegPeriodEnd = LocalDate.of(2018, 10, 3);
        FieldWithMetaDayCountFractionEnum fixedLegDCF = FieldWithMetaDayCountFractionEnum.builder().setValue(DayCountFractionEnum._30E_360).build();
        BigDecimal notional = BigDecimal.valueOf(50_000_000);


        // Instantiate functions to be used in calculations. These function implementations are an example of how
        // trivial data can be passed through to the calculation
        //
        CalculationPeriod fixedLegPeriod = new NoOpCalculationPeriod(fixedLegPeriodStart, fixedLegPeriodEnd);


        // Create a interest rate payout CDM object and stamp input data onto it
        //
        InterestRatePayout fixedRatePayout = InterestRatePayoutCreation.getFixedRatePayout().toBuilder()
                .setQuantityBuilder(ContractualQuantity.builder()
                        .setNotionalScheduleBuilder(NotionalSchedule.builder()
                                .setNotionalStepScheduleBuilder(NonNegativeAmountSchedule.builder()
                                		.setCurrency(FieldWithMetaString.builder().setValue("EUR").build())
                                        .setInitialValue(notional))))
                .setDayCountFraction(fixedLegDCF)
                .build();


        // Calculate the fixed amount, using the function implementations from above
        //
        FixedAmount.CalculationResult fixedAmountResult = new FixedAmount(fixedLegPeriod, null, null)
                .calculate(fixedRatePayout);


        // Make assertions on the calculation results
        //
        printJson(fixedAmountResult);
        assertThat("Computed fixed amount matches expectation",
                fixedAmountResult.getFixedAmount(), closeTo(new BigDecimal("750000.0000"), new BigDecimal("0.005")));
    }

    private static void printJson(IResult result) throws JsonProcessingException {
        String json = RosettaObjectMapper.getDefaultRosettaObjectMapper()
                .writerWithDefaultPrettyPrinter().writeValueAsString(result);
        System.out.println(json);
    }
}
