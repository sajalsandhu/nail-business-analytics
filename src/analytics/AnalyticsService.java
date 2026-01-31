package analytics;

import model.Appointment;
import model.Purchase;

import java.time.DayOfWeek;
import java.util.*;

public class AnalyticsService {

    public static double totalServiceValue(List<Appointment> appts) {
        return appts.stream().mapToDouble(Appointment::getPrice).sum();
    }

    public static double totalDeposits(List<Appointment> appts) {
        return appts.stream().mapToDouble(Appointment::getDepositOnline).sum();
    }

    public static double totalOutstandingBalance(List<Appointment> appts) {
        return appts.stream().mapToDouble(Appointment::balanceDue).sum();
    }

    public static double totalCompedValue(List<Appointment> appts) {
        return appts.stream().filter(Appointment::isComped)
                .mapToDouble(Appointment::getPrice).sum();
    }

    public static int compedCount(List<Appointment> appts) {
        return (int) appts.stream().filter(Appointment::isComped).count();
    }

    public static double estimatedCollectedNoTips(List<Appointment> appts) {
        return appts.stream().mapToDouble(Appointment::estimatedCollectedNoTips).sum();
    }

    public static Map<String, Integer> serviceCounts(List<Appointment> appts) {
        Map<String, Integer> map = new HashMap<>();
        for (Appointment a : appts)
            map.put(a.getServiceType(), map.getOrDefault(a.getServiceType(), 0) + 1);
        return map;
    }

    public static Map<String, Double> revenueByService(List<Appointment> appts) {
        Map<String, Double> map = new HashMap<>();
        for (Appointment a : appts)
            map.put(a.getServiceType(),
                    map.getOrDefault(a.getServiceType(), 0.0) + a.getPrice());
        return map;
    }

    public static Map<String, Integer> clientCounts(List<Appointment> appts) {
        Map<String, Integer> map = new HashMap<>();
        for (Appointment a : appts)
            map.put(a.getClient(), map.getOrDefault(a.getClient(), 0) + 1);
        return map;
    }

    public static Map<DayOfWeek, Integer> apptsByDay(List<Appointment> appts) {
        Map<DayOfWeek, Integer> map = new EnumMap<>(DayOfWeek.class);
        for (Appointment a : appts)
            map.put(a.getDate().getDayOfWeek(),
                    map.getOrDefault(a.getDate().getDayOfWeek(), 0) + 1);
        return map;
    }

    public static double totalSupplySpend(List<Purchase> purchases) {
        return purchases.stream().mapToDouble(Purchase::totalCost).sum();
    }

    public static Map<String, Double> spendByCategory(List<Purchase> purchases) {
        Map<String, Double> map = new HashMap<>();
        for (Purchase p : purchases)
            map.put(p.getCategory(),
                    map.getOrDefault(p.getCategory(), 0.0) + p.totalCost());
        return map;
    }

    public static Map<String, Double> spendByVendor(List<Purchase> purchases) {
        Map<String, Double> map = new HashMap<>();
        for (Purchase p : purchases)
            map.put(p.getVendor(),
                    map.getOrDefault(p.getVendor(), 0.0) + p.totalCost());
        return map;
    }

    public static <K> List<Map.Entry<K, Integer>> topNCounts(Map<K, Integer> map, int n) {
        return map.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(n).toList();
    }

    public static <K> List<Map.Entry<K, Double>> topNRevenue(Map<K, Double> map, int n) {
        return map.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(n).toList();
    }
    public static double estimatedCollectedWithTips(List<model.Appointment> appts) {
        double sum = 0;
        for (model.Appointment a : appts) sum += a.estimatedCollectedWithTips();
        return sum;
    }

    public static double totalTips(List<model.Appointment> appts) {
        double sum = 0;
        for (model.Appointment a : appts) sum += a.getTip();
        return sum;
    }

}
