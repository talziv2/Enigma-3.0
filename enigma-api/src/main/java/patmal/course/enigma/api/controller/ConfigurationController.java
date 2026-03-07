package patmal.course.enigma.api.controller;

import mta.patmal.enigma.dto.CodeConfigurationRequestDTO;
import mta.patmal.enigma.dto.CodeConfigurationResultDTO;
import mta.patmal.enigma.dto.MachineConfigSpecs;
import mta.patmal.enigma.engine.Engine;
import mta.patmal.enigma.engine.exceptions.CodeNotConfiguredException;
import mta.patmal.enigma.engine.exceptions.InvalidConfigurationException;
import mta.patmal.enigma.engine.exceptions.MachineNotLoadedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patmal.course.enigma.api.manager.SessionManager;

import java.util.List;

/**
 * REST Controller for managing Enigma machine configuration.
 * All endpoints require a valid sessionID.
 */
@RestController
@RequestMapping("/enigma/config")
public class ConfigurationController {

    private final SessionManager sessionManager;

    public ConfigurationController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Get current machine configuration specs.
     *
     * GET /enigma/config?sessionID=xxx
     */
    @GetMapping
    public ResponseEntity<?> getConfiguration(@RequestParam("sessionID") String sessionId) {
        SessionManager.Session session = sessionManager.getSession(sessionId);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Unknown sessionID: " + sessionId));
        }

        try {
            Engine engine = session.getEngine();
            MachineConfigSpecs specs = engine.getMachineConfigSpecs();
            return ResponseEntity.ok(specs);
        } catch (MachineNotLoadedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Machine not loaded"));
        }
    }

    /**
     * Configure machine code manually.
     *
     * PUT /enigma/config/manual?sessionID=xxx
     */
    @PutMapping("/manual")
    public ResponseEntity<?> configureManual(
            @RequestParam("sessionID") String sessionId,
            @RequestBody ManualConfigRequest request) {

        SessionManager.Session session = sessionManager.getSession(sessionId);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Unknown sessionID: " + sessionId));
        }

        try {
            Engine engine = session.getEngine();

            CodeConfigurationRequestDTO configRequest = new CodeConfigurationRequestDTO(
                    request.getRotorIDs(),
                    request.getStartPositions(),
                    request.getReflectorID(),
                    request.getPlugPairs()
            );

            CodeConfigurationResultDTO result = engine.codeManual(configRequest);
            return ResponseEntity.ok(new ConfigResponse(true, result.getFormattedCode(), null));
        } catch (MachineNotLoadedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Machine not loaded"));
        } catch (InvalidConfigurationException e) {
            return ResponseEntity.badRequest()
                    .body(new ConfigResponse(false, null, e.getMessage()));
        }
    }

    /**
     * Configure machine code automatically (random).
     *
     * PUT /enigma/config/automatic?sessionID=xxx
     */
    @PutMapping("/automatic")
    public ResponseEntity<?> configureAutomatic(@RequestParam("sessionID") String sessionId) {
        SessionManager.Session session = sessionManager.getSession(sessionId);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Unknown sessionID: " + sessionId));
        }

        try {
            Engine engine = session.getEngine();
            CodeConfigurationResultDTO result = engine.codeAutomatic();
            return ResponseEntity.ok(new ConfigResponse(true, result.getFormattedCode(), null));
        } catch (MachineNotLoadedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Machine not loaded"));
        } catch (InvalidConfigurationException e) {
            return ResponseEntity.badRequest()
                    .body(new ConfigResponse(false, null, e.getMessage()));
        }
    }

    /**
     * Reset machine to original code configuration.
     *
     * PUT /enigma/config/reset?sessionID=xxx
     */
    @PutMapping("/reset")
    public ResponseEntity<?> resetConfiguration(@RequestParam("sessionID") String sessionId) {
        SessionManager.Session session = sessionManager.getSession(sessionId);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Unknown sessionID: " + sessionId));
        }

        try {
            Engine engine = session.getEngine();
            engine.resetCurrentCode();
            return ResponseEntity.ok(new ConfigResponse(true, "Code reset successfully", null));
        } catch (MachineNotLoadedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Machine not loaded"));
        } catch (CodeNotConfiguredException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Code not configured yet"));
        }
    }

    // ==================== Request/Response DTOs ====================

    public static class ManualConfigRequest {
        private List<Integer> rotorIDs;
        private int reflectorID;
        private String plugPairs;
        private String startPositions;

        public List<Integer> getRotorIDs() { return rotorIDs; }
        public void setRotorIDs(List<Integer> rotorIDs) { this.rotorIDs = rotorIDs; }
        public int getReflectorID() { return reflectorID; }
        public void setReflectorID(int reflectorID) { this.reflectorID = reflectorID; }
        public String getPlugPairs() { return plugPairs; }
        public void setPlugPairs(String plugPairs) { this.plugPairs = plugPairs; }
        public String getStartPositions() { return startPositions; }
        public void setStartPositions(String startPositions) { this.startPositions = startPositions; }
    }

    public static class ConfigResponse {
        private boolean success;
        private String code;
        private String error;

        public ConfigResponse(boolean success, String code, String error) {
            this.success = success;
            this.code = code;
            this.error = error;
        }

        public boolean isSuccess() { return success; }
        public String getCode() { return code; }
        public String getError() { return error; }
    }

    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) { this.error = error; }
        public String getError() { return error; }
    }
}
