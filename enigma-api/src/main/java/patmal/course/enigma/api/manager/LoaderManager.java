package patmal.course.enigma.api.manager;

import mta.patmal.enigma.dto.MachineData;
import mta.patmal.enigma.engine.Engine;
import mta.patmal.enigma.engine.EngineImpl;
import mta.patmal.enigma.engine.exceptions.XmlLoadException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import patmal.course.enigma.dal.entity.MachineEntity;
import patmal.course.enigma.dal.entity.MachineRotorEntity;
import patmal.course.enigma.dal.entity.MachineReflectorEntity;
import patmal.course.enigma.dal.repository.MachineRepository;
import patmal.course.enigma.dal.repository.MachineRotorRepository;
import patmal.course.enigma.dal.repository.MachineReflectorRepository;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LoaderManager {

    // Map of machine name -> XML file path (to create new engines per session)
    private final Map<String, String> machineXmlPaths = new HashMap<>();

    // Database repositories
    private final MachineRepository machineRepository;
    private final MachineRotorRepository machineRotorRepository;
    private final MachineReflectorRepository machineReflectorRepository;

    public LoaderManager(MachineRepository machineRepository,
                         MachineRotorRepository machineRotorRepository,
                         MachineReflectorRepository machineReflectorRepository) {
        this.machineRepository = machineRepository;
        this.machineRotorRepository = machineRotorRepository;
        this.machineReflectorRepository = machineReflectorRepository;
    }

    /**
     * Load machine from XML file path
     * @param xmlPath path to the XML configuration file
     * @param originalFilename the original filename (fallback for machine name)
     * @return the machine name (from XML's name attribute, or filename as fallback)
     * @throws XmlLoadException if loading fails
     * @throws IllegalArgumentException if machine with same name already exists
     */
    @Transactional
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

        // Check if machine with this name already exists in memory
        if (machineXmlPaths.containsKey(machineName)) {
            throw new IllegalArgumentException("Machine with name '" + machineName + "' already exists. Machine names must be unique.");
        }

        // Check if machine with this name already exists in database
        if (machineRepository.existsByName(machineName)) {
            throw new IllegalArgumentException("Machine with name '" + machineName + "' already exists in database. Machine names must be unique.");
        }

        // Save to database
        saveMachineToDatabase(engine, machineName);

        // Store the XML path (not the engine) so we can create new engines per session
        machineXmlPaths.put(machineName, xmlPath);

        return machineName;
    }

    /**
     * Save machine and its components to database
     */
    private void saveMachineToDatabase(Engine engine, String machineName) {
        try {
            // Get machine specs
            var specs = engine.getMachineConfigSpecs();
            String abc = specs.getAlphabet();
            int rotorsCount = specs.getRequiredRotorsCount();

            // Save machine entity
            MachineEntity machineEntity = new MachineEntity(machineName, rotorsCount, abc);
            machineEntity = machineRepository.save(machineEntity);

            // Save rotors with actual details from XML
            var rotorDetails = specs.getRotorDetails();
            if (rotorDetails != null) {
                for (var rotor : rotorDetails) {
                    MachineRotorEntity rotorEntity = new MachineRotorEntity(
                        machineEntity,
                        rotor.getId(),
                        rotor.getNotch(),
                        rotor.getWiringRight(),
                        rotor.getWiringLeft()
                    );
                    machineRotorRepository.save(rotorEntity);
                }
            }

            // Save reflectors with actual details from XML
            var reflectorDetails = specs.getReflectorDetails();
            if (reflectorDetails != null) {
                for (var reflector : reflectorDetails) {
                    // Use native query to handle PostgreSQL enum type
                    UUID reflectorUuid = UUID.randomUUID();
                    machineReflectorRepository.insertReflector(
                        reflectorUuid,
                        machineEntity.getId(),
                        reflector.getId(),  // Roman numeral from XML
                        reflector.getInput(),
                        reflector.getOutput()
                    );
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to save machine to database: " + e.getMessage(), e);
        }
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
}

