package mta.patmal.enigma.dto;


public class MachineData {
    private final String name;  // Machine name from XML (Exercise 3)
    private final int totalRotors;
    private final int totalReflectors;
    private final int messagesProcessed;
    private String originalCode;
    private String currentCode;

    public MachineData(String name, int totalRotors, int totalReflectors, int messagesProcessed, String originalCode, String currentCode) {
        this.name = name;
        this.totalRotors = totalRotors;
        this.totalReflectors = totalReflectors;
        this.messagesProcessed = messagesProcessed;
        this.originalCode = originalCode;
        this.currentCode = currentCode;
    }

    public MachineData(int totalRotors, int totalReflectors, int messagesProcessed, String originalCode, String currentCode) {
        this(null, totalRotors, totalReflectors, messagesProcessed, originalCode, currentCode);
    }

    public MachineData(int totalRotors, int totalReflectors, int messagesProcessed) {
        this(null, totalRotors, totalReflectors, messagesProcessed, null, null);
    }

    public String getName() {
        return name;
    }
    public int getTotalRotors() {
        return totalRotors;
    }
    public int getTotalReflectors() {
        return totalReflectors;
    }
    public int getMessagesProcessed() {
        return messagesProcessed;
    }
    public String getOriginalCode() {
        return originalCode;
    }
    public String getCurrentCode() {
        return currentCode;
    }
    public void setOriginalCode(String originalCode) {
        this.originalCode = originalCode;
    }
    public void setCurrentCode(String currentCode) {
        this.currentCode = currentCode;
    }
}
