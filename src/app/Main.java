package app;

import ui.SetswSageApp;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SetswSageApp().setVisible(true);
        });
    }
}
