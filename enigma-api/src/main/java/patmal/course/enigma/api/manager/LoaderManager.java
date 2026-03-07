package patmal.course.enigma.api.manager;

import mta.patmal.enigma.dto.MachineData;
import mta.patmal.enigma.engine.Engine;
import mta.patmal.enigma.engine.EngineImpl;
import mta.patmal.enigma.engine.exceptions.MachineNotLoadedException;
import mta.patmal.enigma.engine.exceptions.XmlLoadException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoaderManager {

    // Map of machine name -> XML file path (to create new engines per session)
    private final Map<String, String> machineXmlPaths = new HashMap<>();

    /**
     * Load machine from XML file path
     * @param xmlPath path to the XML configuration file
     * @param originalFilename the original filename (fallback for machine name)
     * @return the machine name (from XML's name attribute, or filename as fallback)
     * @throws XmlLoadException if loading fails
     * @throws IllegalArgumentException if machine with same name already exists
     */
    public String loadMachine(String xmlPath, String originalFilename) throws XmlLoadException, IllegalArgumentException {
        // Create a temporary engine to validate and get machine name
        Engine engine = new EngineImpl();
        engine.loadXml(xmlPath);

        // Try to get machine name from XML's name attribute
        String machineName;
        try {
            MachineData machineData = engine.showMachineData();
            machineName = machineData.getName();
            // If name is null or empty, fallback to filename
            if (machineName == null || machineName.trim().isEmpty()) {
                machineName = extractMachineName(originalFilename);
            }
        } catch (Exception e) {
            // Fallback to filename if can't get name from XML
            machineName = extractMachineName(originalFilename);
        }

        // Check if machine with this name already exists (per exercise 3 requirement)
        if (machineXmlPaths.containsKey(machineName)) {
            throw new IllegalArgumentException("Machine with name '" + machineName + "' already exists. Machine names must be unique.");
        }

        // Store the XML path (not the engine) so we can create new engines per session
        machineXmlPaths.put(machineName, xmlPath);

        return machineName;
    }

    /**
     * Load machine from XML file path (uses path for machine name)
     * @param xmlPath path to the XML configuration file
     * @return the machine name (derived from filename)
     * @throws XmlLoadException if loading fails
     */
    public String loadMachine(String xmlPath) throws XmlLoadException {
        return loadMachine(xmlPath, xmlPath);
    }

    /**
     * Extract machine name from file path (filename without .xml extension)
     */
    private String extractMachineName(String xmlPath) {
        File file = new File(xmlPath);
        String fileName = file.getName();
        if (fileName.toLowerCase().endsWith(".xml")) {
            return fileName.substring(0, fileName.length() - 4);
        }
        return fileName;
    }

    /**
     * Create a NEW engine instance for a machine (each session gets its own engine)
     * @param machineName the machine name
     * @return a new Engine instance or null if machine not found
     */
    public Engine createNewEngine(String machineName) {
        String xmlPath = machineXmlPaths.get(machineName);
        if (xmlPath == null) {
            return null;
        }

        try {
            Engine engine = new EngineImpl();
            engine.loadXml(xmlPath);
            return engine;
        } catch (XmlLoadException e) {
            return null;
        }
    }

    /**
     * Check if a machine exists
     * @param machineName the machine name
     * @return true if machine exists
     */
    public boolean machineExists(String machineName) {
        return machineXmlPaths.containsKey(machineName);
    }

    /**
     * Get all loaded machine names
     * @return set of machine names
     */
    public java.util.Set<String> getAllMachineNames() {
        return new java.util.HashSet<>(machineXmlPaths.keySet());
    }
}

