import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class hotel {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "Shruti@123";

    private JFrame frame;
    private JTextField txtGuestName, txtRoomNo, txtContactNo, txtReservationId;
    private JTable table;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(hotel::new);
    }

    public hotel() {
        frame = new JFrame("Hotel Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 5, 5));

        panel.add(new JLabel("Guest Name:"));
        txtGuestName = new JTextField();
        panel.add(txtGuestName);

        panel.add(new JLabel("Room No:"));
        txtRoomNo = new JTextField();
        panel.add(txtRoomNo);

        panel.add(new JLabel("Contact No:"));
        txtContactNo = new JTextField();
        panel.add(txtContactNo);

        panel.add(new JLabel("Reservation ID (For Update/Delete):"));
        txtReservationId = new JTextField();
        panel.add(txtReservationId);

        JButton btnReserve = new JButton("Reserve Room");
        JButton btnView = new JButton("View Reservations");
        JButton btnUpdate = new JButton("Update Reservation");
        JButton btnDelete = new JButton("Delete Reservation");

        panel.add(btnReserve);
        panel.add(btnView);
        panel.add(btnUpdate);
        panel.add(btnDelete);

        frame.add(panel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Guest Name", "Room No", "Contact No", "Date"}, 0);
        table = new JTable(tableModel);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        btnReserve.addActionListener(e -> reserveRoom());
        btnView.addActionListener(e -> viewReservations());
        btnUpdate.addActionListener(e -> updateReservation());
        btnDelete.addActionListener(e -> deleteReservation());

        frame.setVisible(true);
    }

    private void reserveRoom() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO reservations(guest_name, room_number, contact_number) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, txtGuestName.getText());
                statement.setInt(2, Integer.parseInt(txtRoomNo.getText()));
                statement.setString(3, txtContactNo.getText());
                statement.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Reservation Successful!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + e.getMessage());
        }
    }

    private void viewReservations() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT * FROM reservations";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                tableModel.setRowCount(0);
                while (resultSet.next()) {
                    tableModel.addRow(new Object[]{
                            resultSet.getInt("reservation_id"),
                            resultSet.getString("guest_name"),
                            resultSet.getInt("room_number"),
                            resultSet.getString("contact_number"),
                            resultSet.getString("reservation_date")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + e.getMessage());
        }
    }

    private void updateReservation() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "UPDATE reservations SET guest_name = ?, room_number = ?, contact_number = ? WHERE reservation_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, txtGuestName.getText());
                statement.setInt(2, Integer.parseInt(txtRoomNo.getText()));
                statement.setString(3, txtContactNo.getText());
                statement.setInt(4, Integer.parseInt(txtReservationId.getText()));
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(frame, "Reservation Updated Successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Reservation Not Found!");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + e.getMessage());
        }
    }

    private void deleteReservation() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "DELETE FROM reservations WHERE reservation_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, Integer.parseInt(txtReservationId.getText()));
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(frame, "Reservation Deleted Successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Reservation Not Found!");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + e.getMessage());
        }
    }
}