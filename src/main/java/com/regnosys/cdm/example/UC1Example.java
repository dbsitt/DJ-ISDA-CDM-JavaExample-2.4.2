package com.regnosys.cdm.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import org.isda.cdm.*;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class UC1Example {
    private static final String task1Input = "UC1_Block_Trade_BT1.json";
    public static void main(String[] args) throws IOException {
        ObjectMapper rosettaObjectMapper = RosettaObjectMapper.getDefaultRosettaObjectMapper();
        Event event = rosettaObjectMapper.readValue(new File(task1Input), Event.class);
        int effectedExecutionSize =  event.getEventEffect().getEffectedExecution().size();
        int primitiveExecutionSize = event.getPrimitive().getExecution().size();
        Execution afterExecution = event.getPrimitive().getExecution().get(0).getAfter().getExecution();
        if ( effectedExecutionSize != primitiveExecutionSize ) {
            throw new IllegalStateException("Size different, effected: " + effectedExecutionSize + " primitive: "+ primitiveExecutionSize);
        }
        for (int i = 0; i < effectedExecutionSize; i++) {
            String effectedKey = event.getEventEffect().getEffectedExecution().get(i).getGlobalReference();
            String primitiveKey = event.getPrimitive().getExecution().get(i).getAfter().getExecution().getMeta().getGlobalKey();

            if (!primitiveKey.equals(effectedKey)) {
                throw new IllegalStateException("Key different, effected: " + effectedKey + " primitive: "+ primitiveKey);
            }
        }
        System.out.println("Parties: ");
        for (Party party: event.getParty()) {
            System.out.println(party.getName().getValue());
            System.out.println(getRole(party, afterExecution));
        }
        System.out.println("Price: ");
        System.out.println(rosettaObjectMapper.writeValueAsString(afterExecution.getPrice()));
        System.out.println("Quantity");
        System.out.println(rosettaObjectMapper.writeValueAsString(afterExecution.getQuantity()));
        System.out.println("Security");
        System.out.println(rosettaObjectMapper.writeValueAsString(afterExecution.getProduct().getSecurity()));
    }

    private static String getRole(Party party, Execution afterExecution) {
        return afterExecution.getPartyRole().stream()
                .filter(role -> role.getPartyReference().getGlobalReference().equals(party.getMeta().getGlobalKey()))
                .map(role -> role.getRole().name())
                .collect(Collectors.joining(", "));

//        throw new IllegalStateException("Role for party " + party.getName().getValue() + " not found");
    }
}
