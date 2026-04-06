package com.synthtwin.exporter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synthtwin.cli.Options;
import com.synthtwin.generator.PatientGenerator;
import com.synthtwin.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FhirExporterTest {

    @Test
    void testExport_createsFiles(@TempDir Path tempDir) throws Exception {
        Options options = new Options();
        options.setPopulation(3);
        options.setSeed(42L);

        PatientGenerator generator = new PatientGenerator(42L);
        List<Patient> patients = generator.generatePatients(options);

        FhirExporter exporter = new FhirExporter();
        exporter.export(patients, tempDir.toString());

        File fhirDir = new File(tempDir.toFile(), "fhir");
        assertTrue(fhirDir.exists());

        for (Patient p : patients) {
            File jsonFile = new File(fhirDir, p.getId() + ".json");
            assertTrue(jsonFile.exists(), "Missing FHIR file for patient " + p.getId());
        }
    }

    @Test
    void testExport_validJson(@TempDir Path tempDir) throws Exception {
        Options options = new Options();
        options.setPopulation(1);
        options.setSeed(42L);

        PatientGenerator generator = new PatientGenerator(42L);
        List<Patient> patients = generator.generatePatients(options);

        FhirExporter exporter = new FhirExporter();
        exporter.export(patients, tempDir.toString());

        File fhirDir = new File(tempDir.toFile(), "fhir");
        File jsonFile = new File(fhirDir, patients.get(0).getId() + ".json");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode bundle = mapper.readTree(jsonFile);

        assertEquals("Bundle", bundle.get("resourceType").asText());

        boolean foundPatient = false;
        for (JsonNode entry : bundle.get("entry")) {
            JsonNode resource = entry.get("resource");
            if ("Patient".equals(resource.get("resourceType").asText())) {
                foundPatient = true;
                assertNotNull(resource.get("name"));
                break;
            }
        }
        assertTrue(foundPatient, "Bundle should contain a Patient resource");
    }
}
