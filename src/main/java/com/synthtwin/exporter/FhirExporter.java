package com.synthtwin.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.synthtwin.model.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FhirExporter implements Exporter {

    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public void export(List<Patient> patients, String outputDir) throws IOException {
        File fhirDir = new File(outputDir, "fhir");
        fhirDir.mkdirs();

        List<String> patientIds = new ArrayList<>();

        for (Patient patient : patients) {
            Map<String, Object> bundle = buildBundle(patient);
            File outFile = new File(fhirDir, patient.getId() + ".json");
            mapper.writeValue(outFile, bundle);
            patientIds.add(patient.getId());
        }

        // Summary file
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalPatients", patientIds.size());
        summary.put("patientIds", patientIds);
        mapper.writeValue(new File(fhirDir, "bundle_summary.json"), summary);
    }

    private Map<String, Object> buildBundle(Patient patient) {
        Map<String, Object> bundle = new LinkedHashMap<>();
        bundle.put("resourceType", "Bundle");
        bundle.put("id", UUID.randomUUID().toString());
        bundle.put("type", "collection");

        List<Map<String, Object>> entries = new ArrayList<>();

        // Patient resource
        entries.add(createEntry(buildPatientResource(patient)));

        // Encounters
        for (Encounter enc : patient.getEncounters()) {
            entries.add(createEntry(buildEncounterResource(enc, patient.getId())));
        }

        // Conditions
        for (Condition cond : patient.getConditions()) {
            entries.add(createEntry(buildConditionResource(cond, patient.getId())));
        }

        // Medications
        for (Medication med : patient.getMedications()) {
            entries.add(createEntry(buildMedicationRequestResource(med, patient.getId())));
        }

        // Allergies
        for (Allergy allergy : patient.getAllergies()) {
            entries.add(createEntry(buildAllergyIntoleranceResource(allergy, patient.getId())));
        }

        // Immunizations
        for (Vaccination vacc : patient.getVaccinations()) {
            entries.add(createEntry(buildImmunizationResource(vacc, patient.getId())));
        }

        // Lab results (Observations)
        for (LabResult lab : patient.getLabResults()) {
            entries.add(createEntry(buildObservationResource(lab, patient.getId())));
        }

        bundle.put("entry", entries);
        return bundle;
    }

    private Map<String, Object> createEntry(Map<String, Object> resource) {
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("resource", resource);
        return entry;
    }

    private Map<String, Object> buildPatientResource(Patient patient) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("resourceType", "Patient");
        res.put("id", patient.getId());

        Map<String, Object> nameEntry = new LinkedHashMap<>();
        nameEntry.put("use", "official");
        nameEntry.put("family", patient.getLastName());
        nameEntry.put("given", List.of(patient.getFirstName()));
        res.put("name", List.of(nameEntry));

        res.put("gender", "M".equals(patient.getGender()) ? "male" : "female");
        res.put("birthDate", patient.getBirthDate().toString());
        if (patient.getDeathDate() != null) {
            res.put("deceasedDateTime", patient.getDeathDate().toString());
        }

        if (patient.getAddress() != null) {
            Address addr = patient.getAddress();
            Map<String, Object> addrMap = new LinkedHashMap<>();
            addrMap.put("line", List.of(addr.getStreet()));
            addrMap.put("city", addr.getCity());
            addrMap.put("state", addr.getState());
            addrMap.put("postalCode", addr.getZip());
            addrMap.put("country", "US");
            res.put("address", List.of(addrMap));
        }

        return res;
    }

    private Map<String, Object> buildEncounterResource(Encounter enc, String patientId) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("resourceType", "Encounter");
        res.put("id", enc.getId());
        res.put("status", "finished");

        Map<String, Object> classCode = new LinkedHashMap<>();
        classCode.put("code", enc.getType());
        res.put("class", classCode);

        Map<String, Object> subject = new LinkedHashMap<>();
        subject.put("reference", "Patient/" + patientId);
        res.put("subject", subject);

        Map<String, Object> period = new LinkedHashMap<>();
        period.put("start", enc.getDate().toString());
        period.put("end", enc.getDate().toString());
        res.put("period", period);

        if (enc.getReasonCode() != null) {
            Map<String, Object> coding = new LinkedHashMap<>();
            coding.put("code", enc.getReasonCode());
            coding.put("display", enc.getReasonDescription());
            Map<String, Object> reason = new LinkedHashMap<>();
            reason.put("coding", List.of(coding));
            res.put("reasonCode", List.of(reason));
        }

        return res;
    }

    private Map<String, Object> buildConditionResource(Condition cond, String patientId) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("resourceType", "Condition");
        res.put("id", cond.getId());

        Map<String, Object> clinicalStatus = new LinkedHashMap<>();
        Map<String, Object> csCoding = new LinkedHashMap<>();
        csCoding.put("code", cond.getClinicalStatus());
        clinicalStatus.put("coding", List.of(csCoding));
        res.put("clinicalStatus", clinicalStatus);

        Map<String, Object> coding = new LinkedHashMap<>();
        coding.put("system", "http://snomed.info/sct");
        coding.put("code", cond.getCode());
        coding.put("display", cond.getDescription());
        Map<String, Object> code = new LinkedHashMap<>();
        code.put("coding", List.of(coding));
        res.put("code", code);

        Map<String, Object> subject = new LinkedHashMap<>();
        subject.put("reference", "Patient/" + patientId);
        res.put("subject", subject);

        res.put("onsetDateTime", cond.getOnsetDate().toString());
        if (cond.getAbatementDate() != null) {
            res.put("abatementDateTime", cond.getAbatementDate().toString());
        }

        return res;
    }

    private Map<String, Object> buildMedicationRequestResource(Medication med, String patientId) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("resourceType", "MedicationRequest");
        res.put("id", med.getId());
        res.put("status", med.getStopDate() == null ? "active" : "stopped");
        res.put("intent", "order");

        Map<String, Object> coding = new LinkedHashMap<>();
        coding.put("system", "http://www.nlm.nih.gov/research/umls/rxnorm");
        coding.put("code", med.getCode());
        coding.put("display", med.getDescription());
        Map<String, Object> medicationCodeableConcept = new LinkedHashMap<>();
        medicationCodeableConcept.put("coding", List.of(coding));
        res.put("medicationCodeableConcept", medicationCodeableConcept);

        Map<String, Object> subject = new LinkedHashMap<>();
        subject.put("reference", "Patient/" + patientId);
        res.put("subject", subject);

        res.put("authoredOn", med.getStartDate().toString());

        if (med.getDosageInstructions() != null) {
            Map<String, Object> dosage = new LinkedHashMap<>();
            dosage.put("text", med.getDosageInstructions());
            res.put("dosageInstruction", List.of(dosage));
        }

        return res;
    }

    private Map<String, Object> buildAllergyIntoleranceResource(Allergy allergy, String patientId) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("resourceType", "AllergyIntolerance");
        res.put("id", allergy.getId());
        res.put("clinicalStatus", Map.of("coding", List.of(Map.of("code", "active"))));
        res.put("type", allergy.getType());
        res.put("category", List.of(allergy.getCategory()));
        res.put("criticality", "severe".equals(allergy.getSeverity()) ? "high" : "low");

        Map<String, Object> coding = new LinkedHashMap<>();
        coding.put("code", allergy.getCode());
        coding.put("display", allergy.getDescription());
        Map<String, Object> code = new LinkedHashMap<>();
        code.put("coding", List.of(coding));
        res.put("code", code);

        Map<String, Object> patient = new LinkedHashMap<>();
        patient.put("reference", "Patient/" + patientId);
        res.put("patient", patient);

        res.put("onsetDateTime", allergy.getOnsetDate().toString());

        return res;
    }

    private Map<String, Object> buildImmunizationResource(Vaccination vacc, String patientId) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("resourceType", "Immunization");
        res.put("id", vacc.getId());
        res.put("status", "completed");

        Map<String, Object> coding = new LinkedHashMap<>();
        coding.put("system", "http://hl7.org/fhir/sid/cvx");
        coding.put("code", vacc.getCvxCode());
        coding.put("display", vacc.getDescription());
        Map<String, Object> vaccineCode = new LinkedHashMap<>();
        vaccineCode.put("coding", List.of(coding));
        res.put("vaccineCode", vaccineCode);

        Map<String, Object> patient = new LinkedHashMap<>();
        patient.put("reference", "Patient/" + patientId);
        res.put("patient", patient);

        res.put("occurrenceDateTime", vacc.getDate().toString());
        res.put("primarySource", true);

        return res;
    }

    private Map<String, Object> buildObservationResource(LabResult lab, String patientId) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("resourceType", "Observation");
        res.put("id", lab.getId());
        res.put("status", "final");

        Map<String, Object> coding = new LinkedHashMap<>();
        coding.put("system", "http://loinc.org");
        coding.put("code", lab.getLoincCode());
        coding.put("display", lab.getDescription());
        Map<String, Object> code = new LinkedHashMap<>();
        code.put("coding", List.of(coding));
        res.put("code", code);

        Map<String, Object> subject = new LinkedHashMap<>();
        subject.put("reference", "Patient/" + patientId);
        res.put("subject", subject);

        res.put("effectiveDateTime", lab.getDate().toString());

        Map<String, Object> valueQuantity = new LinkedHashMap<>();
        valueQuantity.put("value", lab.getValue());
        valueQuantity.put("unit", lab.getUnit());
        res.put("valueQuantity", valueQuantity);

        String interpretation = lab.getInterpretation();
        if (interpretation != null && !interpretation.isEmpty()) {
            Map<String, Object> interp = new LinkedHashMap<>();
            Map<String, Object> interpCoding = new LinkedHashMap<>();
            interpCoding.put("code", interpretation.substring(0, 1).toUpperCase());
            interp.put("coding", List.of(interpCoding));
            res.put("interpretation", List.of(interp));
        }

        Map<String, Object> refRange = new LinkedHashMap<>();
        Map<String, Object> low = new LinkedHashMap<>();
        low.put("value", lab.getReferenceRangeLow());
        low.put("unit", lab.getUnit());
        Map<String, Object> high = new LinkedHashMap<>();
        high.put("value", lab.getReferenceRangeHigh());
        high.put("unit", lab.getUnit());
        refRange.put("low", low);
        refRange.put("high", high);
        res.put("referenceRange", List.of(refRange));

        return res;
    }
}
