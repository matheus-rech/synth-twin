package com.synthtwin.generator;

import com.synthtwin.cli.Options;
import com.synthtwin.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientGeneratorTest {

    @Test
    void testGeneratePatients_defaultOptions() {
        Options options = new Options();
        options.setPopulation(10);
        options.setSeed(42L);

        PatientGenerator generator = new PatientGenerator(42L);
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

        PatientGenerator generator = new PatientGenerator(123L);
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

        PatientGenerator generator = new PatientGenerator(456L);
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

        PatientGenerator generator = new PatientGenerator(789L);
        List<Patient> patients = generator.generatePatients(options);

        assertEquals(20, patients.size());
        for (Patient p : patients) {
            int age = p.getAge();
            assertTrue(age >= 20 && age <= 50,
                "Expected age between 20 and 50, got " + age + " for patient born " + p.getBirthDate());
        }
    }

    @Test
    void testReproducibility() {
        Options options = new Options();
        options.setPopulation(5);
        options.setSeed(999L);

        PatientGenerator g1 = new PatientGenerator(999L);
        List<Patient> patients1 = g1.generatePatients(options);

        PatientGenerator g2 = new PatientGenerator(999L);
        List<Patient> patients2 = g2.generatePatients(options);

        assertEquals(patients1.size(), patients2.size());
        assertEquals(patients1.get(0).getFirstName(), patients2.get(0).getFirstName());
        assertEquals(patients1.get(0).getLastName(), patients2.get(0).getLastName());
    }

    @Test
    void testPatientHasLabResults() {
        Options options = new Options();
        options.setPopulation(5);
        options.setSeed(111L);

        PatientGenerator generator = new PatientGenerator(111L);
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

        PatientGenerator generator = new PatientGenerator(222L);
        List<Patient> patients = generator.generatePatients(options);

        for (Patient p : patients) {
            assertFalse(p.getVaccinations().isEmpty(),
                "Patient " + p.getId() + " should have at least one vaccination");
        }
    }
}
