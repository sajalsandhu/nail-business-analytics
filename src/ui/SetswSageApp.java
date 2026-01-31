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
import java.util.List;

public class SetswSageApp extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    private final ReportPanel reportPanel;

    public SetswSageApp() {
        super("setswsage");

        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        reportPanel = new ReportPanel(this::showHome);

        HomePanel home = new HomePanel(
                this::openAddAppointment,
                this::openAddPurchase,
                this::showReport
        );

        root.add(home, "HOME");
        root.add(reportPanel, "REPORT");

        add(root);
        showHome();
    }

    private void showHome() {
        cards.show(root, "HOME");
    }

    private void showReport() {
        reportPanel.refresh();
        cards.show(root, "REPORT");
    }

    private void openAddAppointment() {
        AddAppointmentDialog dialog = new AddAppointmentDialog(this, "data/appointments_manual.csv");
        dialog.setVisible(true);   // <-- THIS is the missing part (shows the popup, blocks until closed)
        showReport();              // auto-refresh after it closes
    }

    private void openAddPurchase() {
        AddPurchaseDialog dialog = new AddPurchaseDialog(this, "data/purchases.csv");
        dialog.setVisible(true);   // <-- show popup
        showReport();              // auto-refresh after it closes
    }

}
