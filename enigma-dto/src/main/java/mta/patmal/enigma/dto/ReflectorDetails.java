package mta.patmal.enigma.dto;

/**
 * DTO containing details about a reflector configuration.
 */
public class ReflectorDetails {
    private final String id;  // Roman numeral (I, II, III, IV, V)
    private final String input;
    private final String output;

    public ReflectorDetails(String id, String input, String output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    public String getId() {
        return id;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }
}

