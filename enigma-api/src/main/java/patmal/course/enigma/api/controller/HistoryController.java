package patmal.course.enigma.api.controller;

import mta.patmal.enigma.dto.StatisticsDTO;
import mta.patmal.enigma.engine.exceptions.CodeNotConfiguredException;
import mta.patmal.enigma.engine.exceptions.MachineNotLoadedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patmal.course.enigma.api.manager.HistoryManager;

/**
 * REST Controller for viewing processing history and statistics.
 * All endpoints require a valid sessionID.
 */
@RestController
@RequestMapping("/enigma")
public class HistoryController {

    private final HistoryManager historyManager;

    public HistoryController(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    /**
     * Get processing history and statistics.
     * GET /enigma/history?sessionID=xxx
     */
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestParam("sessionID") String sessionId) {
        try {
            StatisticsDTO stats = historyManager.getHistory(sessionId);
            return ResponseEntity.ok(stats);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (MachineNotLoadedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Machine not loaded"));
        } catch (CodeNotConfiguredException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Code not configured yet"));
        }
    }

    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) { this.error = error; }
        public String getError() { return error; }
    }
}
