package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomePanel extends JPanel {

	public HomePanel(Runnable onAddAppointment,
			Runnable onAddPurchase,
			Runnable onReviewReport) {

		setBackground(Color.BLACK);
		setLayout(new BorderLayout());

		// --- Title ---
		JLabel title = new JLabel("setswsage", SwingConstants.CENTER);
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Serif", Font.ITALIC, 72));
		title.setBorder(new EmptyBorder(80, 0, 60, 0));

		add(title, BorderLayout.NORTH);

		// --- Buttons ---
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));

		JButton addAppt = createButton("add\nappointment");
		JButton addPurchase = createButton("add\npurchase");
		JButton review = createButton("review\nreport");

		addAppt.addActionListener(e -> onAddAppointment.run());
		addPurchase.addActionListener(e -> onAddPurchase.run());
		review.addActionListener(e -> onReviewReport.run());

		buttonPanel.add(addAppt);
		buttonPanel.add(addPurchase);
		buttonPanel.add(review);

		add(buttonPanel, BorderLayout.CENTER);
	}

	private JButton createButton(String text) {
		JButton btn = new JButton(
				"<html><center>" + text.replace("\n", "<br>") + "</center></html>"
				);

		Color cream = new Color(245, 242, 235);

		btn.setPreferredSize(new Dimension(220, 80));
		btn.setBackground(cream);
		btn.setForeground(Color.BLACK);

		btn.setFont(new Font("Serif", Font.PLAIN, 18));

		// IMPORTANT: these lines force Swing to respect your colors
		btn.setOpaque(true);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setContentAreaFilled(true);

		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

		Color hover = new Color(235, 232, 225);

		btn.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        btn.setBackground(hover);
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        btn.setBackground(cream);
		    }
		});

		return btn;
		
	}

}
