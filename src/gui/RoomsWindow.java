package gui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RoomsWindow extends JFrame {
    public RoomsWindow() {
        setTitle("Rooms Management");
        setSize(800, 400);
        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rooms")) {

            while (rs.next()) {
                area.append("Room: " + rs.getString("room_number") +
                        ", Type: " + rs.getString("type") +
                        ", Bed: " + rs.getString("bed") +
                        ", Status: " + rs.getString("status") +
                        ", Price: ₹" + rs.getDouble("price") + "\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        add(new JScrollPane(area), BorderLayout.CENTER);
        setVisible(true);
    }
}