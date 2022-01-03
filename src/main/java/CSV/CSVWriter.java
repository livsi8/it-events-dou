package CSV;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVWriter {
    public void writerCSV(String[] header, List<String[]> body, String fileName) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("./target/Sheets2GCal_ItEvents" + fileName +
                ".csv"));
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