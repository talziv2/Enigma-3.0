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

    // Map of machine name -> Engine instance (supports multiple machines)
    private final Map<String, Engine> machines = new HashMap<>();

    /**
     * Load machine from XML file path
     * @param xmlPath path to the XML configuration file
     * @param originalFilename the original filename (for machine name)
     * @return the machine name (derived from original filename)
     * @throws XmlLoadException if loading fails
     */
    public String loadMachine(String xmlPath, String originalFilename) throws XmlLoadException {
        Engine engine = new EngineImpl();
        engine.loadXml(xmlPath);

        // Use original filename (without extension) as machine name
        String machineName = extractMachineName(originalFilename);

        // Store the engine with its machine name
        machines.put(machineName, engine);

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
     * Get engine by machine name
     * @param machineName the machine name
     * @return the Engine instance or null if not found
     */
    public Engine getEngine(String machineName) {
        return machines.get(machineName);
    }

    /**
     * Check if a machine exists
     * @param machineName the machine name
     * @return true if machine exists
     */
    public boolean machineExists(String machineName) {
        return machines.containsKey(machineName);
    }

    /**
     * Get all loaded machine names
     * @return map of machine names to engines
     */
    public Map<String, Engine> getAllMachines() {
        return new HashMap<>(machines);
    }
}

