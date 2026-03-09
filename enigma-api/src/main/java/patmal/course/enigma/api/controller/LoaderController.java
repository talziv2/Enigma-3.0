package patmal.course.enigma.api.controller;

import mta.patmal.enigma.engine.exceptions.XmlLoadException;
import org.springframework.http.HttpStatus;
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

    // Directory to store uploaded XML files permanently
    private static final String UPLOAD_DIR = System.getProperty("java.io.tmpdir") + "/enigma-machines/";

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
            // Create upload directory if it doesn't exist
            Path uploadDir = Path.of(UPLOAD_DIR);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Save uploaded file to permanent location
            String originalFilename = file.getOriginalFilename();
            Path savedFile = uploadDir.resolve(originalFilename != null ? originalFilename : "machine.xml");
            file.transferTo(savedFile.toFile());

            // Load the machine (file is NOT deleted - needed for creating sessions later)
            String machineName = loaderManager.loadMachine(savedFile.toString(), originalFilename);

            return ResponseEntity.ok(new LoadResponse(true, machineName, null));
        } catch (XmlLoadException e) {
            return ResponseEntity.ok(new LoadResponse(false, null, e.getMessage()));
        } catch (IllegalArgumentException e) {
            // Machine with same name already exists
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new LoadResponse(false, null, e.getMessage()));
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

