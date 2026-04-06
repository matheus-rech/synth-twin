package com.synthtwin.model;

import java.time.LocalDate;

public class LabResult {
    private String id;
    private String loincCode;
    private String description;
    private double value;
    private String unit;
    private LocalDate date;
    private double referenceRangeLow;
    private double referenceRangeHigh;
    private String interpretation;
    private String encounterId;

    public LabResult() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getLoincCode() { return loincCode; }
    public void setLoincCode(String loincCode) { this.loincCode = loincCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public double getReferenceRangeLow() { return referenceRangeLow; }
    public void setReferenceRangeLow(double referenceRangeLow) { this.referenceRangeLow = referenceRangeLow; }
    public double getReferenceRangeHigh() { return referenceRangeHigh; }
    public void setReferenceRangeHigh(double referenceRangeHigh) { this.referenceRangeHigh = referenceRangeHigh; }
    public String getInterpretation() { return interpretation; }
    public void setInterpretation(String interpretation) { this.interpretation = interpretation; }
    public String getEncounterId() { return encounterId; }
    public void setEncounterId(String encounterId) { this.encounterId = encounterId; }
}
