package com.regnosys.cdm.example.calculation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.regnosys.cdm.example.InterestRatePayoutCreation;
import com.regnosys.cdm.example.calculation.function.impl.NoOpCalculationPeriod;
import com.regnosys.cdm.example.calculation.function.impl.NoOpResolveRateIndex;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.functions.IResult;
import org.isda.cdm.metafields.FieldWithMetaDayCountFractionEnum;
import org.isda.cdm.metafields.FieldWithMetaString;

import org.isda.cdm.*;
import org.isda.cdm.calculation.FloatingAmount;
import org.isda.cdm.functions.CalculationPeriod;
import org.isda.cdm.functions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

/**
 * An illustration of how to pass pre-defined inputs into a calculation via custom function implementations
 */
public class FloatingRateCalculationWithFunction {

    public static void main(String[] args) throws Exception {

        // Pre-defined inputs to be used in floating amount calculation
        //
        LocalDate floatingLegPeriodStart = LocalDate.of(2018, 7, 3);
        LocalDate floatingLegPeriodEnd = LocalDate.of(2019, 1, 3);
        BigDecimal notional = BigDecimal.valueOf(50_000_000);
        FieldWithMetaDayCountFractionEnum floatingLegDCF = FieldWithMetaDayCountFractionEnum.builder().setValue(DayCountFractionEnum.ACT_365_FIXED).build();
        BigDecimal rate = new BigDecimal("0.0875");


        // Create a interest rate payout CDM object and stamp input data onto it.  This approach re-uses an existing
        // floating rate payout CDM object.
        //
        InterestRatePayout floatingRatePayout = InterestRatePayoutCreation.getFloatingRatePayout().toBuilder()
                .setQuantityBuilder(ContractualQuantity.builder()
                        .setNotionalScheduleBuilder(NotionalSchedule.builder()
                                .setNotionalStepScheduleBuilder((NonNegativeAmountSchedule.NonNegativeAmountScheduleBuilder) NonNegativeAmountSchedule.builder()
                                		.setCurrency(FieldWithMetaString.builder().setValue("EUR").build())
                                        .setInitialValue(notional))))
                .setDayCountFraction(floatingLegDCF)
                .build();


        // Instantiate function implementations to be used in calculations
        //
        CalculationPeriod floatingLegPeriodFunc = new NoOpCalculationPeriod(floatingLegPeriodStart, floatingLegPeriodEnd);
        ResolveRateIndex resolveRateIndexFunc = new NoOpResolveRateIndex(rate);
        GetRateSchedule getRateScheduleFunc = new GetRateScheduleImpl();


        // Calculate the floating amount
        //
        FloatingAmount.CalculationResult floatingAmountResult = new FloatingAmount(resolveRateIndexFunc,
                getRateScheduleFunc, floatingLegPeriodFunc, null, null)
                .calculate(floatingRatePayout);


        // Make assertions on the calculation results
        //
        printJson(floatingAmountResult);
        assertThat("Computed floating amount matches expectation",
                floatingAmountResult.getFloatingAmount(), closeTo(new BigDecimal("2205479.45"), new BigDecimal("0.005")));
    }

    private static void printJson(IResult result) throws JsonProcessingException {
        String json = RosettaObjectMapper.getDefaultRosettaObjectMapper()
                .writerWithDefaultPrettyPrinter().writeValueAsString(result);
        System.out.println(json);
    }
}
