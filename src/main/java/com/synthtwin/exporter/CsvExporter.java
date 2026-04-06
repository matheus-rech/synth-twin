package com.synthtwin.exporter;

import com.synthtwin.model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter implements Exporter {

    @Override
    public void export(List<Patient> patients, String outputDir) throws IOException {
        File csvDir = new File(outputDir, "csv");
        csvDir.mkdirs();

        exportPatients(patients, csvDir);
        exportEncounters(patients, csvDir);
        exportConditions(patients, csvDir);
        exportMedications(patients, csvDir);
        exportAllergies(patients, csvDir);
        exportVaccinations(patients, csvDir);
        exportLabResults(patients, csvDir);
    }

    private void exportPatients(List<Patient> patients, File dir) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
            .setHeader("id", "first_name", "last_name", "birth_date", "death_date", "gender",
                       "address_street", "address_city", "address_state", "address_zip", "race", "ethnicity")
            .build();
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(new File(dir, "patients.csv")), format)) {
            for (Patient p : patients) {
                Address a = p.getAddress();
                printer.printRecord(
                    p.getId(), p.getFirstName(), p.getLastName(),
                    p.getBirthDate(), nullableDate(p.getDeathDate()),
                    p.getGender(),
                    a != null ? a.getStreet() : "",
                    a != null ? a.getCity() : "",
                    a != null ? a.getState() : "",
                    a != null ? a.getZip() : "",
                    p.getRace(), p.getEthnicity()
                );
            }
        }
    }

    private void exportEncounters(List<Patient> patients, File dir) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
            .setHeader("id", "patient_id", "type", "date", "reason_code", "reason_description",
                       "provider_name", "cost", "duration_minutes")
            .build();
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(new File(dir, "encounters.csv")), format)) {
            for (Patient p : patients) {
                for (Encounter e : p.getEncounters()) {
                    printer.printRecord(
                        e.getId(), p.getId(), e.getType(), e.getDate(),
                        e.getReasonCode(), e.getReasonDescription(),
                        e.getProviderName(), e.getCost(), e.getDuration()
                    );
                }
            }
        }
    }

    private void exportConditions(List<Patient> patients, File dir) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
            .setHeader("id", "patient_id", "code", "description", "category",
                       "onset_date", "abatement_date", "clinical_status")
            .build();
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(new File(dir, "conditions.csv")), format)) {
            for (Patient p : patients) {
                for (Condition c : p.getConditions()) {
                    printer.printRecord(
                        c.getId(), p.getId(), c.getCode(), c.getDescription(), c.getCategory(),
                        c.getOnsetDate(), nullableDate(c.getAbatementDate()), c.getClinicalStatus()
                    );
                }
            }
        }
    }

    private void exportMedications(List<Patient> patients, File dir) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
            .setHeader("id", "patient_id", "code", "description", "start_date", "stop_date",
                       "reason_code", "reason_description", "dosage_instructions")
            .build();
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(new File(dir, "medications.csv")), format)) {
            for (Patient p : patients) {
                for (Medication m : p.getMedications()) {
                    printer.printRecord(
                        m.getId(), p.getId(), m.getCode(), m.getDescription(),
                        m.getStartDate(), nullableDate(m.getStopDate()),
                        m.getReasonCode(), m.getReasonDescription(), m.getDosageInstructions()
                    );
                }
            }
        }
    }

    private void exportAllergies(List<Patient> patients, File dir) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
            .setHeader("id", "patient_id", "code", "description", "type", "category",
                       "severity", "onset_date", "reaction")
            .build();
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(new File(dir, "allergies.csv")), format)) {
            for (Patient p : patients) {
                for (Allergy a : p.getAllergies()) {
                    printer.printRecord(
                        a.getId(), p.getId(), a.getCode(), a.getDescription(),
                        a.getType(), a.getCategory(), a.getSeverity(),
                        a.getOnsetDate(), a.getReaction()
                    );
                }
            }
        }
    }

    private void exportVaccinations(List<Patient> patients, File dir) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
            .setHeader("id", "patient_id", "cvx_code", "description", "date", "dose_number", "series")
            .build();
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(new File(dir, "vaccinations.csv")), format)) {
            for (Patient p : patients) {
                for (Vaccination v : p.getVaccinations()) {
                    printer.printRecord(
                        v.getId(), p.getId(), v.getCvxCode(), v.getDescription(),
                        v.getDate(), v.getDoseNumber(), v.getSeries()
                    );
                }
            }
        }
    }

    private void exportLabResults(List<Patient> patients, File dir) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
            .setHeader("id", "patient_id", "encounter_id", "loinc_code", "description",
                       "value", "unit", "date", "reference_range_low", "reference_range_high", "interpretation")
            .build();
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(new File(dir, "lab_results.csv")), format)) {
            for (Patient p : patients) {
                for (LabResult lr : p.getLabResults()) {
                    printer.printRecord(
                        lr.getId(), p.getId(), lr.getEncounterId(),
                        lr.getLoincCode(), lr.getDescription(),
                        lr.getValue(), lr.getUnit(), lr.getDate(),
                        lr.getReferenceRangeLow(), lr.getReferenceRangeHigh(),
                        lr.getInterpretation()
                    );
                }
            }
        }
    }

    private String nullableDate(java.time.LocalDate date) {
        return date != null ? date.toString() : "";
    }
}
