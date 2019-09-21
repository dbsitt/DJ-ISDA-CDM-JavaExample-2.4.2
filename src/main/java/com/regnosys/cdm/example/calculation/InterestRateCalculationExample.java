package com.regnosys.cdm.example.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

import java.math.BigDecimal;

import org.isda.cdm.FloatingRateIndexEnum;
import org.isda.cdm.calculation.FixedAmount;
import org.isda.cdm.calculation.FloatingAmount;
import org.isda.cdm.functions.CalculationPeriod;
import org.isda.cdm.functions.CalculationPeriodImpl;
import org.isda.cdm.functions.GetRateSchedule;
import org.isda.cdm.functions.GetRateScheduleImpl;
import org.isda.cdm.functions.ResolveRateIndex;
import org.isda.cdm.functions.ResolveRateIndexImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.regnosys.cdm.example.InterestRatePayoutCreation;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.functions.IResult;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;

/**
 * An illustration of how to implement CDM Functions and how to use them when calling CDM Calculations
 *<p>
 * CDM Functions are implemented using the <code>function</code> keyword in Rosetta and forms a contract on what the
 * function's inputs and outputs should be. The rest is left to the clients to implement.  Here we see a couple of ways
 * such functions can be implemented.
 */
public class InterestRateCalculationExample {

    private static final Date REFERENCE_DATE = DateImpl.of(2018, 8, 22);

    public static void main(String[] args) throws Exception {

        // The Fixed Amount calculation as defined in CDM requires the implementation of 2 CDM Functions:
        // CalculationPeriod and DaysInPeriod
        //

        // CalculationPeriodImpl is an example implementation of extracting a 'period' from a
        // CalculationPeriodDates CDM object
        //
        CalculationPeriod calculationPeriodResolver = new CalculationPeriodImpl(REFERENCE_DATE);

        // Calculate the fixed amount, using the function implementations from above
        //
        FixedAmount.CalculationResult fixedAmountResult = new FixedAmount(calculationPeriodResolver, null, null)
                .calculate(InterestRatePayoutCreation.getFixedRatePayout());

        // Function implementations can be as simple as a lambda
        //
        ResolveRateIndex rateIndexResolver = new ResolveRateIndex() {
        	@Override
        	protected BigDecimal doEvaluate(FloatingRateIndexEnum index) {
        		return new BigDecimal("0.0875");
        	}
        };
        GetRateSchedule rateScheduleResolver = new GetRateScheduleImpl();

        // Calculate the floating amount
        //
        FloatingAmount.CalculationResult floatingAmountResult = new FloatingAmount(rateIndexResolver,
                rateScheduleResolver, calculationPeriodResolver, null, null)
                .calculate(InterestRatePayoutCreation.getFloatingRatePayout());

        // Make some assertions on the calculation results
        //
        printJson(fixedAmountResult);
        assertThat("Computed fixed amount matches expectation",
                fixedAmountResult.getFixedAmount(), closeTo(new BigDecimal("750000.0000"), new BigDecimal("0.005")));

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
