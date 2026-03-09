package patmal.course.enigma.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patmal.course.enigma.api.manager.SessionManager;

/**
 * REST Controller for managing Enigma machine sessions.
 * A session represents a user's connection to a specific loaded machine.
 * All machine operations require a valid session ID.
 */
@RestController
@RequestMapping("/enigma")
public class SessionController {

    private final SessionManager sessionManager;

    public SessionController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Create a new session for a loaded machine.
     *
     * POST /enigma/session
     * Request body: { "machine": "machine-name" }
     *
     * @param request contains the machine name to connect to
     * @return 200 OK with sessionID if successful
     *         409 CONFLICT if machine doesn't exist
     */
    @PostMapping("/session")
    public ResponseEntity<?> createSession(@RequestBody CreateSessionRequest request) {
        try {
            String sessionId = sessionManager.createSession(request.getMachine());
            return ResponseEntity.ok(new CreateSessionResponse(sessionId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Delete an existing session.
     *
     * DELETE /enigma/session?sessionID=xxx
     *
     * @param sessionId the session ID to delete
     * @return 204 NO CONTENT if deleted successfully
     *         404 NOT FOUND if session doesn't exist
     */
    @DeleteMapping("/session")
    public ResponseEntity<?> deleteSession(@RequestParam("sessionID") String sessionId) {
        if (sessionManager.deleteSession(sessionId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Unknown sessionID: " + sessionId));
        }
    }

    // ==================== Request/Response DTOs ====================

    /**
     * Request body for creating a session
     */
    public static class CreateSessionRequest {
        private String machine;

        public String getMachine() {
            return machine;
        }

        public void setMachine(String machine) {
            this.machine = machine;
        }
    }

    /**
     * Response containing the created session ID
     */
    public static class CreateSessionResponse {
        private String sessionID;

        public CreateSessionResponse(String sessionID) {
            this.sessionID = sessionID;
        }

        public String getSessionID() {
            return sessionID;
        }
    }

    /**
     * Error response with message
     */
    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}

