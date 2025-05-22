package gui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BillingWindow extends JFrame {
    private JTextField nameField, idField, phoneField, daysField, totalField;
    private final int ratePerDay = 1000;

    public BillingWindow() {
        setTitle("Billing");
        setSize(400, 350);
        setLayout(new GridLayout(7, 2, 10, 10));
        setLocationRelativeTo(null);

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

        add(new JLabel("Total Amount:"));
        totalField = new JTextField();
        totalField.setEditable(false);
        add(totalField);

        JButton calculateBtn = new JButton("Calculate");
        calculateBtn.addActionListener(e -> calculateTotal());
        add(calculateBtn);

        JButton saveBtn = new JButton("Save Billing");
        saveBtn.addActionListener(e -> saveBillingInfo());
        add(saveBtn);

        setVisible(true);
    }

    private void calculateTotal() {
        try {
            int days = Integer.parseInt(daysField.getText());
            int total = days * ratePerDay;
            totalField.setText(String.valueOf(total));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid number of days.");
        }
    }

    private void saveBillingInfo() {
        String name = nameField.getText();
        String id = idField.getText();
        String phone = phoneField.getText();
        String daysStr = daysField.getText();
        String totalStr = totalField.getText();

        if (name.isEmpty() || id.isEmpty() || phone.isEmpty() || daysStr.isEmpty() || totalStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and calculate total first.");
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO billing (customer_id, customer_name, phone, days_stayed, total_amount) VALUES (?, ?, ?, ?, ?)")) {

            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.setInt(4, Integer.parseInt(daysStr));
            ps.setDouble(5, Double.parseDouble(totalStr));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Billing info saved.");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving billing info.");
        }
    }

    private void clearFields() {
        nameField.setText("");
        idField.setText("");
        phoneField.setText("");
        daysField.setText("");
        totalField.setText("");
    }
}
