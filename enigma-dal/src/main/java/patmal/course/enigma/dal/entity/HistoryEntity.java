package patmal.course.enigma.dal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity representing a processing history entry in the database.
 * Maps to the "history" table.
 *
 * Each row = one encryption/decryption operation
 */
@Entity
@Table(name = "history")
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // Many history entries belong to one session
    @JoinColumn(name = "session_id", nullable = false)
    private SessionEntity session;

    @Column(name = "input", nullable = false)  // The input text (e.g., "HELLO")
    private String input;

    @Column(name = "output", nullable = false)  // The output text (e.g., "XYZAB")
    private String output;

    @Column(name = "code_configuration")  // The machine configuration used
    private String codeConfiguration;

    @Column(name = "processed_at", nullable = false)  // When the operation was performed
    private LocalDateTime processedAt;

    @Column(name = "processing_time_ms")  // How long it took in milliseconds
    private Long processingTimeMs;

    // Default constructor - required by JPA
    public HistoryEntity() {
    }

    public HistoryEntity(SessionEntity session, String input, String output,
                         String codeConfiguration, Long processingTimeMs) {
        this.session = session;
        this.input = input;
        this.output = output;
        this.codeConfiguration = codeConfiguration;
        this.processedAt = LocalDateTime.now();
        this.processingTimeMs = processingTimeMs;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SessionEntity getSession() {
        return session;
    }

    public void setSession(SessionEntity session) {
        this.session = session;
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

    public String getCodeConfiguration() {
        return codeConfiguration;
    }

    public void setCodeConfiguration(String codeConfiguration) {
        this.codeConfiguration = codeConfiguration;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public Long getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(Long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }
}

