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
                "Room No", "Type", "Bed Type", "Price", "Status", "Guest Name", "Contact", "Staying Days"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

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

        // Edit room details
        String newType = JOptionPane.showInputDialog("Enter new type:", model.getValueAt(row, 1));
        if (newType == null) return;

        String newBed;
        while (true) {
            newBed = JOptionPane.showInputDialog("Enter new bed (single/double):", model.getValueAt(row, 2));
            if (newBed == null) return;
            newBed = newBed.trim().toLowerCase();
            if (newBed.equals("single") || newBed.equals("double")) break;
            JOptionPane.showMessageDialog(this, "Invalid bed type! Only 'single' or 'double' allowed.");
        }

        String priceInput = JOptionPane.showInputDialog("Enter new price:", model.getValueAt(row, 3));
        if (priceInput == null) return;

        double newPrice;
        try {
            newPrice = Double.parseDouble(priceInput.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid price entered.");
            return;
        }

        String newStatus = JOptionPane.showInputDialog("Enter new status (booked/available):", model.getValueAt(row, 4));
        if (newStatus == null) return;

        // Edit guest details
        String currentGuestName = (String) model.getValueAt(row, 5);
        String currentContact = (String) model.getValueAt(row, 6);

        String newGuestName = JOptionPane.showInputDialog("Enter guest name:", currentGuestName);
        if (newGuestName == null) return;

        String newContact = JOptionPane.showInputDialog("Enter guest contact:", currentContact);
        if (newContact == null) return;

        try (Connection con = DBConnection.getConnection()) {
            // Update room
            try (PreparedStatement pstRoom = con.prepareStatement(
                    "UPDATE rooms SET type=?, bed=?, price=?, status=? WHERE room_number=?")) {

                pstRoom.setString(1, newType);
                pstRoom.setString(2, newBed);
                pstRoom.setDouble(3, newPrice);
                pstRoom.setString(4, newStatus);
                pstRoom.setString(5, roomNo);
                pstRoom.executeUpdate();
            }

            // Update guest
            // Only update if status is booked and guest info is provided
            if ("booked".equalsIgnoreCase(newStatus.trim())) {
                // Check if guest record exists for the room
                try (PreparedStatement pstCheckGuest = con.prepareStatement(
                        "SELECT id FROM guests WHERE room_number = ?")) {
                    pstCheckGuest.setString(1, roomNo);
                    try (ResultSet rs = pstCheckGuest.executeQuery()) {
                        if (rs.next()) {
                            // Update guest info
                            try (PreparedStatement pstGuestUpdate = con.prepareStatement(
                                    "UPDATE guests SET name=?, contact=? WHERE room_number=?")) {
                                pstGuestUpdate.setString(1, newGuestName);
                                pstGuestUpdate.setString(2, newContact);
                                pstGuestUpdate.setString(3, roomNo);
                                pstGuestUpdate.executeUpdate();
                            }
                        } else {
                            // Insert new guest record if none exists
                            try (PreparedStatement pstGuestInsert = con.prepareStatement(
                                    "INSERT INTO guests (name, contact, room_number) VALUES (?, ?, ?)")) {
                                pstGuestInsert.setString(1, newGuestName);
                                pstGuestInsert.setString(2, newContact);
                                pstGuestInsert.setString(3, roomNo);
                                pstGuestInsert.executeUpdate();
                            }
                        }
                    }
                }
            } else {
                // If status is not booked, optionally you may want to remove guest info or leave as is.
            }

            loadRoomData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
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
