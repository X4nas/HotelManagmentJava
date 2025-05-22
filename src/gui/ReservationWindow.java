package gui;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReservationWindow extends JFrame {
    public ReservationWindow() {
        setTitle("Reservations");
        setSize(600, 300);
        setLayout(new BorderLayout());

        JTextArea reservationArea = new JTextArea();
        reservationArea.setEditable(false);

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM reservations")) {

            while (rs.next()) {
                reservationArea.append("Reservation ID: " + rs.getInt("id") +
                        ", Guest: " + rs.getString("guest_name") +
                        ", Room: " + rs.getString("room_number") +
                        ", Check-in: " + rs.getString("check_in") +
                        ", Check-out: " + rs.getString("check_out") + "\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        add(new JScrollPane(reservationArea), BorderLayout.CENTER);
        setVisible(true);
    }
}
