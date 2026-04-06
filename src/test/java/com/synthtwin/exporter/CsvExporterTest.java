package com.synthtwin.exporter;

import com.synthtwin.cli.Options;
import com.synthtwin.generator.PatientGenerator;
import com.synthtwin.model.Patient;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvExporterTest {

    @Test
    void testExport_createsAllCsvFiles(@TempDir Path tempDir) throws Exception {
        Options options = new Options();
        options.setPopulation(5);
        options.setSeed(42L);

        PatientGenerator generator = new PatientGenerator(42L);
        List<Patient> patients = generator.generatePatients(options);

        CsvExporter exporter = new CsvExporter();
        exporter.export(patients, tempDir.toString());

        File csvDir = new File(tempDir.toFile(), "csv");
        assertTrue(csvDir.exists());

        String[] expectedFiles = {"patients.csv", "encounters.csv", "conditions.csv",
            "medications.csv", "allergies.csv", "vaccinations.csv", "lab_results.csv"};
        for (String fileName : expectedFiles) {
            File f = new File(csvDir, fileName);
            assertTrue(f.exists(), "Missing CSV file: " + fileName);
        }
    }

    @Test
    void testExport_patientsHaveCorrectColumns(@TempDir Path tempDir) throws Exception {
        Options options = new Options();
        options.setPopulation(3);
        options.setSeed(42L);

        PatientGenerator generator = new PatientGenerator(42L);
        List<Patient> patients = generator.generatePatients(options);

        CsvExporter exporter = new CsvExporter();
        exporter.export(patients, tempDir.toString());

        File csvDir = new File(tempDir.toFile(), "csv");
        File patientsFile = new File(csvDir, "patients.csv");

        try (CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build()
                .parse(new FileReader(patientsFile))) {
            assertTrue(parser.getHeaderMap().containsKey("id"));
            assertTrue(parser.getHeaderMap().containsKey("first_name"));
            assertTrue(parser.getHeaderMap().containsKey("last_name"));
            assertTrue(parser.getHeaderMap().containsKey("birth_date"));
            assertTrue(parser.getHeaderMap().containsKey("gender"));
        }
    }
}
