package gui;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RoomStatusWindow extends JFrame {
    public RoomStatusWindow() {
        setTitle("Room Status Management");
        setSize(600, 400);
        setLayout(new GridLayout(5, 5));

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rooms")) {

            while (rs.next()) {
                String roomNum = rs.getString("room_number");
                String status = rs.getString("status");

                JButton btn = new JButton(roomNum + " (" + status + ")");
                btn.setToolTipText("Click to change status");

                btn.addActionListener(e -> {
                    String[] options = {"Clean", "Dirty", "DND"};
                    String newStatus = (String) JOptionPane.showInputDialog(
                            this,
                            "Set new status for Room " + roomNum,
                            "Update Room Status",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            status
                    );

                    if (newStatus != null) {
                        try (PreparedStatement ps = con.prepareStatement("UPDATE rooms SET status=? WHERE room_number=?")) {
                            ps.setString(1, newStatus);
                            ps.setString(2, roomNum);
                            ps.executeUpdate();
                            btn.setText(roomNum + " (" + newStatus + ")");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                add(btn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setVisible(true);
    }
}
