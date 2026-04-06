package com.synthtwin.generator;

import com.synthtwin.cli.Options;
import com.synthtwin.data.DemographicData;
import com.synthtwin.data.MedicalData;
import com.synthtwin.model.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PatientGenerator {

    private final Random random;

    public PatientGenerator(long seed) {
        this.random = new Random(seed);
    }

    public List<Patient> generatePatients(Options options) {
        List<Patient> patients = new ArrayList<>();
        for (int i = 0; i < options.getPopulation(); i++) {
            patients.add(generatePatient(options));
        }
        return patients;
    }

    public Patient generatePatient(Options options) {
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID().toString());

        // Gender
        String gender = options.getGender() != null ? options.getGender() : (random.nextBoolean() ? "M" : "F");
        patient.setGender(gender);

        // Name
        if ("M".equals(gender)) {
            patient.setFirstName(DemographicData.MALE_FIRST_NAMES[random.nextInt(DemographicData.MALE_FIRST_NAMES.length)]);
        } else {
            patient.setFirstName(DemographicData.FEMALE_FIRST_NAMES[random.nextInt(DemographicData.FEMALE_FIRST_NAMES.length)]);
        }
        patient.setLastName(DemographicData.LAST_NAMES[random.nextInt(DemographicData.LAST_NAMES.length)]);

        // Birth date - generate within age range
        LocalDate today = LocalDate.now();
        int minAge = options.getMinAge();
        int maxAge = Math.min(options.getMaxAge(), 110);
        int ageYears = minAge + random.nextInt(Math.max(1, maxAge - minAge + 1));
        LocalDate birthDate = today.minusYears(ageYears).minusDays(random.nextInt(365));
        patient.setBirthDate(birthDate);

        // Death date
        int currentAge = (int) ChronoUnit.YEARS.between(birthDate, today);
        LocalDate deathDate = null;
        if (currentAge > 80 && random.nextDouble() < 0.40) {
            long daysAlive = ChronoUnit.DAYS.between(birthDate, today);
            long deathDayOffset = (long)(daysAlive * 0.75) + random.nextInt((int)(daysAlive * 0.25) + 1);
            deathDate = birthDate.plusDays(deathDayOffset);
            if (deathDate.isAfter(today)) deathDate = today.minusDays(random.nextInt(365) + 1);
        } else if (currentAge > 70 && random.nextDouble() < 0.20) {
            long daysAlive = ChronoUnit.DAYS.between(birthDate, today);
            long deathDayOffset = (long)(daysAlive * 0.80) + random.nextInt((int)(daysAlive * 0.20) + 1);
            deathDate = birthDate.plusDays(deathDayOffset);
            if (deathDate.isAfter(today)) deathDate = today.minusDays(random.nextInt(365) + 1);
        } else if (currentAge > 60 && random.nextDouble() < 0.10) {
            long daysAlive = ChronoUnit.DAYS.between(birthDate, today);
            long deathDayOffset = (long)(daysAlive * 0.85) + random.nextInt((int)(daysAlive * 0.15) + 1);
            deathDate = birthDate.plusDays(deathDayOffset);
            if (deathDate.isAfter(today)) deathDate = today.minusDays(random.nextInt(365) + 1);
        }
        patient.setDeathDate(deathDate);

        LocalDate endDate = deathDate != null ? deathDate : today;

        // Demographics
        patient.setRace(DemographicData.RACES[random.nextInt(DemographicData.RACES.length)]);
        patient.setEthnicity(DemographicData.ETHNICITIES[random.nextInt(DemographicData.ETHNICITIES.length)]);

        // Address
        String state = options.getState() != null ? options.getState() : DemographicData.getRandomState(random);
        String city = options.getCity() != null ? options.getCity() : DemographicData.getRandomCity(state, random);
        String streetNum = String.valueOf(100 + random.nextInt(9900));
        String streetName = DemographicData.STREET_NAMES[random.nextInt(DemographicData.STREET_NAMES.length)];
        String streetType = DemographicData.STREET_TYPES[random.nextInt(DemographicData.STREET_TYPES.length)];
        String zip = String.format("%05d", 10000 + random.nextInt(89999));
        patient.setAddress(new Address(streetNum + " " + streetName + " " + streetType, city, state, zip));

        // Conditions
        generateConditions(patient, birthDate, endDate);

        // Medications based on conditions
        generateMedications(patient, endDate);

        // Allergies
        if (random.nextDouble() < 0.20) {
            int allergyCount = 1 + random.nextInt(2);
            generateAllergies(patient, allergyCount, birthDate, endDate);
        }

        // Vaccinations
        generateVaccinations(patient, birthDate, endDate);

        // Encounters
        generateEncounters(patient, birthDate, endDate);

        // Lab results for primary care encounters
        generateLabResults(patient);

        return patient;
    }

    private void generateConditions(Patient patient, LocalDate birthDate, LocalDate endDate) {
        int age = (int) ChronoUnit.YEARS.between(birthDate, endDate);

        // Chronic conditions for older patients
        if (age > 40) {
            int chronicCount = 1 + random.nextInt(Math.min(3, 1 + (age - 40) / 15));
            Set<Integer> usedIndices = new HashSet<>();
            for (int i = 0; i < chronicCount; i++) {
                int idx;
                do { idx = random.nextInt(MedicalData.CHRONIC_CONDITIONS.length); } while (usedIndices.contains(idx));
                usedIndices.add(idx);
                MedicalData.CodedConcept concept = MedicalData.CHRONIC_CONDITIONS[idx];

                Condition condition = new Condition();
                condition.setId(UUID.randomUUID().toString());
                condition.setCode(concept.code());
                condition.setDescription(concept.display());
                condition.setCategory("chronic");
                // Onset after age 40
                int yearsAfter40 = age - 40;
                int onsetOffset = random.nextInt(Math.max(1, yearsAfter40 * 365));
                LocalDate onsetDate = birthDate.plusYears(40).plusDays(onsetOffset);
                if (onsetDate.isAfter(endDate)) onsetDate = endDate.minusDays(random.nextInt(365) + 1);
                condition.setOnsetDate(onsetDate);
                condition.setClinicalStatus("active");
                patient.getConditions().add(condition);
            }
        }

        // Acute conditions for all ages
        int acuteCount = random.nextInt(3);
        for (int i = 0; i < acuteCount; i++) {
            MedicalData.CodedConcept concept = MedicalData.ACUTE_CONDITIONS[random.nextInt(MedicalData.ACUTE_CONDITIONS.length)];
            Condition condition = new Condition();
            condition.setId(UUID.randomUUID().toString());
            condition.setCode(concept.code());
            condition.setDescription(concept.display());
            condition.setCategory("acute");
            long daysRange = ChronoUnit.DAYS.between(birthDate, endDate);
            if (daysRange < 1) daysRange = 1;
            LocalDate onsetDate = birthDate.plusDays(random.nextInt((int) Math.min(daysRange, Integer.MAX_VALUE)));
            condition.setOnsetDate(onsetDate);
            // Acute conditions resolve after 7-21 days
            LocalDate abatementDate = onsetDate.plusDays(7 + random.nextInt(14));
            if (abatementDate.isAfter(endDate)) abatementDate = endDate;
            condition.setAbatementDate(abatementDate);
            condition.setClinicalStatus("resolved");
            patient.getConditions().add(condition);
        }
    }

    private void generateMedications(Patient patient, LocalDate endDate) {
        for (Condition condition : patient.getConditions()) {
            if ("chronic".equals(condition.getCategory())) {
                int medCount = 1 + random.nextInt(2);
                for (int i = 0; i < medCount; i++) {
                    MedicalData.CodedConcept medConcept = MedicalData.MEDICATIONS[random.nextInt(MedicalData.MEDICATIONS.length)];
                    Medication med = new Medication();
                    med.setId(UUID.randomUUID().toString());
                    med.setCode(medConcept.code());
                    med.setDescription(medConcept.display());
                    // Start after condition onset
                    int daysAfterOnset = random.nextInt(90) + 1;
                    LocalDate startDate = condition.getOnsetDate().plusDays(daysAfterOnset);
                    if (startDate.isAfter(endDate)) startDate = endDate.minusDays(1);
                    med.setStartDate(startDate);
                    med.setReasonCode(condition.getCode());
                    med.setReasonDescription(condition.getDescription());
                    med.setDosageInstructions("Take as directed");
                    patient.getMedications().add(med);
                }
            }
        }
    }

    private void generateAllergies(Patient patient, int count, LocalDate birthDate, LocalDate endDate) {
        Set<Integer> usedIndices = new HashSet<>();
        for (int i = 0; i < count; i++) {
            int idx;
            do { idx = random.nextInt(MedicalData.ALLERGIES.length); } while (usedIndices.contains(idx));
            usedIndices.add(idx);
            MedicalData.AllergyData allergyData = MedicalData.ALLERGIES[idx];

            Allergy allergy = new Allergy();
            allergy.setId(UUID.randomUUID().toString());
            allergy.setCode(allergyData.code());
            allergy.setDescription(allergyData.display());
            allergy.setType("allergy");
            allergy.setCategory(allergyData.category());
            allergy.setSeverity(allergyData.severity());
            allergy.setReaction(allergyData.reaction());

            long daysRange = ChronoUnit.DAYS.between(birthDate, endDate);
            if (daysRange < 1) daysRange = 1;
            LocalDate onsetDate = birthDate.plusDays(random.nextInt((int) Math.min(daysRange, Integer.MAX_VALUE)));
            allergy.setOnsetDate(onsetDate);

            patient.getAllergies().add(allergy);
        }
    }

    private void generateVaccinations(Patient patient, LocalDate birthDate, LocalDate endDate) {
        int age = (int) ChronoUnit.YEARS.between(birthDate, endDate);

        // Childhood vaccines (first 6 years)
        String[] childhoodVaccines = {"08", "17", "10", "20", "21", "94", "33", "03"};
        for (String cvxCode : childhoodVaccines) {
            MedicalData.VaccineData vData = findVaccine(cvxCode);
            if (vData == null) continue;
            for (int dose = 1; dose <= vData.doses(); dose++) {
                LocalDate vaccDate = birthDate.plusMonths(2L * dose).plusDays(random.nextInt(14));
                if (vaccDate.isAfter(endDate)) break;
                Vaccination vacc = new Vaccination();
                vacc.setId(UUID.randomUUID().toString());
                vacc.setCvxCode(cvxCode);
                vacc.setDescription(vData.fullName());
                vacc.setDate(vaccDate);
                vacc.setDoseNumber(dose);
                vacc.setSeries(vData.shortName());
                patient.getVaccinations().add(vacc);
            }
        }

        // Annual influenza for everyone
        if (age >= 1) {
            MedicalData.VaccineData fluData = findVaccine("141");
            if (fluData != null) {
                for (int yr = 1; yr <= Math.min(age, 10); yr++) {
                    LocalDate vaccDate = birthDate.plusYears(yr).withMonth(10).withDayOfMonth(1 + random.nextInt(30));
                    if (vaccDate.isAfter(endDate)) break;
                    Vaccination vacc = new Vaccination();
                    vacc.setId(UUID.randomUUID().toString());
                    vacc.setCvxCode("141");
                    vacc.setDescription(fluData.fullName());
                    vacc.setDate(vaccDate);
                    vacc.setDoseNumber(1);
                    vacc.setSeries(fluData.shortName());
                    patient.getVaccinations().add(vacc);
                }
            }
        }

        // Adult vaccines
        if (age >= 65) {
            MedicalData.VaccineData ppsv = findVaccine("133");
            if (ppsv != null) {
                LocalDate vaccDate = birthDate.plusYears(65).plusDays(random.nextInt(365));
                if (!vaccDate.isAfter(endDate)) {
                    Vaccination vacc = new Vaccination();
                    vacc.setId(UUID.randomUUID().toString());
                    vacc.setCvxCode("133");
                    vacc.setDescription(ppsv.fullName());
                    vacc.setDate(vaccDate);
                    vacc.setDoseNumber(1);
                    vacc.setSeries(ppsv.shortName());
                    patient.getVaccinations().add(vacc);
                }
            }
        }
        if (age >= 50) {
            MedicalData.VaccineData shingles = findVaccine("187");
            if (shingles != null) {
                for (int dose = 1; dose <= 2; dose++) {
                    LocalDate vaccDate = birthDate.plusYears(50).plusMonths((long)(dose - 1) * 6 + random.nextInt(3));
                    if (vaccDate.isAfter(endDate)) break;
                    Vaccination vacc = new Vaccination();
                    vacc.setId(UUID.randomUUID().toString());
                    vacc.setCvxCode("187");
                    vacc.setDescription(shingles.fullName());
                    vacc.setDate(vaccDate);
                    vacc.setDoseNumber(dose);
                    vacc.setSeries(shingles.shortName());
                    patient.getVaccinations().add(vacc);
                }
            }
        }
        if (age >= 12) {
            MedicalData.VaccineData covid = findVaccine("207");
            if (covid != null) {
                // Give COVID vaccine in 2021
                LocalDate covidStart = LocalDate.of(2021, 3, 1);
                if (!covidStart.isBefore(birthDate) && !covidStart.isAfter(endDate)) {
                    for (int dose = 1; dose <= 2; dose++) {
                        LocalDate vaccDate = covidStart.plusDays((long)(dose - 1) * 28 + random.nextInt(7));
                        if (vaccDate.isAfter(endDate)) break;
                        Vaccination vacc = new Vaccination();
                        vacc.setId(UUID.randomUUID().toString());
                        vacc.setCvxCode("207");
                        vacc.setDescription(covid.fullName());
                        vacc.setDate(vaccDate);
                        vacc.setDoseNumber(dose);
                        vacc.setSeries(covid.shortName());
                        patient.getVaccinations().add(vacc);
                    }
                }
            }
        }
    }

    private MedicalData.VaccineData findVaccine(String cvxCode) {
        for (MedicalData.VaccineData vd : MedicalData.VACCINES) {
            if (vd.cvxCode().equals(cvxCode)) return vd;
        }
        return null;
    }

    private void generateEncounters(Patient patient, LocalDate birthDate, LocalDate endDate) {
        int age = (int) ChronoUnit.YEARS.between(birthDate, endDate);

        // Primary care: 1-2 per year
        int years = Math.min(age, 110);
        for (int yr = 0; yr <= years; yr++) {
            int visitsThisYear = 1 + random.nextInt(2);
            for (int v = 0; v < visitsThisYear; v++) {
                LocalDate visitDate = birthDate.plusYears(yr).plusDays(random.nextInt(365));
                if (visitDate.isAfter(endDate)) continue;
                if (visitDate.isBefore(birthDate)) continue;
                patient.getEncounters().add(createEncounter("PRIMARY_CARE", visitDate));
            }
        }

        // Emergency: 0-2 total
        int emergencyCount = random.nextInt(3);
        for (int i = 0; i < emergencyCount; i++) {
            long daysRange = ChronoUnit.DAYS.between(birthDate, endDate);
            if (daysRange < 1) continue;
            LocalDate visitDate = birthDate.plusDays(random.nextInt((int) Math.min(daysRange, Integer.MAX_VALUE)));
            patient.getEncounters().add(createEncounter("EMERGENCY", visitDate));
        }

        // Specialist visits for conditions
        for (Condition c : patient.getConditions()) {
            if ("chronic".equals(c.getCategory()) && random.nextDouble() < 0.6) {
                LocalDate visitDate = c.getOnsetDate().plusDays(30 + random.nextInt(180));
                if (!visitDate.isAfter(endDate)) {
                    patient.getEncounters().add(createEncounter("SPECIALIST", visitDate));
                }
            }
        }
    }

    private Encounter createEncounter(String type, LocalDate date) {
        Encounter encounter = new Encounter();
        encounter.setId(UUID.randomUUID().toString());
        encounter.setType(type);
        encounter.setDate(date);

        String[][] reasons = MedicalData.ENCOUNTER_REASONS.get(type);
        if (reasons != null && reasons.length > 0) {
            String[] reason = reasons[random.nextInt(reasons.length)];
            encounter.setReasonCode(reason[0]);
            encounter.setReasonDescription(reason[1]);
        }

        String providerFirst = MedicalData.PROVIDER_FIRST_NAMES[random.nextInt(MedicalData.PROVIDER_FIRST_NAMES.length)];
        String providerLast = MedicalData.PROVIDER_LAST_NAMES[random.nextInt(MedicalData.PROVIDER_LAST_NAMES.length)];
        encounter.setProviderName("Dr. " + providerFirst + " " + providerLast);

        encounter.setCost(50 + random.nextInt(950));
        encounter.setDuration(15 + random.nextInt(46));

        return encounter;
    }

    private void generateLabResults(Patient patient) {
        for (Encounter encounter : patient.getEncounters()) {
            if (!"PRIMARY_CARE".equals(encounter.getType())) continue;

            int labCount = 5 + random.nextInt(6);
            Set<Integer> usedLabs = new HashSet<>();
            for (int i = 0; i < labCount && i < MedicalData.LAB_TESTS.length; i++) {
                int idx = -1;
                int attempts = 0;

                // Try to find a lab index that hasn't been used yet, up to 30 attempts
                while (attempts < 30) {
                    int candidate = random.nextInt(MedicalData.LAB_TESTS.length);
                    attempts++;
                    if (!usedLabs.contains(candidate)) {
                        idx = candidate;
                        break;
                    }
                }

                // If no new index was found within the attempt limit, stop generating labs
                if (idx == -1) {
                    break;
                }

                usedLabs.add(idx);

                MedicalData.LabTestData labTest = MedicalData.LAB_TESTS[idx];
                LabResult result = new LabResult();
                result.setId(UUID.randomUUID().toString());
                result.setLoincCode(labTest.loincCode());
                result.setDescription(labTest.description());
                result.setUnit(labTest.unit());
                result.setDate(encounter.getDate());
                result.setEncounterId(encounter.getId());
                result.setReferenceRangeLow(labTest.normalLow());
                result.setReferenceRangeHigh(labTest.normalHigh());

                // Generate realistic value - sometimes abnormal
                double range = labTest.normalHigh() - labTest.normalLow();
                double value;
                if (random.nextDouble() < 0.85) {
                    // Normal range
                    value = labTest.normalLow() + random.nextDouble() * range;
                } else {
                    // Abnormal
                    if (random.nextBoolean()) {
                        value = labTest.normalHigh() + random.nextDouble() * range * 0.3;
                    } else {
                        value = labTest.normalLow() - random.nextDouble() * range * 0.2;
                    }
                }
                value = Math.max(0, value);
                result.setValue(Math.round(value * 100.0) / 100.0);

                if (value < labTest.normalLow()) {
                    result.setInterpretation("low");
                } else if (value > labTest.normalHigh()) {
                    result.setInterpretation("high");
                } else {
                    result.setInterpretation("normal");
                }

                patient.getLabResults().add(result);
            }
        }
    }
}
