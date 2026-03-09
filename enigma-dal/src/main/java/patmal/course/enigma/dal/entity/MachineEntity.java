package patmal.course.enigma.dal.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * JPA Entity representing an Enigma machine in the database.
 * Maps to the "machines" table.
 */
@Entity
@Table(name = "machines")
public class MachineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "rotors_count", nullable = false)
    private int rotorsCount;

    @Column(name = "abc", nullable = false)
    private String abc;

    // Default constructor - required by JPA
    public MachineEntity() {
    }

    public MachineEntity(String name, int rotorsCount, String abc) {
        this.name = name;
        this.rotorsCount = rotorsCount;
        this.abc = abc;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRotorsCount() {
        return rotorsCount;
    }

    public void setRotorsCount(int rotorsCount) {
        this.rotorsCount = rotorsCount;
    }

    public String getAbc() {
        return abc;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }
}

