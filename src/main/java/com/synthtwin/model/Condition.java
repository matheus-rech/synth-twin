package com.synthtwin.model;

import java.time.LocalDate;

public class Condition {
    private String id;
    private String code;
    private String description;
    private String category;
    private LocalDate onsetDate;
    private LocalDate abatementDate;
    private String clinicalStatus;

    public Condition() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDate getOnsetDate() { return onsetDate; }
    public void setOnsetDate(LocalDate onsetDate) { this.onsetDate = onsetDate; }
    public LocalDate getAbatementDate() { return abatementDate; }
    public void setAbatementDate(LocalDate abatementDate) { this.abatementDate = abatementDate; }
    public String getClinicalStatus() { return clinicalStatus; }
    public void setClinicalStatus(String clinicalStatus) { this.clinicalStatus = clinicalStatus; }
}
