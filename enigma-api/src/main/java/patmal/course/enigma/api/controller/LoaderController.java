package patmal.course.enigma.api.controller;

import mta.patmal.enigma.engine.exceptions.XmlLoadException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import patmal.course.enigma.api.manager.LoaderManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/enigma")
public class LoaderController {

    private final LoaderManager loaderManager;

    public LoaderController(LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
    }

    /**
     * Load a machine from XML file
     * POST /enigma/load
     * Content-Type: multipart/form-data
     */
    @PostMapping(value = "/load", consumes = "multipart/form-data")
    public ResponseEntity<LoadResponse> loadMachine(@RequestParam(value = "file", required = false) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new LoadResponse(false, null, "File not provided or empty"));
        }

        try {
            // Save uploaded file temporarily
            Path tempFile = Files.createTempFile("enigma-", ".xml");
            file.transferTo(tempFile.toFile());

            // Get original filename for machine name
            String originalFilename = file.getOriginalFilename();

            // Load the machine using original filename
            String machineName = loaderManager.loadMachine(tempFile.toString(), originalFilename);

            // Clean up temp file
            Files.deleteIfExists(tempFile);

            return ResponseEntity.ok(new LoadResponse(true, machineName, null));
        } catch (XmlLoadException e) {
            return ResponseEntity.ok(new LoadResponse(false, null, e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body(new LoadResponse(false, null, "Failed to process file: " + e.getMessage()));
        }
    }

    // Response DTO matching the API spec
    public static class LoadResponse {
        private boolean success;
        private String name;
        private String error;

        public LoadResponse(boolean success, String name, String error) {
            this.success = success;
            this.name = name;
            this.error = error;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getName() {
            return name;
        }

        public String getError() {
            return error;
        }
    }
}

