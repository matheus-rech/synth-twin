package com.synthtwin.model;

import java.time.LocalDate;

public class Vaccination {
    private String id;
    private String cvxCode;
    private String description;
    private LocalDate date;
    private int doseNumber;
    private String series;

    public Vaccination() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCvxCode() { return cvxCode; }
    public void setCvxCode(String cvxCode) { this.cvxCode = cvxCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public int getDoseNumber() { return doseNumber; }
    public void setDoseNumber(int doseNumber) { this.doseNumber = doseNumber; }
    public String getSeries() { return series; }
    public void setSeries(String series) { this.series = series; }
}
