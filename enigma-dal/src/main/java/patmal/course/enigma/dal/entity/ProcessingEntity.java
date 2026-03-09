package patmal.course.enigma.dal.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * JPA Entity representing a processing history entry.
 * Maps to the "processing" table.
 */
@Entity
@Table(name = "processing")
public class ProcessingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id", nullable = false)
    private MachineEntity machine;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "input", nullable = false)
    private String input;

    @Column(name = "output", nullable = false)
    private String output;

    @Column(name = "time", nullable = false)
    private long time;

    // Default constructor
    public ProcessingEntity() {
    }

    public ProcessingEntity(MachineEntity machine, String sessionId, String code, String input, String output, long time) {
        this.machine = machine;
        this.sessionId = sessionId;
        this.code = code;
        this.input = input;
        this.output = output;
        this.time = time;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public MachineEntity getMachine() {
        return machine;
    }

    public void setMachine(MachineEntity machine) {
        this.machine = machine;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

