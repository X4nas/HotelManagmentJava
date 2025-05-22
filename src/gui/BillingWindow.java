package gui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BillingWindow extends JFrame {
    private JTextField nameField, idField, phoneField, daysField;
    private JLabel totalLabel;

    public BillingWindow() {
        setTitle("Billing");
        setSize(400, 300);
        setLayout(new GridLayout(6, 2));

        add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Customer ID:"));
        idField = new JTextField();
        add(idField);

        add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        add(phoneField);

        add(new JLabel("Days Stayed:"));
        daysField = new JTextField();
        add(daysField);

        JButton calculateBtn = new JButton("Calculate & Save");
        add(calculateBtn);
        totalLabel = new JLabel("Total: ₹0.00");
        add(totalLabel);

        calculateBtn.addActionListener(e -> saveBilling());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void saveBilling() {
        String name = nameField.getText();
        String id = idField.getText();
        String phone = phoneField.getText();
        int days = Integer.parseInt(daysField.getText());
        double ratePerDay = 1000.0; // default rate
        double total = days * ratePerDay;
        totalLabel.setText("Total: ₹" + total);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "INSERT INTO billing (customer_name, customer_id, phone, days, total_amount) VALUES (?, ?, ?, ?, ?)")) {

            pst.setString(1, name);
            pst.setString(2, id);
            pst.setString(3, phone);
            pst.setInt(4, days);
            pst.setDouble(5, total);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Billing info saved successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving billing info.");
        }
    }
}
