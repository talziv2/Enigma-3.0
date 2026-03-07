package patmal.course.enigma.api.controller;

import mta.patmal.enigma.engine.Engine;
import mta.patmal.enigma.engine.exceptions.CodeNotConfiguredException;
import mta.patmal.enigma.engine.exceptions.InvalidInputException;
import mta.patmal.enigma.engine.exceptions.MachineNotLoadedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patmal.course.enigma.api.manager.SessionManager;

/**
 * REST Controller for processing (encrypting/decrypting) text.
 * All endpoints require a valid sessionID.
 */
@RestController
@RequestMapping("/enigma")
public class ProcessController {

    private final SessionManager sessionManager;

    public ProcessController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Process (encrypt/decrypt) input text.
     *
     * POST /enigma/process?sessionID=xxx
     *
     * @param sessionId the session ID
     * @param request contains the input text to process
     * @return processed output or error
     */
    @PostMapping("/process")
    public ResponseEntity<?> process(
            @RequestParam("sessionID") String sessionId,
            @RequestBody ProcessRequest request) {

        SessionManager.Session session = sessionManager.getSession(sessionId);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Unknown sessionID: " + sessionId));
        }

        try {
            Engine engine = session.getEngine();
            String output = engine.process(request.getInput());
            return ResponseEntity.ok(new ProcessResponse(true, output, null));
        } catch (MachineNotLoadedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Machine not loaded"));
        } catch (CodeNotConfiguredException e) {
            return ResponseEntity.badRequest()
                    .body(new ProcessResponse(false, null, "Code not configured. Please configure code first."));
        } catch (InvalidInputException e) {
            return ResponseEntity.badRequest()
                    .body(new ProcessResponse(false, null, "Invalid input: " + e.getMessage()));
        }
    }

    // ==================== Request/Response DTOs ====================

    /**
     * Request body for processing text
     */
    public static class ProcessRequest {
        private String input;

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }
    }

    /**
     * Process response with output
     */
    public static class ProcessResponse {
        private boolean success;
        private String output;
        private String error;

        public ProcessResponse(boolean success, String output, String error) {
            this.success = success;
            this.output = output;
            this.error = error;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getOutput() {
            return output;
        }

        public String getError() {
            return error;
        }
    }

    /**
     * Error response
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

