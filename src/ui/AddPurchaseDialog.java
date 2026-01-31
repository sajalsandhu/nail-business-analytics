package ui;

import io.CsvWriter;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddPurchaseDialog extends JDialog {

    public AddPurchaseDialog(JFrame parent, String purchasesPath) {
        super(parent, "Add Purchase", true);

        setSize(500, 360);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField dateField = new JTextField(LocalDate.now().toString());
        JTextField vendorField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField itemField = new JTextField();
        JTextField qtyField = new JTextField("1");
        JTextField unitCostField = new JTextField();
        JTextField shippingField = new JTextField("0");
        JTextField taxField = new JTextField("0");
        JTextField notesField = new JTextField();

        form.add(new JLabel("Date (YYYY-MM-DD):")); form.add(dateField);
        form.add(new JLabel("Vendor:")); form.add(vendorField);
        form.add(new JLabel("Category:")); form.add(categoryField);
        form.add(new JLabel("Item:")); form.add(itemField);
        form.add(new JLabel("Qty:")); form.add(qtyField);
        form.add(new JLabel("Unit Cost:")); form.add(unitCostField);
        form.add(new JLabel("Shipping:")); form.add(shippingField);
        form.add(new JLabel("Tax:")); form.add(taxField);
        form.add(new JLabel("Notes:")); form.add(notesField);

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
                String vendor = vendorField.getText().trim();
                String category = categoryField.getText().trim();
                String item = itemField.getText().trim();

                int qty = Integer.parseInt(qtyField.getText().trim());
                double unitCost = Double.parseDouble(unitCostField.getText().trim());
                double shipping = Double.parseDouble(shippingField.getText().trim());
                double tax = Double.parseDouble(taxField.getText().trim());
                String notes = notesField.getText().trim();

                if (vendor.isEmpty() || category.isEmpty() || item.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vendor, Category, and Item are required.");
                    return;
                }

                // purchases.csv format:
                // date,vendor,category,item,qty,unit_cost,shipping,tax,notes
                String line = String.format("%s,%s,%s,%s,%d,%.2f,%.2f,%.2f,%s",
                        escape(date), escape(vendor), escape(category), escape(item),
                        qty, unitCost, shipping, tax, escape(notes));

                CsvWriter.appendLine(purchasesPath, line);

                JOptionPane.showMessageDialog(this, "Purchase saved!");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            }
        });
    }

    private static String escape(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"")) {
            s = s.replace("\"", "\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }
}
