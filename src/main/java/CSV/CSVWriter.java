package CSV;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CSVWriter {
    private static final String SAMPLE_CSV_FILE = "./target/Sheets2GCal_ItEvents.csv";
    public void writerCSV(String[] header, List<String[]> body) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(header))) {
            for (String[] strings : body) {
                csvPrinter.printRecord(Arrays.asList(strings));
            }
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}