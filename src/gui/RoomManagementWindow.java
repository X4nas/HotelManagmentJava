package gui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RoomManagementWindow extends JFrame {
    private DefaultTableModel model;
    private JTable table;

    public RoomManagementWindow() {
        setTitle("Manage Rooms");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[]{"Room No", "Type", "Bed", "Price", "Status"}, 0);
        table = new JTable(model);
        loadRoomData();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton editBtn = new JButton("Edit Room");
        JButton deleteBtn = new JButton("Delete Room");

        editBtn.addActionListener(e -> editRoom());
        deleteBtn.addActionListener(e -> deleteRoom());

        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadRoomData() {
        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rooms")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getString("bed"),
                        rs.getDouble("price"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void editRoom() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String roomNo = (String) model.getValueAt(row, 0);
        String newType = JOptionPane.showInputDialog("Enter new type:", model.getValueAt(row, 1));
        String newBed = JOptionPane.showInputDialog("Enter new bed (single/double):", model.getValueAt(row, 2));
        double newPrice = Double.parseDouble(JOptionPane.showInputDialog("Enter new price:", model.getValueAt(row, 3)));
        String newStatus = JOptionPane.showInputDialog("Enter new status (booked/unbooked):", model.getValueAt(row, 4));

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement("UPDATE rooms SET type=?, bed=?, price=?, status=? WHERE room_number=?")) {

            pst.setString(1, newType);
            pst.setString(2, newBed);
            pst.setDouble(3, newPrice);
            pst.setString(4, newStatus);
            pst.setString(5, roomNo);
            pst.executeUpdate();

            loadRoomData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteRoom() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String roomNo = (String) model.getValueAt(row, 0);
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement("DELETE FROM rooms WHERE room_number = ?")) {
            pst.setString(1, roomNo);
            pst.executeUpdate();

            loadRoomData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
