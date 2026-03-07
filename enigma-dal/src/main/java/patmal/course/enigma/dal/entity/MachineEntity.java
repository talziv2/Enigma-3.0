package patmal.course.enigma.dal.entity;

import jakarta.persistence.*;

/**
 * JPA Entity representing an Enigma machine in the database.
 * Maps to the "machines" table.
 *
 * Each row in the table = one MachineEntity object
 */
@Entity
@Table(name = "machines")
public class MachineEntity {

    @Id  // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;

    @Column(name = "name", unique = true, nullable = false)  // Machine name must be unique
    private String name;

    @Column(name = "abc", nullable = false)  // The alphabet (e.g., "ABCDEF")
    private String abc;

    @Column(name = "rotors_count", nullable = false)  // How many rotors this machine uses
    private int rotorsCount;

    @Column(name = "xml_content", columnDefinition = "TEXT")  // Original XML file content
    private String xmlContent;

    // Default constructor - required by JPA
    public MachineEntity() {
    }

    public MachineEntity(String name, String abc, int rotorsCount, String xmlContent) {
        this.name = name;
        this.abc = abc;
        this.rotorsCount = rotorsCount;
        this.xmlContent = xmlContent;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbc() {
        return abc;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }

    public int getRotorsCount() {
        return rotorsCount;
    }

    public void setRotorsCount(int rotorsCount) {
        this.rotorsCount = rotorsCount;
    }

    public String getXmlContent() {
        return xmlContent;
    }

    public void setXmlContent(String xmlContent) {
        this.xmlContent = xmlContent;
    }
}

