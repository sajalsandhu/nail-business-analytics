package analytics;

import model.Appointment;
import model.Purchase;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public class SummaryPrinter {

    public static void printAppointmentsReport(List<Appointment> appts) {

        System.out.println("=== APPOINTMENT REPORT ===");
        System.out.println("Records loaded: " + appts.size());
        System.out.println();

        double serviceValue = AnalyticsService.totalServiceValue(appts);
        double deposits = AnalyticsService.totalDeposits(appts);
        double outstanding = AnalyticsService.totalOutstandingBalance(appts);
        double compedValue = AnalyticsService.totalCompedValue(appts);
        int compedCount = AnalyticsService.compedCount(appts);
        double estCollected = AnalyticsService.estimatedCollectedNoTips(appts);

        System.out.println("Service Value (List Price):     $" + String.format("%.2f", serviceValue));
        System.out.println("Deposits Collected (Online):    $" + String.format("%.2f", deposits));
        System.out.println("Outstanding Balance (Est.):     $" + String.format("%.2f", outstanding));
        System.out.println("Comped Appointments:            " + compedCount +
                " (Value: $" + String.format("%.2f", compedValue) + ")");
        System.out.println("Estimated Collected (no tips):  $" + String.format("%.2f", estCollected));
        System.out.println();

        System.out.println("Top Services:");
        AnalyticsService.topNCounts(AnalyticsService.serviceCounts(appts), 8)
                .forEach(e -> System.out.println("- " + e.getKey() + ": " + e.getValue()));

        System.out.println();
        System.out.println("Top Clients:");
        AnalyticsService.topNCounts(AnalyticsService.clientCounts(appts), 8)
                .forEach(e -> System.out.println("- " + e.getKey() + ": " + e.getValue()));

        System.out.println();
        System.out.println("Busiest Day:");
        Map<DayOfWeek, Integer> byDay = AnalyticsService.apptsByDay(appts);
        DayOfWeek best = null;
        int max = -1;
        for (var e : byDay.entrySet())
            if (e.getValue() > max) { max = e.getValue(); best = e.getKey(); }
        if (best != null)
            System.out.println("- " + best + " (" + max + " appts)");
    }

    public static void printPurchasesReport(List<Purchase> purchases, double estCollected) {

        System.out.println();
        System.out.println("=== PURCHASES / REINVESTMENT REPORT ===");

        double spend = AnalyticsService.totalSupplySpend(purchases);
        double net = estCollected - spend;

        System.out.println("Total Supply Spend:   $" + String.format("%.2f", spend));
        System.out.println("Net After Supplies:   $" + String.format("%.2f", net));

        double rate = estCollected == 0 ? 0 : spend / estCollected;
        System.out.println("Reinvestment Rate:    " + String.format("%.1f", rate * 100) + "%");

        System.out.println();
        System.out.println("Top Spend Categories:");
        AnalyticsService.topNRevenue(AnalyticsService.spendByCategory(purchases), 8)
                .forEach(e -> System.out.println("- " + e.getKey() + ": $" + String.format("%.2f", e.getValue())));

        System.out.println();
        System.out.println("Top Vendors:");
        AnalyticsService.topNRevenue(AnalyticsService.spendByVendor(purchases), 8)
                .forEach(e -> System.out.println("- " + e.getKey() + ": $" + String.format("%.2f", e.getValue())));
    }
    
    public static String buildFullReport(
            java.util.List<model.Appointment> appts,
            java.util.List<model.Purchase> purchases,
            double estCollectedNoTips
    ) {
        StringBuilder sb = new StringBuilder();

        // --- Appointment section ---
        sb.append("=== APPOINTMENT REPORT ===\n");
        sb.append("Records loaded: ").append(appts.size()).append("\n\n");

        sb.append(String.format("Service Value (List Price):     $%.2f\n",
                AnalyticsService.totalServiceValue(appts)));
        sb.append(String.format("Deposits Collected (Online):    $%.2f\n",
                AnalyticsService.totalDeposits(appts)));
        sb.append(String.format("Outstanding Balance (Est.):     $%.2f\n",
                AnalyticsService.totalOutstandingBalance(appts)));
        sb.append(String.format("Comped Value:                   $%.2f\n",
                AnalyticsService.totalCompedValue(appts)));

        sb.append(String.format("Estimated Collected (no tips):  $%.2f\n",
                estCollectedNoTips));

        // NEW: tips + with-tips totals
        double tips = AnalyticsService.totalTips(appts);
        double estCollectedWithTips = AnalyticsService.estimatedCollectedWithTips(appts);

        sb.append(String.format("Tips (Manual):                  $%.2f\n",
                tips));
        sb.append(String.format("Estimated Collected (w tips):   $%.2f\n\n",
                estCollectedWithTips));

        // --- Purchases section ---
        sb.append("=== PURCHASES / REINVESTMENT ===\n");
        double spend = AnalyticsService.totalSupplySpend(purchases);

        sb.append(String.format("Total Supply Spend:             $%.2f\n", spend));
        sb.append(String.format("Net After Supplies (w tips):    $%.2f\n",
                estCollectedWithTips - spend));

        double reinvestRate = (estCollectedWithTips == 0) ? 0 : (spend / estCollectedWithTips);
        sb.append(String.format("Reinvestment Rate:              %.1f%%\n",
                reinvestRate * 100));

        return sb.toString();
    }

}
