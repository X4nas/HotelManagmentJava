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
        JLabel statusBar = new JLabel("Logged in: admin | Hotel Management System");
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

        JButton bill = new JButton("Bill Calculator");
        bill.addActionListener(e -> new BillCalculatorWindow());
        panel.add(bill);

        add(panel, BorderLayout.CENTER);
        setVisible(true);

        JButton roomStatus = new JButton("Room Status");
        roomStatus.addActionListener(e -> new RoomStatusWindow());
        panel.add(roomStatus);

        JButton cashDesk = new JButton("Cash Desk");
        cashDesk.addActionListener(e -> new CashDeskWindow());
        panel.add(cashDesk);

        JButton manageRooms = new JButton("Manage Rooms");
        manageRooms.addActionListener(e -> new RoomManagementWindow());
        panel.add(manageRooms);

        JButton billing = new JButton("Billing");
        billing.addActionListener(e -> new BillingWindow());
        panel.add(billing);


    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
