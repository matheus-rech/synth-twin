package com.synthtwin.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Patient {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String gender;
    private Address address;
    private String race;
    private String ethnicity;
    private List<Encounter> encounters = new ArrayList<>();
    private List<Condition> conditions = new ArrayList<>();
    private List<Medication> medications = new ArrayList<>();
    private List<Allergy> allergies = new ArrayList<>();
    private List<Vaccination> vaccinations = new ArrayList<>();
    private List<LabResult> labResults = new ArrayList<>();

    public Patient() {}

    public int getAge() {
        LocalDate endDate = deathDate != null ? deathDate : LocalDate.now();
        return Period.between(birthDate, endDate).getYears();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public LocalDate getDeathDate() { return deathDate; }
    public void setDeathDate(LocalDate deathDate) { this.deathDate = deathDate; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public String getRace() { return race; }
    public void setRace(String race) { this.race = race; }
    public String getEthnicity() { return ethnicity; }
    public void setEthnicity(String ethnicity) { this.ethnicity = ethnicity; }
    public List<Encounter> getEncounters() { return encounters; }
    public void setEncounters(List<Encounter> encounters) { this.encounters = encounters; }
    public List<Condition> getConditions() { return conditions; }
    public void setConditions(List<Condition> conditions) { this.conditions = conditions; }
    public List<Medication> getMedications() { return medications; }
    public void setMedications(List<Medication> medications) { this.medications = medications; }
    public List<Allergy> getAllergies() { return allergies; }
    public void setAllergies(List<Allergy> allergies) { this.allergies = allergies; }
    public List<Vaccination> getVaccinations() { return vaccinations; }
    public void setVaccinations(List<Vaccination> vaccinations) { this.vaccinations = vaccinations; }
    public List<LabResult> getLabResults() { return labResults; }
    public void setLabResults(List<LabResult> labResults) { this.labResults = labResults; }
}
