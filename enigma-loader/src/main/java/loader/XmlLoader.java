package loader;

import generated.BTEEnigma;
import jakarta.xml.bind.JAXBException;
import mta.patmal.enigma.machine.component.machine.Machine;
import mta.patmal.enigma.machine.component.reflector.Reflector;
import mta.patmal.enigma.machine.component.rotor.Rotor;

import java.io.File;
import java.io.FileNotFoundException;

public class XmlLoader {
    private static final String XML_FILE_EXTENSION = ".xml";

    private BTEEnigma lastLoadedEnigma;
    private final JaxbLoader jaxbLoader;
    private final JaxbTranslator jaxbTranslator;
    private final XmlValidator xmlValidator;

    public XmlLoader() {
        this(new JaxbLoader(), new JaxbTranslator(), new XmlValidator());
    }

    public XmlLoader(JaxbLoader jaxbLoader, JaxbTranslator jaxbTranslator, XmlValidator xmlValidator) {
        this.jaxbLoader = jaxbLoader;
        this.jaxbTranslator = jaxbTranslator;
        this.xmlValidator = xmlValidator;
    }

    public Machine loadMachineFromXml(String xmlPath) throws FileNotFoundException, JAXBException, IllegalArgumentException, IllegalStateException {
        validateXmlPath(xmlPath);
        File xmlFile = new File(xmlPath);
        validateXmlFile(xmlFile);
        BTEEnigma bteEnigma = jaxbLoader.load(xmlFile);
        xmlValidator.validateMachineFormat(bteEnigma);
        this.lastLoadedEnigma = bteEnigma;
        return jaxbTranslator.createMachineWithoutCode(bteEnigma);
    }

    public int getTotalRotorCount() {
        if (lastLoadedEnigma == null || lastLoadedEnigma.getBTERotors() == null) {
            return 0;
        }
        return lastLoadedEnigma.getBTERotors().getBTERotor().size();
    }

    public int getTotalReflectorCount() {
        if (lastLoadedEnigma == null || lastLoadedEnigma.getBTEReflectors() == null) {
            return 0;
        }
        return lastLoadedEnigma.getBTEReflectors().getBTEReflector().size();
    }

    public int getRequiredRotorsCount() {
        if (lastLoadedEnigma == null || lastLoadedEnigma.getRotorsCount() == null) {
            return 0;
        }
        return lastLoadedEnigma.getRotorsCount().intValue();
    }

    public BTEEnigma getLastLoadedEnigma() {
        return lastLoadedEnigma;
    }

    /**
     * Get the machine name from the XML's name attribute (Exercise 3)
     * @return the machine name or null if not available
     */
    public String getMachineName() {
        if (lastLoadedEnigma == null) {
            return null;
        }
        return lastLoadedEnigma.getName();
    }

    public String getABC() {
        if (lastLoadedEnigma == null || lastLoadedEnigma.getABC() == null) {
            return null;
        }
        return lastLoadedEnigma.getABC().trim();
    }

    public Rotor createRotorById(int rotorId) {
        if (lastLoadedEnigma == null) {
            throw new IllegalStateException("No XML file has been loaded");
        }
        return jaxbTranslator.createRotorById(lastLoadedEnigma, rotorId);
    }

    public Reflector createReflectorByNumericId(int numericId) {
        if (lastLoadedEnigma == null) {
            throw new IllegalStateException("No XML file has been loaded");
        }
        return jaxbTranslator.createReflectorByNumericId(lastLoadedEnigma, numericId);
    }

    private void validateXmlPath(String xmlPath) throws FileNotFoundException {
        if (xmlPath == null || xmlPath.trim().isEmpty()) {
            throw new FileNotFoundException("XML path is empty");
        }
        if (!xmlPath.toLowerCase().endsWith(XML_FILE_EXTENSION)) {
            throw new IllegalArgumentException("File is not an XML (" + XML_FILE_EXTENSION + "): " + xmlPath);
        }
    }

    private void validateXmlFile(File xmlFile) throws FileNotFoundException {
        if (!xmlFile.exists() || !xmlFile.isFile()) {
            throw new FileNotFoundException("XML file not found: " + xmlFile.getAbsolutePath());
        }
    }

    public int getPositionIndexByRightLetter(int rotorId, char letter) {
        if (lastLoadedEnigma == null) {
            throw new IllegalStateException("No XML file has been loaded");
        }
        return jaxbTranslator.getPositionIndexByRightLetter(lastLoadedEnigma, rotorId, letter);
    }

    public char getRightLetterByPosition(int rotorId, int positionIndex) {
        if (lastLoadedEnigma == null) {
            throw new IllegalStateException("No XML file has been loaded");
        }
        return jaxbTranslator.getRightLetterByPosition(lastLoadedEnigma, rotorId, positionIndex);
    }

    /**
     * Get rotor details (id, notch, wiring) for all rotors
     * Used for database storage
     */
    public java.util.List<RotorInfo> getAllRotorDetails() {
        if (lastLoadedEnigma == null || lastLoadedEnigma.getBTERotors() == null) {
            return java.util.Collections.emptyList();
        }

        java.util.List<RotorInfo> details = new java.util.ArrayList<>();
        for (var bteRotor : lastLoadedEnigma.getBTERotors().getBTERotor()) {
            int id = bteRotor.getId();
            int notch = bteRotor.getNotch();

            // Build wiring strings from positioning
            StringBuilder wiringRight = new StringBuilder();
            StringBuilder wiringLeft = new StringBuilder();
            for (var pos : bteRotor.getBTEPositioning()) {
                wiringRight.append(pos.getRight());
                wiringLeft.append(pos.getLeft());
            }

            details.add(new RotorInfo(id, notch, wiringRight.toString(), wiringLeft.toString()));
        }
        return details;
    }

    /**
     * Get reflector details (id, input, output) for all reflectors
     * Used for database storage
     */
    public java.util.List<ReflectorInfo> getAllReflectorDetails() {
        if (lastLoadedEnigma == null || lastLoadedEnigma.getBTEReflectors() == null) {
            return java.util.Collections.emptyList();
        }

        java.util.List<ReflectorInfo> details = new java.util.ArrayList<>();
        for (var bteReflector : lastLoadedEnigma.getBTEReflectors().getBTEReflector()) {
            String id = bteReflector.getId();  // Roman numeral

            // Build input/output strings from reflect pairs
            // Each number is formatted with 2 digits (zero-padded) to ensure same length
            java.util.List<String> inputs = new java.util.ArrayList<>();
            java.util.List<String> outputs = new java.util.ArrayList<>();
            for (var reflect : bteReflector.getBTEReflect()) {
                inputs.add(String.format("%02d", reflect.getInput()));
                outputs.add(String.format("%02d", reflect.getOutput()));
            }

            String input = String.join(",", inputs);
            String output = String.join(",", outputs);

            details.add(new ReflectorInfo(id, input, output));
        }
        return details;
    }

    /**
     * Inner class to hold rotor info
     */
    public static class RotorInfo {
        public final int id;
        public final int notch;
        public final String wiringRight;
        public final String wiringLeft;

        public RotorInfo(int id, int notch, String wiringRight, String wiringLeft) {
            this.id = id;
            this.notch = notch;
            this.wiringRight = wiringRight;
            this.wiringLeft = wiringLeft;
        }
    }

    /**
     * Inner class to hold reflector info
     */
    public static class ReflectorInfo {
        public final String id;
        public final String input;
        public final String output;

        public ReflectorInfo(String id, String input, String output) {
            this.id = id;
            this.input = input;
            this.output = output;
        }
    }
}
