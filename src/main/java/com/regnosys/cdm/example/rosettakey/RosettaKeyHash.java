package com.regnosys.cdm.example.rosettakey;

import com.regnosys.cdm.example.InterestRatePayoutCreation;

import org.isda.cdm.InterestRatePayout;
import org.isda.cdm.metafields.MetaFields;
import org.isda.cdm.rosettakey.SerialisingHashFunction;

/**
 * A demonstration of how to set the Rosetta Key using a {@link HashFunction}
 */
public class RosettaKeyHash {

    public static void main(String[] args) {

        // Create a CDM object.
        //
        InterestRatePayout fixedRatePayout = InterestRatePayoutCreation.getFixedRatePayout();


        // Assert the rosettaKey has not been set
        //
        assert fixedRatePayout.getMeta().getGlobalKey() == null : "rosettaKey should be null";


        // Use the a HashFunction to generate a hash value for the fixedRatePayout object created above.
        //
        String hash = new SerialisingHashFunction().hash(fixedRatePayout);


        // Now, set the computed rosetta key onto the original object. Note that toBuilder()
        // creates a new instance of the object.
        //
        InterestRatePayout withRosettaKey = fixedRatePayout.toBuilder().setMeta(MetaFields.builder().setGlobalKey(hash).build()).build();

        System.out.println("rosettaKey is: " + withRosettaKey.getMeta().getGlobalKey());
    }

}
