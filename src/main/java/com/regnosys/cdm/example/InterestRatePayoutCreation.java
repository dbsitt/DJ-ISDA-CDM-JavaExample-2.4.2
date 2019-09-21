package com.regnosys.cdm.example;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.isda.cdm.*;
import org.isda.cdm.metafields.FieldWithMetaDayCountFractionEnum;
import org.isda.cdm.metafields.FieldWithMetaFloatingRateIndexEnum;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaBusinessCenters;
import org.isda.cdm.metafields.ReferenceWithMetaParty;

import com.rosetta.model.lib.records.DateImpl;

/**
 * Examples of how to create ISDA CDM(TM) Java objects
 */
public class InterestRatePayoutCreation {

    public static InterestRatePayout getFloatingRatePayout() {
        return InterestRatePayout.builder()
                .setQuantityBuilder(ContractualQuantity.builder()
                        .setNotionalScheduleBuilder(NotionalSchedule.builder()
                                .setNotionalStepScheduleBuilder(NonNegativeAmountSchedule.builder()
                                        .setCurrency(FieldWithMetaString.builder().setValue("EUR").build())
                                        .setInitialValue(BigDecimal.valueOf(50_000_000)))))

                .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder().setValue(DayCountFractionEnum.ACT_365_FIXED).build())

                .setCalculationPeriodDatesBuilder(CalculationPeriodDates.builder()
                        .setEffectiveDateBuilder(AdjustableOrRelativeDate.builder()
                                .setAdjustableDateBuilder(AdjustableDate.builder()
                                        .setUnadjustedDate(DateImpl.of(2018, 1, 3))
                                        .setDateAdjustmentsBuilder(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(BusinessDayConventionEnum.NONE))))
                        .setTerminationDateBuilder(AdjustableOrRelativeDate.builder()
                                .setAdjustableDateBuilder(AdjustableDate.builder()
                                        .setUnadjustedDate(DateImpl.of(2020, 1, 3))
                                        .setDateAdjustmentsBuilder(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                                .setBusinessCentersBuilder(BusinessCenters.builder()
                                                        .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder().setExternalReference("primaryBusinessCenters").build()))))
                        )
                        .setCalculationPeriodFrequencyBuilder(CalculationPeriodFrequency.builder()
                                .setRollConvention(RollConventionEnum._3)
                                .setPeriodMultiplier(6)
                                .setPeriod(PeriodExtendedEnum.M))
                        .setCalculationPeriodDatesAdjustmentsBuilder(BusinessDayAdjustments.builder()
                                .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                .setBusinessCentersBuilder(BusinessCenters.builder()
                                        .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder().setExternalReference("primaryBusinessCenters").build()))))

                .setPaymentDatesBuilder(PaymentDates.builder()
                        .setPaymentFrequencyBuilder(Frequency.builder()
                                .setPeriodMultiplier(3)
                                .setPeriod(PeriodExtendedEnum.M)))

                .setRateSpecificationBuilder(RateSpecification.builder()
                        .setFloatingRateBuilder(FloatingRateSpecification.builder()
                                .setFloatingRateIndex(FieldWithMetaFloatingRateIndexEnum.builder().setValue(FloatingRateIndexEnum.EUR_LIBOR_BBA).build())
                                .setIndexTenorBuilder(Period.builder()
                                        .setPeriod(PeriodEnum.M)
                                        .setPeriodMultiplier(6))))

                .setPayerReceiverBuilder(PayerReceiver.builder()
                        .setPayerPartyReference(ReferenceWithMetaParty.builder().setExternalReference("giga-bank").build())
                        .setReceiverPartyReference(ReferenceWithMetaParty.builder().setExternalReference("mega-bank").build()))

                .build();
    }

    public static InterestRatePayout getFixedRatePayout() {
        return InterestRatePayout.builder()
                .setQuantityBuilder(ContractualQuantity.builder()
                        .setNotionalScheduleBuilder(NotionalSchedule.builder()
                                .setNotionalStepScheduleBuilder(NonNegativeAmountSchedule.builder()
                                        .setCurrency(FieldWithMetaString.builder().setValue("EUR").build())
                                        .setInitialValue(BigDecimal.valueOf(50_000_000)))))

                .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder().setValue(DayCountFractionEnum._30E_360).build())

                .setCalculationPeriodDatesBuilder(CalculationPeriodDates.builder()
                        .setEffectiveDateBuilder(AdjustableOrRelativeDate.builder()
                                .setAdjustableDateBuilder(AdjustableDate.builder()
                                        .setUnadjustedDate(DateImpl.of(2018, 1, 3))
                                        .setDateAdjustmentsBuilder(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(BusinessDayConventionEnum.NONE))))
                        .setTerminationDateBuilder(AdjustableOrRelativeDate.builder()
                                .setAdjustableDateBuilder(AdjustableDate.builder()
                                        .setUnadjustedDate(DateImpl.of(2020, 1, 3))
                                        .setDateAdjustmentsBuilder(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                                .setBusinessCentersBuilder(BusinessCenters.builder()
                                                        .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder().setExternalReference("primaryBusinessCenters").build())))))
                        .setCalculationPeriodFrequencyBuilder(CalculationPeriodFrequency.builder()
                                .setRollConvention(RollConventionEnum._3)
                                .setPeriodMultiplier(3)
                                .setPeriod(PeriodExtendedEnum.M))
                        .setCalculationPeriodDatesAdjustmentsBuilder(BusinessDayAdjustments.builder()
                                .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                .setBusinessCentersBuilder(BusinessCenters.builder()
                                        .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder().setExternalReference("primaryBusinessCenters").build()))))

                .setRateSpecificationBuilder(RateSpecification.builder()
                        .setFixedRateBuilder(Schedule.builder()
                                .setInitialValue(BigDecimal.valueOf(0.06))))

                .setPayerReceiverBuilder(PayerReceiver.builder()
                        .setPayerPartyReference(ReferenceWithMetaParty.builder().setExternalReference("mega-bank").build())
                        .setReceiverPartyReference(ReferenceWithMetaParty.builder().setExternalReference("giga-bank").build()))

                .build();
    }

    public static void main(String[] args) {
        System.out.println(getFixedRatePayout().toString());
        System.out.println(getFloatingRatePayout().toString());
    }
}
