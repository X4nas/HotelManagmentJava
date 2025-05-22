package gui;

import db.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RoomManagementWindow extends JFrame {
    public RoomManagementWindow() {
        setTitle("Room Management");
        setSize(800, 400);

        String[] columnNames = {"Room No", "Room Type", "Bed Type", "Price", "Status"};
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.setColumnIdentifiers(columnNames);

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

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
        setVisible(true);
    }
}
