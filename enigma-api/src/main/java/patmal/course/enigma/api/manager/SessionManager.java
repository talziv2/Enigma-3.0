package patmal.course.enigma.api.manager;

import mta.patmal.enigma.engine.Engine;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SessionManager {

    // Map of sessionID -> Session data
    private final Map<String, Session> sessions = new HashMap<>();
    
    private final LoaderManager loaderManager;

    public SessionManager(LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
    }

    /**
     * Create a new session for a machine
     * @param machineName the machine name
     * @return the session ID
     * @throws IllegalArgumentException if machine doesn't exist
     */
    public String createSession(String machineName) {
        if (!loaderManager.machineExists(machineName)) {
            throw new IllegalArgumentException("Unknown machine name: " + machineName);
        }
        
        String sessionId = "sess_" + UUID.randomUUID().toString().substring(0, 8);
        Engine engine = loaderManager.getEngine(machineName);
        
        Session session = new Session(sessionId, machineName, engine);
        sessions.put(sessionId, session);
        
        return sessionId;
    }

    /**
     * Delete a session
     * @param sessionId the session ID
     * @return true if deleted, false if not found
     */
    public boolean deleteSession(String sessionId) {
        return sessions.remove(sessionId) != null;
    }

    /**
     * Get session by ID
     * @param sessionId the session ID
     * @return the Session or null if not found
     */
    public Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * Check if session exists
     * @param sessionId the session ID
     * @return true if exists
     */
    public boolean sessionExists(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    /**
     * Session data class
     */
    public static class Session {
        private final String sessionId;
        private final String machineName;
        private final Engine engine;

        public Session(String sessionId, String machineName, Engine engine) {
            this.sessionId = sessionId;
            this.machineName = machineName;
            this.engine = engine;
        }

        public String getSessionId() {
            return sessionId;
        }

        public String getMachineName() {
            return machineName;
        }

        public Engine getEngine() {
            return engine;
        }
    }
}

