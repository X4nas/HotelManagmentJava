package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BillingWindow extends JFrame {
    public BillingWindow() {
        setTitle("Billing Calculator");
        setSize(400, 300);
        setLayout(new GridLayout(7, 2));

        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField daysField = new JTextField();
        JTextField pricePerDayField = new JTextField();
        JTextField totalAmountField = new JTextField();
        totalAmountField.setEditable(false);

        JButton calculateBtn = new JButton("Calculate");
        calculateBtn.addActionListener(e -> {
            try {
                int days = Integer.parseInt(daysField.getText());
                double pricePerDay = Double.parseDouble(pricePerDayField.getText());
                double total = days * pricePerDay;
                totalAmountField.setText(String.valueOf(total));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(new JLabel("Customer Name:")); add(nameField);
        add(new JLabel("Customer ID:")); add(idField);
        add(new JLabel("Phone Number:")); add(phoneField);
        add(new JLabel("Days Stay:")); add(daysField);
        add(new JLabel("Price/Day:")); add(pricePerDayField);
        add(new JLabel("Total Amount:")); add(totalAmountField);
        add(new JLabel("")); add(calculateBtn);

        setVisible(true);
    }
}