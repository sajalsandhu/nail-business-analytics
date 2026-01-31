package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Appointment {

    private final LocalDate date;
    private final LocalDateTime start;
    private final LocalDateTime end;

    private final String client;
    private final String serviceType;

    private final double price;
    private final double depositOnline;
    private final double tip;          // NEW
    private final int durationMin;

    private final boolean comped;
    private final String compReason;
    private final String notes;

    public Appointment(LocalDate date, LocalDateTime start, LocalDateTime end,
                       String client, String serviceType,
                       double price, double depositOnline, double tip, int durationMin,
                       boolean comped, String compReason, String notes) {

        this.date = date;
        this.start = start;
        this.end = end;
        this.client = client;
        this.serviceType = serviceType;
        this.price = price;
        this.depositOnline = depositOnline;
        this.tip = tip;
        this.durationMin = durationMin;
        this.comped = comped;
        this.compReason = compReason == null ? "" : compReason;
        this.notes = notes == null ? "" : notes;
    }

    public LocalDate getDate() { return date; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }

    public String getClient() { return client; }
    public String getServiceType() { return serviceType; }

    public double getPrice() { return price; }
    public double getDepositOnline() { return depositOnline; }
    public double getTip() { return tip; }                 // NEW
    public int getDurationMin() { return durationMin; }

    public boolean isComped() { return comped; }
    public String getCompReason() { return compReason; }
    public String getNotes() { return notes; }

    public double balanceDue() {
        if (comped) return 0.0;
        double due = price - depositOnline;
        return due < 0 ? 0.0 : due;
    }

    public double estimatedCollectedNoTips() {
        if (comped) return 0.0;
        return depositOnline + balanceDue();
    }

    public double estimatedCollectedWithTips() {            // NEW
        if (comped) return 0.0;
        return estimatedCollectedNoTips() + tip;
    }
}
