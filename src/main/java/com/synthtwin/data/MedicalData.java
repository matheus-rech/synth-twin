package com.synthtwin.data;

import java.util.HashMap;
import java.util.Map;

public class MedicalData {

    public record CodedConcept(String code, String display) {}
    public record AllergyData(String code, String display, String category, String severity, String reaction) {}
    public record VaccineData(String cvxCode, String shortName, int doses, String fullName) {}
    public record LabTestData(String loincCode, String description, double normalLow, double normalHigh, String unit) {}

    public static final CodedConcept[] CHRONIC_CONDITIONS = {
        new CodedConcept("44054006", "Type 2 diabetes mellitus"),
        new CodedConcept("73211009", "Diabetes mellitus"),
        new CodedConcept("38341003", "Hypertensive disorder"),
        new CodedConcept("13645005", "Chronic obstructive lung disease"),
        new CodedConcept("195967001", "Asthma"),
        new CodedConcept("53741008", "Coronary artery disease"),
        new CodedConcept("84114007", "Heart failure"),
        new CodedConcept("40930008", "Hypothyroidism"),
        new CodedConcept("15777000", "Prediabetes"),
        new CodedConcept("87433001", "Pulmonary emphysema"),
        new CodedConcept("49436004", "Atrial fibrillation"),
        new CodedConcept("230690007", "Stroke"),
        new CodedConcept("414545008", "Chronic kidney disease"),
        new CodedConcept("396275006", "Osteoarthritis"),
        new CodedConcept("267432004", "Hyperlipidemia"),
        new CodedConcept("59621000", "Hypertension")
    };

    public static final CodedConcept[] ACUTE_CONDITIONS = {
        new CodedConcept("195662009", "Acute viral pharyngitis (common cold)"),
        new CodedConcept("444814009", "Viral sinusitis"),
        new CodedConcept("43878008", "Streptococcal sore throat"),
        new CodedConcept("10509002", "Acute bronchitis"),
        new CodedConcept("40275004", "Contact dermatitis"),
        new CodedConcept("72892002", "Normal pregnancy"),
        new CodedConcept("83074005", "Urinary tract infection"),
        new CodedConcept("57676002", "Joint pain"),
        new CodedConcept("422587007", "Nausea"),
        new CodedConcept("25064002", "Headache")
    };

    public static final CodedConcept[] MEDICATIONS = {
        new CodedConcept("314076", "Lisinopril 10mg"),
        new CodedConcept("197361", "Amlodipine 5mg"),
        new CodedConcept("310798", "Metformin 500mg"),
        new CodedConcept("860975", "Simvastatin 20mg"),
        new CodedConcept("309362", "Omeprazole 20mg"),
        new CodedConcept("200243", "Atorvastatin 40mg"),
        new CodedConcept("213169", "Levothyroxine 50mcg"),
        new CodedConcept("309843", "Metoprolol 25mg"),
        new CodedConcept("835603", "Albuterol 90mcg inhaler"),
        new CodedConcept("308460", "Furosemide 20mg"),
        new CodedConcept("308136", "Fluticasone 50mcg inhaler"),
        new CodedConcept("310429", "Gabapentin 300mg"),
        new CodedConcept("313782", "Acetaminophen 325mg"),
        new CodedConcept("198440", "Ibuprofen 200mg"),
        new CodedConcept("1049502", "Amoxicillin 500mg"),
        new CodedConcept("1049621", "Azithromycin 250mg"),
        new CodedConcept("197511", "Hydrochlorothiazide 25mg"),
        new CodedConcept("198211", "Warfarin 5mg"),
        new CodedConcept("309059", "Aspirin 81mg"),
        new CodedConcept("104490", "Sertraline 50mg")
    };

    public static final AllergyData[] ALLERGIES = {
        new AllergyData("372687004", "Penicillin", "medication", "moderate", "Hives"),
        new AllergyData("387207008", "Ibuprofen", "medication", "mild", "Upset stomach"),
        new AllergyData("372798002", "Sulfonamide", "medication", "severe", "Anaphylaxis"),
        new AllergyData("111088007", "Latex", "environment", "moderate", "Urticaria"),
        new AllergyData("226790001", "Shellfish", "food", "severe", "Anaphylaxis"),
        new AllergyData("417532002", "Peanut", "food", "severe", "Anaphylaxis"),
        new AllergyData("227037002", "Tree nut", "food", "moderate", "Hives"),
        new AllergyData("89811004", "Bee venom", "environment", "severe", "Anaphylaxis"),
        new AllergyData("288328004", "Cat dander", "environment", "mild", "Rhinitis"),
        new AllergyData("418689008", "Grass pollen", "environment", "mild", "Rhinitis")
    };

    public static final VaccineData[] VACCINES = {
        new VaccineData("08", "Hep B", 3, "Hepatitis B"),
        new VaccineData("17", "Hib", 3, "Haemophilus influenzae type b"),
        new VaccineData("10", "IPV", 4, "Inactivated Poliovirus"),
        new VaccineData("20", "DTaP", 5, "Diphtheria, Tetanus, Pertussis"),
        new VaccineData("21", "Varicella", 2, "Varicella"),
        new VaccineData("94", "MMRV", 2, "Measles, Mumps, Rubella, Varicella"),
        new VaccineData("33", "Pneumococcal", 4, "Pneumococcal conjugate"),
        new VaccineData("03", "MMR", 2, "Measles, Mumps, Rubella"),
        new VaccineData("141", "Influenza", 1, "Seasonal Influenza"),
        new VaccineData("133", "Pneumococcal polysaccharide", 1, "Pneumococcal polysaccharide"),
        new VaccineData("187", "Zoster recombinant", 2, "Shingles"),
        new VaccineData("207", "COVID-19 mRNA", 2, "COVID-19")
    };

    public static final LabTestData[] LAB_TESTS = {
        new LabTestData("2339-0", "Glucose [Mass/volume] in Blood", 70.0, 100.0, "mg/dL"),
        new LabTestData("4548-4", "Hemoglobin A1c/Hemoglobin.total", 4.0, 5.6, "%"),
        new LabTestData("2093-3", "Cholesterol [Mass/volume] in Serum", 0.0, 200.0, "mg/dL"),
        new LabTestData("13457-7", "Cholesterol in LDL [Mass/volume] in Serum", 0.0, 100.0, "mg/dL"),
        new LabTestData("2085-9", "Cholesterol in HDL [Mass/volume] in Serum", 40.0, 60.0, "mg/dL"),
        new LabTestData("2571-8", "Triglycerides [Mass/volume] in Serum", 0.0, 150.0, "mg/dL"),
        new LabTestData("8480-6", "Systolic blood pressure", 90.0, 120.0, "mm[Hg]"),
        new LabTestData("8462-4", "Diastolic blood pressure", 60.0, 80.0, "mm[Hg]"),
        new LabTestData("718-7", "Hemoglobin [Mass/volume] in Blood", 12.0, 17.5, "g/dL"),
        new LabTestData("6690-2", "Leukocytes [#/volume] in Blood", 4.5, 11.0, "10*3/uL"),
        new LabTestData("777-3", "Platelets [#/volume] in Blood", 150.0, 400.0, "10*3/uL"),
        new LabTestData("2160-0", "Creatinine [Mass/volume] in Serum", 0.6, 1.2, "mg/dL"),
        new LabTestData("3094-0", "Urea nitrogen [Mass/volume] in Serum", 7.0, 20.0, "mg/dL"),
        new LabTestData("17861-6", "Calcium [Mass/volume] in Serum", 8.5, 10.5, "mg/dL"),
        new LabTestData("2947-0", "Sodium [Moles/volume] in Blood", 136.0, 145.0, "mmol/L")
    };

    public static final String[] PROVIDER_FIRST_NAMES = {
        "James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda", "William", "Barbara",
        "David", "Elizabeth", "Richard", "Susan", "Joseph", "Jessica", "Thomas", "Sarah", "Charles", "Karen"
    };

    public static final String[] PROVIDER_LAST_NAMES = {
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Wilson", "Anderson",
        "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Thompson", "White", "Harris", "Clark"
    };

    public static final Map<String, String[][]> ENCOUNTER_REASONS = new HashMap<>();

    static {
        ENCOUNTER_REASONS.put("PRIMARY_CARE", new String[][]{
            {"185349003", "Encounter for check up"},
            {"310495003", "Mild depression"},
            {"386661006", "Fever"},
            {"267036007", "Dyspnoea"},
            {"68235000", "Nasal congestion"}
        });
        ENCOUNTER_REASONS.put("EMERGENCY", new String[][]{
            {"39848009", "Whiplash injury"},
            {"416940007", "Past history of procedure"},
            {"19169002", "Miscarriage in first trimester"},
            {"55827005", "Left ventricular hypertrophy"},
            {"74400008", "Appendicitis"}
        });
        ENCOUNTER_REASONS.put("SPECIALIST", new String[][]{
            {"160303001", "FH: Ischaemic heart disease"},
            {"314994000", "Patient on maximal tolerated therapy"},
            {"183932001", "Procedure contraindicated"},
            {"38341003", "Hypertensive disorder"},
            {"195967001", "Asthma"}
        });
        ENCOUNTER_REASONS.put("URGENT_CARE", new String[][]{
            {"195662009", "Acute viral pharyngitis"},
            {"444814009", "Viral sinusitis"},
            {"43878008", "Streptococcal sore throat"},
            {"10509002", "Acute bronchitis"},
            {"57676002", "Joint pain"}
        });
        ENCOUNTER_REASONS.put("WELLNESS", new String[][]{
            {"185349003", "Encounter for check up"},
            {"410620009", "Well child visit"},
            {"108220007", "Antepartum care"},
            {"390906007", "Follow-up encounter"},
            {"185389009", "Follow-up visit"}
        });
    }
}
