package patmal.course.enigma.api.manager;

import mta.patmal.enigma.engine.Engine;
import mta.patmal.enigma.engine.exceptions.CodeNotConfiguredException;
import mta.patmal.enigma.engine.exceptions.InvalidInputException;
import mta.patmal.enigma.engine.exceptions.MachineNotLoadedException;
import org.springframework.stereotype.Service;

/**
 * Manager for processing (encrypting/decrypting) text.
 */
@Service
public class ProcessManager {

    private final SessionManager sessionManager;

    public ProcessManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Process (encrypt/decrypt) input text
     * @param sessionId the session ID
     * @param input the text to process
     * @return the processed output
     */
    public String process(String sessionId, String input)
            throws MachineNotLoadedException, CodeNotConfiguredException, InvalidInputException, IllegalArgumentException {
        Engine engine = getEngineForSession(sessionId);
        return engine.process(input);
    }

    /**
     * Helper method to get engine for a session
     */
    private Engine getEngineForSession(String sessionId) throws IllegalArgumentException {
        SessionManager.Session session = sessionManager.getSession(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Unknown sessionID: " + sessionId);
        }
        return session.getEngine();
    }
}

