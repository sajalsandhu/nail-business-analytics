package ui;

import analytics.AnalyticsService;
import analytics.SummaryPrinter;
import io.AcuityCsvReader;
import io.PurchasesCsvReader;
import model.Appointment;
import model.Purchase;
import io.ManualAppointmentsReader;


import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardFrame extends JFrame {

    private final JTextArea reportArea;

    private final String acuityPath = "data/schedule2026-01-25.csv";
    private final String manualApptPath = "data/appointments_manual.csv";
    private final String purchasesPath = "data/purchases.csv";

    public DashboardFrame() {
        super("Nail Business Analytics");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Left panel buttons
        JPanel left = new JPanel();
        left.setLayout(new GridLayout(0, 1, 10, 10));
        left.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton addApptBtn = new JButton("Add Appointment");
        JButton addPurchaseBtn = new JButton("Add Purchase");
        JButton refreshBtn = new JButton("Refresh Report");

        left.add(addApptBtn);
        left.add(addPurchaseBtn);
        left.add(refreshBtn);

        // Report area
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scroll = new JScrollPane(reportArea);

        // Layout
        setLayout(new BorderLayout());
        add(left, BorderLayout.WEST);
        add(scroll, BorderLayout.CENTER);

        // Actions
        addApptBtn.addActionListener(e -> {
            AddAppointmentDialog dialog = new AddAppointmentDialog(this, manualApptPath);
            dialog.setVisible(true);
            refreshReport();
        });

        addPurchaseBtn.addActionListener(e -> {
            AddPurchaseDialog dialog = new AddPurchaseDialog(this, purchasesPath);
            dialog.setVisible(true);
            refreshReport();
        });

        refreshBtn.addActionListener(e -> refreshReport());

        // initial load
        refreshReport();
    }

    private void refreshReport() {
    	List<Appointment> acuityAppts = AcuityCsvReader.read(acuityPath);
    	List<Appointment> manualAppts = ManualAppointmentsReader.read(manualApptPath);

    	// merge
    	acuityAppts.addAll(manualAppts);

    	List<Purchase> purchases = PurchasesCsvReader.read(purchasesPath);

    	double estCollectedNoTips = AnalyticsService.estimatedCollectedNoTips(acuityAppts);

    	String report = SummaryPrinter.buildFullReport(acuityAppts, purchases, estCollectedNoTips);
    	reportArea.setText(report);
    	reportArea.setCaretPosition(0);
    }
}
