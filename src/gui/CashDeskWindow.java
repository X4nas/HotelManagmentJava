package gui;

import db.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CashDeskWindow extends JFrame {
    public CashDeskWindow() {
        setTitle("Cash Desk");
        setSize(700, 400);
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.setColumnIdentifiers(new String[]{"ID", "Type", "Amount", "Currency"});

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM cashdesk")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("currency")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JPanel controlPanel = new JPanel();
        JTextField typeField = new JTextField(10);
        JTextField amountField = new JTextField(5);
        JComboBox<String> currencyBox = new JComboBox<>(new String[]{"USD", "INR", "EUR"});

        JButton addBtn = new JButton("Add Payment");
        addBtn.addActionListener(e -> {
            String type = typeField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String currency = (String) currencyBox.getSelectedItem();

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement("INSERT INTO cashdesk (type, amount, currency) VALUES (?, ?, ?)")) {

                ps.setString(1, type);
                ps.setDouble(2, amount);
                ps.setString(3, currency);
                ps.executeUpdate();
                model.addRow(new Object[]{model.getRowCount() + 1, type, amount, currency});

                typeField.setText("");
                amountField.setText("");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        controlPanel.add(new JLabel("Type:"));
        controlPanel.add(typeField);
        controlPanel.add(new JLabel("Amount:"));
        controlPanel.add(amountField);
        controlPanel.add(new JLabel("Currency:"));
        controlPanel.add(currencyBox);
        controlPanel.add(addBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}
