package io;

import model.Purchase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchasesCsvReader {

    public static List<Purchase> read(String path) {
        List<Purchase> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String header = br.readLine(); // skip header
            if (header == null) return list;

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                List<String> p = parseCsvLine(line);
                if (p.size() < 9) continue;

                LocalDate date = LocalDate.parse(p.get(0).trim());
                String vendor = p.get(1).trim();
                String category = p.get(2).trim();
                String item = p.get(3).trim();

                int qty = parseInt(p.get(4));
                double unitCost = parseDouble(p.get(5));
                double shipping = parseDouble(p.get(6));
                double tax = parseDouble(p.get(7));
                String notes = p.get(8).trim();

                list.add(new Purchase(date, vendor, category, item, qty, unitCost, shipping, tax, notes));
            }

        } catch (Exception ex) {
            System.out.println("ERROR reading purchases CSV: " + ex.getMessage());
        }

        return list;
    }

    // same safe parser
    private static List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    sb.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                fields.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(ch);
            }
        }
        fields.add(sb.toString().trim());
        return fields;
    }

    private static double parseDouble(String s) {
        if (s == null) return 0.0;
        s = s.trim().replace("$", "");
        if (s.isEmpty()) return 0.0;
        try { return Double.parseDouble(s); } catch (Exception ex) { return 0.0; }
    }

    private static int parseInt(String s) {
        if (s == null) return 0;
        s = s.trim();
        if (s.isEmpty()) return 0;
        try { return Integer.parseInt(s); } catch (Exception ex) { return 0; }
    }
}
