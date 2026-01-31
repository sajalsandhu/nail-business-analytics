package io;

import model.Appointment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ManualAppointmentsReader {

    public static List<Appointment> read(String path) {
        List<Appointment> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String header = br.readLine(); // skip header
            if (header == null) return list;

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                List<String> p = parseCsvLine(line);
                if (p.size() < 7) continue;

                String dateStr = p.get(0).trim();
                String client = p.get(1).trim();
                String service = p.get(2).trim();

                double price = parseDouble(p.get(3));
                double deposit = parseDouble(p.get(4));
                double tip = parseDouble(p.get(5));
                String notes = p.get(6).trim();

                LocalDate date = LocalDate.parse(dateStr);

                // Manual entries don’t have a real start/end time,
                // so we set them to noon for a stable LocalDateTime.
                LocalDateTime start = date.atTime(12, 0);
                LocalDateTime end = start;

                boolean comped = false;        // you can expand this later if you want
                String reason = "";

                list.add(new Appointment(
                        date, start, end,
                        client, service,
                        price, deposit, tip, 0,
                        comped, reason, notes
                ));
            }

        } catch (Exception ex) {
            System.out.println("ERROR reading manual appointments CSV: " + ex.getMessage());
        }

        return list;
    }

    // quote-safe CSV parsing (same style as your other reader)
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
        try { return Double.parseDouble(s); }
        catch (Exception ex) { return 0.0; }
    }
}
