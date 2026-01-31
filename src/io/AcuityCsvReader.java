package io;

import model.Appointment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AcuityCsvReader {

    // Example from Acuity: "May 16, 2025 12:30 pm"
    private static final DateTimeFormatter ACUITY_FMT =
            DateTimeFormatter.ofPattern("MMMM d, yyyy h:mm a", Locale.ENGLISH);

    public static List<Appointment> read(String path) {
        List<Appointment> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String headerLine = br.readLine();
            if (headerLine == null) return list;

            List<String> headers = parseCsvLine(headerLine);
            Map<String, Integer> idx = new HashMap<>();
            for (int i = 0; i < headers.size(); i++) {
                idx.put(headers.get(i).trim(), i);
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                List<String> row = parseCsvLine(line);

                String startRaw = get(row, idx, "Start Time");
                String endRaw   = get(row, idx, "End Time");

                // Skip malformed rows safely
                if (startRaw.isEmpty() || endRaw.isEmpty()) continue;

                // Clean + normalize for parsing
                startRaw = normalizeAmPm(cleanSpaces(startRaw));
                endRaw   = normalizeAmPm(cleanSpaces(endRaw));

                LocalDateTime start = LocalDateTime.parse(startRaw, ACUITY_FMT);
                LocalDateTime end   = LocalDateTime.parse(endRaw, ACUITY_FMT);

                String first = get(row, idx, "First Name");
                String last  = get(row, idx, "Last Name");
                String client = (first + " " + last).trim();

                String serviceType = get(row, idx, "Type");

                double price = parseDouble(get(row, idx, "Appointment Price"));
                double deposit = parseDouble(get(row, idx, "Amount Paid Online"));

                String notes = get(row, idx, "Notes");

                boolean comped = isComped(client, notes);
                String reason  = compReason(client, notes);

                int durationMin = (int) Duration.between(start, end).toMinutes();

                list.add(new Appointment(
                        start.toLocalDate(), start, end,
                        client, serviceType,
                        price, deposit, 0.0, durationMin,   // <-- tip = 0.0
                        comped, reason, notes
                ));
            } 

        } catch (Exception ex) {
            System.out.println("ERROR reading Acuity CSV: " + ex.getMessage());
        }

        return list;
    }

    // --- Fixes "pm" vs "PM" parsing issues ---
    private static String normalizeAmPm(String s) {
        if (s == null) return "";
        // Normalize common cases for AM/PM
        return s.trim()
                .replace(" am", " AM")
                .replace(" pm", " PM")
                .replace("Am", "AM")
                .replace("Pm", "PM");
    }

    // --- Fixes weird invisible spaces (non-breaking space) ---
    private static String cleanSpaces(String s) {
        if (s == null) return "";
        return s.replace('\u00A0', ' ').trim();
    }

    // ---- comp rules ----
    private static boolean isComped(String client, String notes) {
        String c = norm(client);
        if (c.equals("amna sohail")) return true;

        String n = norm(notes);
        return n.contains("service for service") ||
                n.contains("s4s") ||
                n.contains("trade") ||
                n.contains("barter") ||
                n.contains("swap") ||
                n.contains("collab") ||
                n.contains("free") ||
                n.contains("gift") ||
                n.contains("birthday");
    }

    private static String compReason(String client, String notes) {
        String c = norm(client);
        if (c.equals("amna sohail")) return "SERVICE_TRADE";

        String n = norm(notes);
        if (n.contains("birthday")) return "BIRTHDAY_GIFT";
        if (n.contains("gift")) return "GIFT";
        if (n.contains("service for service") || n.contains("s4s") || n.contains("trade")
                || n.contains("barter") || n.contains("swap")) return "SERVICE_TRADE";
        if (n.contains("collab")) return "COLLAB";
        if (n.contains("free")) return "FREE";
        return "";
    }

    private static String norm(String s) {
        return (s == null) ? "" : s.trim().toLowerCase();
    }

    // ---- SAFE CSV parsing (handles commas inside quotes) ----
    private static List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        if (line == null) return fields;

        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                // handle escaped quote ("")
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

    private static String get(List<String> row, Map<String, Integer> idx, String col) {
        Integer i = idx.get(col);
        if (i == null || i < 0 || i >= row.size()) return "";
        return row.get(i).trim();
    }

    private static double parseDouble(String s) {
        if (s == null) return 0.0;
        s = s.trim().replace("$", "");
        if (s.isEmpty()) return 0.0;
        try { return Double.parseDouble(s); }
        catch (Exception ex) { return 0.0; }
    }
}
