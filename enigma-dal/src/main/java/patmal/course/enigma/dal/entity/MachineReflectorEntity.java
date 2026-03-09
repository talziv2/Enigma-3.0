package patmal.course.enigma.dal.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * JPA Entity representing a reflector configuration for a machine.
 * Maps to the "machines_reflectors" table.
 *
 * Note: reflector_id uses PostgreSQL enum, stored as String and cast in native queries
 */
@Entity
@Table(name = "machines_reflectors")
public class MachineReflectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id", nullable = false)
    private MachineEntity machine;

    // Store as String - the repository will use native query with cast
    @Column(name = "reflector_id", nullable = false, insertable = false, updatable = false)
    private String reflectorId;

    @Column(name = "input", nullable = false)
    private String input;

    @Column(name = "output", nullable = false)
    private String output;

    // Default constructor
    public MachineReflectorEntity() {
    }

    public MachineReflectorEntity(MachineEntity machine, String reflectorId, String input, String output) {
        this.machine = machine;
        this.reflectorId = reflectorId;
        this.input = input;
        this.output = output;
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

    public String getReflectorId() {
        return reflectorId;
    }

    public void setReflectorId(String reflectorId) {
        this.reflectorId = reflectorId;
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
}

