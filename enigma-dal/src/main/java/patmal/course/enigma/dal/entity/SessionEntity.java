package patmal.course.enigma.dal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity representing a user session in the database.
 * Maps to the "sessions" table.
 *
 * A session connects a user to a specific machine.
 */
@Entity
@Table(name = "sessions")
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", unique = true, nullable = false)  // The session ID string (e.g., "sess_abc123")
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY)  // Many sessions can use one machine
    @JoinColumn(name = "machine_id", nullable = false)  // Foreign key to machines table
    private MachineEntity machine;

    @Column(name = "created_at", nullable = false)  // When session was created
    private LocalDateTime createdAt;

    @Column(name = "closed_at")  // When session was closed (null if still open)
    private LocalDateTime closedAt;

    @Column(name = "is_active", nullable = false)  // Is session still active?
    private boolean isActive;

    // Default constructor - required by JPA
    public SessionEntity() {
    }

    public SessionEntity(String sessionId, MachineEntity machine) {
        this.sessionId = sessionId;
        this.machine = machine;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public MachineEntity getMachine() {
        return machine;
    }

    public void setMachine(MachineEntity machine) {
        this.machine = machine;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Close this session - marks it as inactive and records close time
     */
    public void close() {
        this.isActive = false;
        this.closedAt = LocalDateTime.now();
    }
}

