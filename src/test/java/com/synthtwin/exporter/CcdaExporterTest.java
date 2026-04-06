package com.synthtwin.exporter;

import com.synthtwin.cli.Options;
import com.synthtwin.generator.PatientGenerator;
import com.synthtwin.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CcdaExporterTest {

    @Test
    void testExport_createsXmlFiles(@TempDir Path tempDir) throws Exception {
        Options options = new Options();
        options.setPopulation(3);
        options.setSeed(42L);

        PatientGenerator generator = new PatientGenerator(42L);
        List<Patient> patients = generator.generatePatients(options);

        CcdaExporter exporter = new CcdaExporter();
        exporter.export(patients, tempDir.toString());

        File ccdaDir = new File(tempDir.toFile(), "ccda");
        assertTrue(ccdaDir.exists());

        for (Patient p : patients) {
            File xmlFile = new File(ccdaDir, p.getId() + ".xml");
            assertTrue(xmlFile.exists(), "Missing CCDA file for patient " + p.getId());
        }
    }

    @Test
    void testExport_validXml(@TempDir Path tempDir) throws Exception {
        Options options = new Options();
        options.setPopulation(1);
        options.setSeed(42L);

        PatientGenerator generator = new PatientGenerator(42L);
        List<Patient> patients = generator.generatePatients(options);

        CcdaExporter exporter = new CcdaExporter();
        exporter.export(patients, tempDir.toString());

        File ccdaDir = new File(tempDir.toFile(), "ccda");
        File xmlFile = new File(ccdaDir, patients.get(0).getId() + ".xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        Document doc = dbf.newDocumentBuilder().parse(xmlFile);

        assertEquals("ClinicalDocument", doc.getDocumentElement().getTagName());
    }
}
