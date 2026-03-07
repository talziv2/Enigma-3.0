package patmal.course.enigma.api.manager;

import mta.patmal.enigma.dto.CodeConfigurationRequestDTO;
import mta.patmal.enigma.dto.CodeConfigurationResultDTO;
import mta.patmal.enigma.dto.MachineConfigSpecs;
import mta.patmal.enigma.engine.Engine;
import mta.patmal.enigma.engine.exceptions.CodeNotConfiguredException;
import mta.patmal.enigma.engine.exceptions.InvalidConfigurationException;
import mta.patmal.enigma.engine.exceptions.MachineNotLoadedException;
import org.springframework.stereotype.Service;

/**
 * Manager for machine configuration operations.
 * Handles code configuration (manual, automatic, reset).
 */
@Service
public class ConfigurationManager {

    private final SessionManager sessionManager;

    public ConfigurationManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Get machine configuration specs for a session
     */
    public MachineConfigSpecs getConfigSpecs(String sessionId)
            throws MachineNotLoadedException, IllegalArgumentException {
        Engine engine = getEngineForSession(sessionId);
        return engine.getMachineConfigSpecs();
    }

    /**
     * Configure machine code manually
     */
    public CodeConfigurationResultDTO configureManual(String sessionId, CodeConfigurationRequestDTO request)
            throws MachineNotLoadedException, InvalidConfigurationException, IllegalArgumentException {
        Engine engine = getEngineForSession(sessionId);
        return engine.codeManual(request);
    }

    /**
     * Configure machine code automatically (random)
     */
    public CodeConfigurationResultDTO configureAutomatic(String sessionId)
            throws MachineNotLoadedException, InvalidConfigurationException, IllegalArgumentException {
        Engine engine = getEngineForSession(sessionId);
        return engine.codeAutomatic();
    }

    /**
     * Reset machine to original code configuration
     */
    public void resetConfiguration(String sessionId)
            throws MachineNotLoadedException, CodeNotConfiguredException, IllegalArgumentException {
        Engine engine = getEngineForSession(sessionId);
        engine.resetCurrentCode();
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

