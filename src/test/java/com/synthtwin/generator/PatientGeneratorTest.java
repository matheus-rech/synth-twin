package com.synthtwin.generator;

import com.synthtwin.cli.Options;
import com.synthtwin.model.Patient;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientGeneratorTest {

    private static final LocalDate FIXED_DATE = LocalDate.of(2025, 1, 1);

    @Test
    void testGeneratePatients_defaultOptions() {
        Options options = new Options();
        options.setPopulation(10);
        options.setSeed(42L);

        PatientGenerator generator = new PatientGenerator(42L, FIXED_DATE);
        List<Patient> patients = generator.generatePatients(options);

        assertEquals(10, patients.size());
        for (Patient p : patients) {
            assertNotNull(p.getId());
            assertNotNull(p.getFirstName());
            assertNotNull(p.getLastName());
            assertNotNull(p.getBirthDate());
            assertNotNull(p.getGender());
        }
    }

    @Test
    void testGeneratePatients_withGenderFilter() {
        Options options = new Options();
        options.setPopulation(10);
        options.setGender("M");
        options.setSeed(123L);

        PatientGenerator generator = new PatientGenerator(123L, FIXED_DATE);
        List<Patient> patients = generator.generatePatients(options);

        assertEquals(10, patients.size());
        for (Patient p : patients) {
            assertEquals("M", p.getGender());
        }
    }

    @Test
    void testGeneratePatients_withStateFilter() {
        Options options = new Options();
        options.setPopulation(10);
        options.setState("CA");
        options.setSeed(456L);

        PatientGenerator generator = new PatientGenerator(456L, FIXED_DATE);
        List<Patient> patients = generator.generatePatients(options);

        assertEquals(10, patients.size());
        for (Patient p : patients) {
            assertEquals("CA", p.getAddress().getState());
        }
    }

    @Test
    void testGeneratePatients_withAgeFilter() {
        Options options = new Options();
        options.setPopulation(20);
        options.setMinAge(20);
        options.setMaxAge(50);
        options.setSeed(789L);

        PatientGenerator generator = new PatientGenerator(789L, FIXED_DATE);
        List<Patient> patients = generator.generatePatients(options);

        assertEquals(20, patients.size());
        for (Patient p : patients) {
            int age = Period.between(p.getBirthDate(), FIXED_DATE).getYears();
            assertTrue(age >= 20 && age <= 50,
                "Expected age between 20 and 50, got " + age + " for patient born " + p.getBirthDate());
        }
    }

    @Test
    void testReproducibility() {
        Options options = new Options();
        options.setPopulation(5);
        options.setSeed(999L);

        PatientGenerator g1 = new PatientGenerator(999L, FIXED_DATE);
        List<Patient> patients1 = g1.generatePatients(options);

        PatientGenerator g2 = new PatientGenerator(999L, FIXED_DATE);
        List<Patient> patients2 = g2.generatePatients(options);

        assertEquals(patients1.size(), patients2.size());
        for (int i = 0; i < patients1.size(); i++) {
            assertEquals(patients1.get(i).getId(), patients2.get(i).getId());
            assertEquals(patients1.get(i).getFirstName(), patients2.get(i).getFirstName());
            assertEquals(patients1.get(i).getLastName(), patients2.get(i).getLastName());
            assertEquals(patients1.get(i).getBirthDate(), patients2.get(i).getBirthDate());
        }
    }

    @Test
    void testPatientHasLabResults() {
        Options options = new Options();
        options.setPopulation(5);
        options.setSeed(111L);

        PatientGenerator generator = new PatientGenerator(111L, FIXED_DATE);
        List<Patient> patients = generator.generatePatients(options);

        long patientsWithLabs = patients.stream()
            .filter(p -> !p.getLabResults().isEmpty())
            .count();
        assertTrue(patientsWithLabs > 0, "At least some patients should have lab results");
    }

    @Test
    void testPatientHasVaccinations() {
        Options options = new Options();
        options.setPopulation(5);
        options.setSeed(222L);

        PatientGenerator generator = new PatientGenerator(222L, FIXED_DATE);
        List<Patient> patients = generator.generatePatients(options);

        for (Patient p : patients) {
            assertFalse(p.getVaccinations().isEmpty(),
                "Patient " + p.getId() + " should have at least one vaccination");
        }
    }

    @Test
    void testAgeRangeSwappedWhenInverted() {
        Options options = new Options();
        options.setPopulation(10);
        options.setMinAge(50);
        options.setMaxAge(20);
        options.setSeed(333L);

        PatientGenerator generator = new PatientGenerator(333L, FIXED_DATE);
        List<Patient> patients = generator.generatePatients(options);

        assertEquals(10, patients.size());
        for (Patient p : patients) {
            int age = Period.between(p.getBirthDate(), FIXED_DATE).getYears();
            assertTrue(age >= 20 && age <= 50,
                "Expected age between 20 and 50, got " + age + " for patient born " + p.getBirthDate());
        }
    }
}
