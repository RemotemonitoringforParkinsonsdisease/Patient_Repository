package manageFiles;

import POJOS.Signal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * Class responsible for creating and updating CSV files that store
 * physiological signals captured from the BITalino device.
 *
 * It provides methods to create timestamped CSV files and to append
 * signal values in a structured format.
 */
public class ManageFiles {
    public ManageFiles() {
    }

    /**
     * Creates a new CSV file intended to store captured signals.
     * <p>
     * The file is placed inside the <strong>signals_captured/</strong> directory, which is created
     * if it does not exist. The file name includes both the provided date and the current time
     * to ensure uniqueness.
     * </p>
     *
     * @param date the date associated with the capture session
     * @return the full path to the created CSV file
     * @throws IOException if an error occurs while creating the directory or file
     */    public String createSignalsCSVFile(LocalDate date) throws IOException {
        String folder = "signals_captured/";
        Files.createDirectories(Paths.get(folder));
        String time = java.time.LocalTime.now().toString().replace(":", "-").substring(0, 8);
        String fileName = "signalFile_" +  date.toString() + "__" + time + ".csv" ;
        Path path = Paths.get(folder + fileName);

        Files.writeString(path, fileName + "\n-------------------------------------------\n");
        return path.toString();
    }

    /**
     * Appends a captured {@link Signal} to an existing CSV file.
     * Each line is written in the format: SIGNAL_TYPE: value1,value2,value3,...
     *
     * @param filePath the path of the CSV file to update
     * @param signal   the signal containing the type and list of sampled values
     * @throws IOException if an error occurs while writing to the file
     */    public void appendSignalToCSV(String filePath, Signal signal) throws IOException {
        String header = signal.getSignalType().name()+ ": ";
        String values = signal.getValues().stream().map(String::valueOf).collect(Collectors.joining(","));
        String line = header + values + "\n";

        Files.write(Paths.get(filePath), line.getBytes(), StandardOpenOption.APPEND);
    }
}
