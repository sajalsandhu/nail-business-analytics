package model;

import java.time.LocalDate;

public class Purchase {

    private final LocalDate date;
    private final String vendor;
    private final String category;
    private final String item;
    private final int qty;
    private final double unitCost;
    private final double shipping;
    private final double tax;
    private final String notes;

    public Purchase(LocalDate date, String vendor, String category, String item,
                    int qty, double unitCost, double shipping, double tax, String notes) {

        this.date = date;
        this.vendor = vendor;
        this.category = category;
        this.item = item;
        this.qty = qty;
        this.unitCost = unitCost;
        this.shipping = shipping;
        this.tax = tax;
        this.notes = notes == null ? "" : notes;
    }

    public LocalDate getDate() { return date; }
    public String getVendor() { return vendor; }
    public String getCategory() { return category; }
    public String getItem() { return item; }
    public int getQty() { return qty; }
    public double getUnitCost() { return unitCost; }
    public double getShipping() { return shipping; }
    public double getTax() { return tax; }
    public String getNotes() { return notes; }

    public double totalCost() {
        return (qty * unitCost) + shipping + tax;
    }
}
