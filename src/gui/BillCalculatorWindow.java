package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BillCalculatorWindow extends JFrame {
    public BillCalculatorWindow() {
        setTitle("Bill Calculator");
        setSize(400, 300);
        setLayout(new GridLayout(6, 2));

        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField daysField = new JTextField();
        JTextField priceField = new JTextField();
        JLabel totalLabel = new JLabel("Total: ₹0");

        add(new JLabel("Customer Name:")); add(nameField);
        add(new JLabel("Customer ID:")); add(idField);
        add(new JLabel("Phone Number:")); add(phoneField);
        add(new JLabel("Days Stay:")); add(daysField);
        add(new JLabel("Price/Day (₹):")); add(priceField);

        JButton calculate = new JButton("Calculate");
        calculate.addActionListener(e -> {
            try {
                int days = Integer.parseInt(daysField.getText());
                double price = Double.parseDouble(priceField.getText());
                totalLabel.setText("Total: ₹" + (days * price));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
            }
        });
        add(calculate);
        add(totalLabel);

        setVisible(true);
    }
}
