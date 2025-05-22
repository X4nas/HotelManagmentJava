package gui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RoomManagementWindow extends JFrame {
    private DefaultTableModel model;
    private JTable table;

    public RoomManagementWindow() {
        setTitle("Manage Rooms");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add columns: Room No, Type, Bed Type, Price, Status, Guest Name, Contact, Days Left
        model = new DefaultTableModel(new String[]{
                "Room No", "Type", "Bed Type", "Price", "Status", "Guest Name", "Contact", "Days Left"
        }, 0);

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
        String sql = "SELECT r.room_number, r.type, r.bed_type, r.price, r.status, " +
                "g.name, g.contact, r.booking_end_date, " +
                "DATEDIFF(r.booking_end_date, CURDATE()) AS days_left " +
                "FROM rooms r LEFT JOIN guests g ON r.room_number = g.room_number";

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String status = rs.getString("status");
                String name = rs.getString("name");
                String contact = rs.getString("contact");
                int daysLeft = rs.getInt("days_left");

                if (!"booked".equalsIgnoreCase(status)) {
                    name = "";
                    contact = "";
                    daysLeft = 0;
                } else if (daysLeft < 0) {
                    daysLeft = 0; // no negative days
                }

                model.addRow(new Object[]{
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getString("bed_type"),
                        rs.getDouble("price"),
                        status,
                        name,
                        contact,
                        daysLeft
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
        String newBed = JOptionPane.showInputDialog("Enter new bed type (single/double/deluxe):", model.getValueAt(row, 2));
        double newPrice = Double.parseDouble(JOptionPane.showInputDialog("Enter new price:", model.getValueAt(row, 3)));
        String newStatus = JOptionPane.showInputDialog("Enter new status (booked/available):", model.getValueAt(row, 4));

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "UPDATE rooms SET type=?, bed_type=?, price=?, status=? WHERE room_number=?")) {

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
