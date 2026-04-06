package com.synthtwin.model;

import java.time.LocalDate;

public class Medication {
    private String id;
    private String code;
    private String description;
    private LocalDate startDate;
    private LocalDate stopDate;
    private String reasonCode;
    private String reasonDescription;
    private String dosageInstructions;

    public Medication() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getStopDate() { return stopDate; }
    public void setStopDate(LocalDate stopDate) { this.stopDate = stopDate; }
    public String getReasonCode() { return reasonCode; }
    public void setReasonCode(String reasonCode) { this.reasonCode = reasonCode; }
    public String getReasonDescription() { return reasonDescription; }
    public void setReasonDescription(String reasonDescription) { this.reasonDescription = reasonDescription; }
    public String getDosageInstructions() { return dosageInstructions; }
    public void setDosageInstructions(String dosageInstructions) { this.dosageInstructions = dosageInstructions; }
}
