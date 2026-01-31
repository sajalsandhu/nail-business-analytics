package ui;

import analytics.AnalyticsService;
import analytics.SummaryPrinter;
import io.AcuityCsvReader;
import io.ManualAppointmentsReader;
import io.PurchasesCsvReader;
import model.Appointment;
import model.Purchase;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ReportPanel extends JPanel {

    private final JTextArea reportArea = new JTextArea();
    private final Runnable onBack;

    public ReportPanel(Runnable onBack) {
        this.onBack = onBack;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JButton back = new JButton("← back");
        back.addActionListener(e -> onBack.run());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(back);

        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
    }

    public void refresh() {
        List<Appointment> appts = new ArrayList<>();
        appts.addAll(AcuityCsvReader.read("data/schedule2026-01-25.csv"));
        appts.addAll(ManualAppointmentsReader.read("data/appointments_manual.csv"));

        List<Purchase> purchases =
                PurchasesCsvReader.read("data/purchases.csv");

        double noTips = AnalyticsService.estimatedCollectedNoTips(appts);

        reportArea.setText(
                SummaryPrinter.buildFullReport(appts, purchases, noTips)
        );
        reportArea.setCaretPosition(0);
    }
}
