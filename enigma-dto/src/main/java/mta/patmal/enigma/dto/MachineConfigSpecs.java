package mta.patmal.enigma.dto;

import java.util.List;

public class MachineConfigSpecs {
    private final List<Integer> availableRotorIds;
    private final List<Integer> availableReflectorIds;
    private final int requiredRotorsCount;
    private final String alphabet;
    private final List<RotorDetails> rotorDetails;
    private final List<ReflectorDetails> reflectorDetails;

    public MachineConfigSpecs(List<Integer> availableRotorIds, List<Integer> availableReflectorIds,
            int requiredRotorsCount, String alphabet) {
        this(availableRotorIds, availableReflectorIds, requiredRotorsCount, alphabet, null, null);
    }

    public MachineConfigSpecs(List<Integer> availableRotorIds, List<Integer> availableReflectorIds,
            int requiredRotorsCount, String alphabet, List<RotorDetails> rotorDetails,
            List<ReflectorDetails> reflectorDetails) {
        this.availableRotorIds = availableRotorIds;
        this.availableReflectorIds = availableReflectorIds;
        this.requiredRotorsCount = requiredRotorsCount;
        this.alphabet = alphabet;
        this.rotorDetails = rotorDetails;
        this.reflectorDetails = reflectorDetails;
    }

    public List<Integer> getAvailableRotorIds() {
        return availableRotorIds;
    }

    public List<Integer> getAvailableReflectorIds() {
        return availableReflectorIds;
    }

    public int getRequiredRotorsCount() {
        return requiredRotorsCount;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public List<RotorDetails> getRotorDetails() {
        return rotorDetails;
    }

    public List<ReflectorDetails> getReflectorDetails() {
        return reflectorDetails;
    }
}
