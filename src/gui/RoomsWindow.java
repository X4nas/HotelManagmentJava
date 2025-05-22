package gui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RoomsWindow extends JFrame {
    public RoomsWindow() {
        setTitle("Rooms Management");
        setSize(800, 400);
        setLayout(new GridLayout(5, 5));

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rooms")) {

            while (rs.next()) {
                JButton btn = new JButton(rs.getString("room_number"));
                btn.setToolTipText("Status: " + rs.getString("status") +
                        ", Type: " + rs.getString("type") +
                        ", Price: $" + rs.getDouble("price"));
                add(btn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        setVisible(true);
    }
}