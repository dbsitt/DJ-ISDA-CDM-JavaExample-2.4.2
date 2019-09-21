package com.regnosys.cdm.example;

import com.rosetta.model.lib.qualify.QualifyResult;
import com.rosetta.model.lib.qualify.QualifyResultsExtractor;
import org.isda.cdm.EconomicTerms;
import org.isda.cdm.Payout;
import org.isda.cdm.meta.EconomicTermsMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.regnosys.cdm.example.InterestRatePayoutCreation.getFixedRatePayout;
import static com.regnosys.cdm.example.InterestRatePayoutCreation.getFloatingRatePayout;

public class Qualification {

    public static void main(String[] args) {

        // Build an EconomicTerms object using two InterestRatePayouts
        //
        EconomicTerms economicTerms = EconomicTerms.builder()
                .setPayoutBuilder(Payout.builder()
                        .addInterestRatePayout(getFixedRatePayout())
                        .addInterestRatePayout(getFloatingRatePayout()))
                .build();


        // Extract the list of qualification function applicable to the EconomicTerms object
        //
        List<Function<? super EconomicTerms, QualifyResult>> qualifyFunctions = new EconomicTermsMeta().getQualifyFunctions();


        // Use the QualifyResultsExtractor helper to easily make use of qualification results
        //
        String result = new QualifyResultsExtractor<>(qualifyFunctions, economicTerms)
                .getOnlySuccessResult()
                .map(QualifyResult::getName)
                .orElse("Failed to qualify");


        System.out.println(result);
    }
}
