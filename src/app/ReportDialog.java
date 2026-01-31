package app;

import javax.swing.*;
import java.awt.*;

public class ReportDialog {

    public static void show(String title, String reportText) {
        JTextArea area = new JTextArea(reportText);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setPreferredSize(new Dimension(700, 500));

        JOptionPane.showMessageDialog(
                null,
                scrollPane,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
