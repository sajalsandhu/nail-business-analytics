package ui;

import io.CsvWriter;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddAppointmentDialog extends JDialog {

    public AddAppointmentDialog(JFrame parent, String manualApptPath) {
        super(parent, "Add Appointment", true);

        setSize(450, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField dateField = new JTextField(LocalDate.now().toString()); // YYYY-MM-DD
        JTextField clientField = new JTextField();
        JTextField serviceField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField depositField = new JTextField("0");
        JTextField tipField = new JTextField("0");
        JTextField notesField = new JTextField();

        form.add(new JLabel("Date (YYYY-MM-DD):"));
        form.add(dateField);

        form.add(new JLabel("Client Name:"));
        form.add(clientField);

        form.add(new JLabel("Service:"));
        form.add(serviceField);

        form.add(new JLabel("Price:"));
        form.add(priceField);

        form.add(new JLabel("Deposit:"));
        form.add(depositField);

        form.add(new JLabel("Tip:"));
        form.add(tipField);

        form.add(new JLabel("Notes:"));
        form.add(notesField);

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");

        JPanel buttons = new JPanel();
        buttons.add(save);
        buttons.add(cancel);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        cancel.addActionListener(e -> dispose());

        save.addActionListener(e -> {
            try {
                String date = dateField.getText().trim();
                String client = clientField.getText().trim();
                String service = serviceField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                double deposit = Double.parseDouble(depositField.getText().trim());
                double tip = Double.parseDouble(tipField.getText().trim());
                String notes = notesField.getText().trim();

                if (client.isEmpty() || service.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Client and Service are required.");
                    return;
                }

                // Manual appointments CSV format:
                // date,client,service,price,deposit,tip,notes
                String line = String.format("%s,%s,%s,%.2f,%.2f,%.2f,%s",
                        escape(date), escape(client), escape(service),
                        price, deposit, tip, escape(notes));

                // If file is empty, you should add a header manually once:
                // date,client,service,price,deposit,tip,notes
                CsvWriter.appendLine(manualApptPath, line);

                JOptionPane.showMessageDialog(this, "Appointment saved!");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            }
        });
    }

    private static String escape(String s) {
        if (s == null) return "";
        // simple escaping: wrap in quotes if contains comma or quotes
        if (s.contains(",") || s.contains("\"")) {
            s = s.replace("\"", "\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }
}
