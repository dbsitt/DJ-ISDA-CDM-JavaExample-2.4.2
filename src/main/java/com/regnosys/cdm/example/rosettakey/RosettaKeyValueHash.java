package com.regnosys.cdm.example.rosettakey;

import com.regnosys.cdm.example.InterestRatePayoutCreation;
import com.rosetta.model.lib.RosettaModelObject;

import java.util.Map;

import org.isda.cdm.EconomicTerms;
import org.isda.cdm.Payout;
import org.isda.cdm.rosettakey.RosettaHashCalculator;

public class RosettaKeyValueHash {

    public static void main(String[] args) {

        // Create an EconomicTerms CDM object.
        //
        EconomicTerms economicTerms = EconomicTerms.builder()
                .setPayoutBuilder(Payout.builder()
                        .addInterestRatePayoutBuilder(InterestRatePayoutCreation.getFixedRatePayout().toBuilder()))
                .build();


        // Assert the rosettaKeyValue has not been set
        //
        assert economicTerms.getRosettaKeyValue() == null : "rosettaKeyValue should be null";


        // Use the a HashFunction to generate a hash value for the fixedRatePayout object created above. The
        // RosettaKeyValueHashFunction needs to know which fields are to be included in the hash calculation as so uses
        // a HashFunction with more features.
        //
        RosettaHashCalculator hasher = new RosettaHashCalculator();
        Map<String, RosettaModelObject> hashes = hasher.computeHashes(economicTerms);


        System.out.println("Objects with RosettaKey are : " + hashes);
    }
}
