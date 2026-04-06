package com.synthtwin.model;

import java.time.LocalDate;

public class Encounter {
    private String id;
    private String type;
    private LocalDate date;
    private String reasonCode;
    private String reasonDescription;
    private String providerName;
    private double cost;
    private int duration;

    public Encounter() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getReasonCode() { return reasonCode; }
    public void setReasonCode(String reasonCode) { this.reasonCode = reasonCode; }
    public String getReasonDescription() { return reasonDescription; }
    public void setReasonDescription(String reasonDescription) { this.reasonDescription = reasonDescription; }
    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
}
