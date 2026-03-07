package patmal.course.enigma.sessions;

import mta.patmal.enigma.engine.Engine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manager for Enigma machine sessions.
 *
 * A session represents a user's connection to a specific machine.
 * When a user wants to work with a machine, they create a session
 * and receive a session ID. All operations (configure, process, etc.)
 * require this session ID.
 */
public class SessionManager {

    // Map of sessionID -> Session data
    private final Map<String, Session> sessions = new HashMap<>();

    // Interface to get engines by machine name (implemented by LoaderManager)
    private final MachineProvider machineProvider;

    public SessionManager(MachineProvider machineProvider) {
        this.machineProvider = machineProvider;
    }

    /**
     * Create a new session for a machine
     * @param machineName the machine name
     * @return the session ID (e.g., "sess_abc12345")
     * @throws IllegalArgumentException if machine doesn't exist
     */
    public String createSession(String machineName) {
        if (!machineProvider.machineExists(machineName)) {
            throw new IllegalArgumentException("Unknown machine name: " + machineName);
        }

        // Generate unique session ID
        String sessionId = "sess_" + UUID.randomUUID().toString().substring(0, 8);

        // Get the engine for this machine
        Engine engine = machineProvider.getEngine(machineName);

        // Create and store session
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
     * Interface for providing machine engines.
     * This allows SessionManager to get engines without knowing about LoaderManager directly.
     */
    public interface MachineProvider {
        boolean machineExists(String machineName);
        Engine getEngine(String machineName);
    }

    /**
     * Session data class - holds session information
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

