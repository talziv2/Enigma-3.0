package patmal.course.enigma.api.manager;

import mta.patmal.enigma.dto.MachineData;
import mta.patmal.enigma.engine.Engine;
import mta.patmal.enigma.engine.exceptions.CodeNotConfiguredException;
import mta.patmal.enigma.engine.exceptions.InvalidInputException;
import mta.patmal.enigma.engine.exceptions.MachineNotLoadedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import patmal.course.enigma.dal.entity.MachineEntity;
import patmal.course.enigma.dal.repository.MachineRepository;
import patmal.course.enigma.dal.repository.ProcessingRepository;

import java.util.UUID;

/**
 * Manager for processing (encrypting/decrypting) text.
 */
@Service
public class ProcessManager {

    private final SessionManager sessionManager;
    private final MachineRepository machineRepository;
    private final ProcessingRepository processingRepository;

    public ProcessManager(SessionManager sessionManager,
                          MachineRepository machineRepository,
                          ProcessingRepository processingRepository) {
        this.sessionManager = sessionManager;
        this.machineRepository = machineRepository;
        this.processingRepository = processingRepository;
    }

    /**
     * Process (encrypt/decrypt) input text
     * @param sessionId the session ID
     * @param input the text to process
     * @return the processed output
     */
    @Transactional
    public String process(String sessionId, String input)
            throws MachineNotLoadedException, CodeNotConfiguredException, InvalidInputException, IllegalArgumentException {

        SessionManager.Session session = sessionManager.getSession(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Unknown sessionID: " + sessionId);
        }

        Engine engine = session.getEngine();

        // Get current code configuration before processing
        MachineData machineData = engine.showMachineData();
        String code = machineData.getCurrentCode();

        // Process the input
        long startTime = System.nanoTime();
        String output = engine.process(input);
        long processingTime = System.nanoTime() - startTime;

        // Save to database
        saveProcessingToDatabase(session, sessionId, code, input, output, processingTime);

        return output;
    }

    /**
     * Save processing record to database
     */
    private void saveProcessingToDatabase(SessionManager.Session session, String sessionId,
                                          String code, String input, String output, long timeNanos) {
        try {
            // Find the machine entity by name
            String machineName = session.getMachineName();
            MachineEntity machineEntity = machineRepository.findByName(machineName)
                    .orElse(null);

            if (machineEntity != null) {
                // Insert using native query
                UUID id = UUID.randomUUID();
                processingRepository.insertProcessing(
                    id,
                    machineEntity.getId(),
                    sessionId,
                    code != null ? code : "",
                    input,
                    output,
                    timeNanos / 1_000_000  // Convert to milliseconds
                );
            }
        } catch (Exception e) {
            // Log but don't fail the processing
            System.err.println("Failed to save processing to database: " + e.getMessage());
        }
    }
}

