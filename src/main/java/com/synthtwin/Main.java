package com.synthtwin;

import com.synthtwin.cli.Options;
import com.synthtwin.exporter.CcdaExporter;
import com.synthtwin.exporter.CsvExporter;
import com.synthtwin.exporter.FhirExporter;
import com.synthtwin.generator.PatientGenerator;
import com.synthtwin.model.Patient;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  SynthTwin - Synthetic Patient Data Generator");
        System.out.println("========================================");

        Options options = Options.parse(args);

        System.out.println("Configuration:");
        System.out.println("  Population : " + options.getPopulation());
        System.out.println("  State      : " + (options.getState() != null ? options.getState() : "any"));
        System.out.println("  Age range  : " + options.getMinAge() + " - " + options.getMaxAge());
        System.out.println("  Gender     : " + (options.getGender() != null ? options.getGender() : "both"));
        System.out.println("  Output dir : " + options.getOutputDir());
        System.out.println("  Formats    : " + options.getFormats());
        System.out.println("  Seed       : " + options.getSeed());
        System.out.println();

        PatientGenerator generator = new PatientGenerator(options.getSeed());
        List<Patient> patients = generator.generatePatients(options);
        System.out.println("Generated " + patients.size() + " patients");

        File outputDir = new File(options.getOutputDir());
        outputDir.mkdirs();

        for (String format : options.getFormats()) {
            try {
                switch (format.trim().toLowerCase()) {
                    case "fhir" -> {
                        System.out.println("Exporting FHIR R4 JSON...");
                        new FhirExporter().export(patients, options.getOutputDir());
                        System.out.println("  -> " + options.getOutputDir() + "/fhir/");
                    }
                    case "ccda" -> {
                        System.out.println("Exporting C-CDA XML...");
                        new CcdaExporter().export(patients, options.getOutputDir());
                        System.out.println("  -> " + options.getOutputDir() + "/ccda/");
                    }
                    case "csv" -> {
                        System.out.println("Exporting CSV...");
                        new CsvExporter().export(patients, options.getOutputDir());
                        System.out.println("  -> " + options.getOutputDir() + "/csv/");
                    }
                    default -> System.err.println("Unknown format: " + format);
                }
            } catch (IOException e) {
                System.err.println("Error exporting " + format + ": " + e.getMessage());
            }
        }

        System.out.println();
        System.out.println("Done! Export complete.");
    }
}
