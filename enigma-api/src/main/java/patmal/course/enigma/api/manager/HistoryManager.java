package patmal.course.enigma.api.manager;

import mta.patmal.enigma.dto.StatisticsDTO;
import mta.patmal.enigma.engine.Engine;
import mta.patmal.enigma.engine.exceptions.CodeNotConfiguredException;
import mta.patmal.enigma.engine.exceptions.MachineNotLoadedException;
import org.springframework.stereotype.Service;

/**
 * Manager for history and statistics operations.
 */
@Service
public class HistoryManager {

    private final SessionManager sessionManager;

    public HistoryManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Get processing history and statistics for a session
     * @param sessionId the session ID
     * @return statistics DTO
     */
    public StatisticsDTO getHistory(String sessionId)
            throws MachineNotLoadedException, CodeNotConfiguredException, IllegalArgumentException {
        Engine engine = getEngineForSession(sessionId);
        return engine.statistics();
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

