package manageFiles;

import POJOS.Signal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class ManageFiles {
    public ManageFiles() {
    }

    //crea un archivo
    public String createSignalsCSVFile(LocalDate date) throws IOException {
        String folder = "signals_captured/";
        Files.createDirectories(Paths.get(folder));
        String time = java.time.LocalTime.now().toString().replace(":", "-").substring(0, 8);
        String fileName = "signalFile_" +  date.toString() + "__" + time + ".csv" ;
        Path path = Paths.get(folder + fileName);

        Files.writeString(path, fileName + "\n-------------------------------------------\n");  // archivo vacío
        return path.toString();
    }

    //añade una señal al archivo ya creado
    public void appendSignalToCSV(String filePath, Signal signal) throws IOException {
        String header = signal.getSignalType().name()+ ": ";
        String values = signal.getValues().stream().map(String::valueOf).collect(Collectors.joining(","));
        String line = header + values + "\n";

        Files.write(Paths.get(filePath), line.getBytes(), StandardOpenOption.APPEND);
    }
}
