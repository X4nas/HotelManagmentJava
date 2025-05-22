package gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        setTitle("Hotel Properties Management System");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu bar setup
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        file.add(exit);
        menuBar.add(file);
        setJMenuBar(menuBar);

        // Status bar
        JLabel statusBar = new JLabel("Logged in: admin | Weather: Sunny | $: 82.3 | €: 89.1");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        add(statusBar, BorderLayout.SOUTH);

        // Panel with buttons
        JPanel panel = new JPanel();
        JButton rooms = new JButton("Rooms");
        rooms.addActionListener(e -> new RoomsWindow());
        panel.add(rooms);

        JButton guests = new JButton("Guests");
        guests.addActionListener(e -> new GuestsWindow());
        panel.add(guests);

        JButton reservations = new JButton("Reservations");
        reservations.addActionListener(e -> new ReservationWindow());
        panel.add(reservations);

        JButton roomStatus = new JButton("Room Status");
        roomStatus.addActionListener(e -> new RoomStatusWindow());
        panel.add(roomStatus);

        JButton billing = new JButton("Billing");
        billing.addActionListener(e -> new BillingWindow());
        panel.add(billing);

        JButton manageRooms = new JButton("Manage Rooms");
        manageRooms.addActionListener(e -> new RoomManagementWindow());
        panel.add(manageRooms);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}