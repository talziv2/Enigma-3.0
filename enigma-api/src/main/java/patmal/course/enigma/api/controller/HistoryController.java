package patmal.course.enigma.api.controller;

import mta.patmal.enigma.dto.StatisticsDTO;
import mta.patmal.enigma.engine.Engine;
import mta.patmal.enigma.engine.exceptions.CodeNotConfiguredException;
import mta.patmal.enigma.engine.exceptions.MachineNotLoadedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patmal.course.enigma.api.manager.SessionManager;

/**
 * REST Controller for viewing processing history and statistics.
 * All endpoints require a valid sessionID.
 */
@RestController
@RequestMapping("/enigma")
public class HistoryController {

    private final SessionManager sessionManager;

    public HistoryController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Get processing history and statistics.
     *
     * GET /enigma/history?sessionID=xxx
     *
     * @param sessionId the session ID
     * @return statistics/history or error
     */
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestParam("sessionID") String sessionId) {
        SessionManager.Session session = sessionManager.getSession(sessionId);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Unknown sessionID: " + sessionId));
        }

        try {
            Engine engine = session.getEngine();
            StatisticsDTO stats = engine.statistics();
            return ResponseEntity.ok(stats);
        } catch (MachineNotLoadedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Machine not loaded"));
        } catch (CodeNotConfiguredException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Code not configured yet"));
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

