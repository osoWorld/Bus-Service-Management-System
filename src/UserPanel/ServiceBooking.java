package UserPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ServiceBooking extends JFrame {
   private String [] cities = {"Lahore", "Islamabad", "Quetta"};
    public ServiceBooking(){
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Search Receipt");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create "Info" and "Receipt" boxes
        JPanel infoBox = createTitledRectanglePanel(0, 0, getWidth(), getHeight() / 3, "Info");

        JPanel receiptBox = createTitledRectanglePanel(0, getHeight() / 3, getWidth(), getHeight() * 2 / 3, "Seats");
        receiptBox.setLayout(new GridLayout(5, 6, 10, 10));


        // Create components for "Info" box
        JLabel customerLabel = new JLabel("Destined City:");
        JComboBox<String> destinedCiy = new JComboBox<>(cities);

        JCheckBox firstClassCheckBox = new JCheckBox("First Class");
        JCheckBox economyClassCheckBox = new JCheckBox("Economy Class");
        JCheckBox localClassCheckBox = new JCheckBox("Local Class");

        JButton checkSeats = new JButton("Check Seats");

        checkSeats.addActionListener(e -> {

            String selectedCity = (String) destinedCiy.getSelectedItem();
            boolean isFirstClassSelected = firstClassCheckBox.isSelected();
            boolean isEconomyClassSelected = economyClassCheckBox.isSelected();
            boolean isLocalClassSelected = localClassCheckBox.isSelected();

            assert selectedCity != null;
            String tableName = busName(selectedCity,isFirstClassSelected,isEconomyClassSelected,isLocalClassSelected);


            if (!tableName.isEmpty()) {
                disableBookedSeats(tableName, receiptBox);
            }

        });

        // Add components to "Info" box
        infoBox.setLayout(new GridLayout(3, 2, 10, 10));
        infoBox.add(customerLabel);
        infoBox.add(destinedCiy);
        infoBox.add(firstClassCheckBox);
        infoBox.add(economyClassCheckBox);
        infoBox.add(localClassCheckBox);
        infoBox.add(checkSeats);

        // Add components to "Receipt" box

        for (int i = 1; i <= 30; i++) {
            JCheckBox seatCheckBox = new JCheckBox(String.valueOf(i));
            seatCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
            receiptBox.add(seatCheckBox);

        }

        // Add "Info" and "Receipt" boxes to the main panel
        mainPanel.add(infoBox, BorderLayout.NORTH);
        mainPanel.add(receiptBox, BorderLayout.CENTER);

        // Create "Print" button
        JButton doneButton = new JButton("Done");
        doneButton.setPreferredSize(new Dimension(120, 28));

        // Add "Print" button to the main panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(doneButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedCity = (String) destinedCiy.getSelectedItem();
                boolean isFirstClassSelected = firstClassCheckBox.isSelected();
                boolean isEconomyClassSelected = economyClassCheckBox.isSelected();
                boolean isLocalClassSelected = localClassCheckBox.isSelected();

                String tableName = busName(selectedCity,isFirstClassSelected,isEconomyClassSelected,isLocalClassSelected);

                if (!tableName.isEmpty()) {
                    updateBookingStatus(tableName, receiptBox);
                }

                dispose();
            }
        });

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createTitledRectanglePanel(int x, int y, int width, int height, String title) {
        JPanel titledRectanglePanel = new JPanel();
        titledRectanglePanel.setBounds(x, y, width, height);
        titledRectanglePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title,
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16)));

        return titledRectanglePanel;
    }


    private void resetAll(JTextField customerTextField, JTextField employeeTextField) {
        customerTextField.setText("");
        employeeTextField.setText("");

    }

    private String busName(String selectedCity, boolean isFirstClassSelected, boolean isEconomyClassSelected, boolean isLocalClassSelected){

        String tableName = "";

        switch (selectedCity) {
            case "Lahore" -> {

                if (isFirstClassSelected) {
                    tableName = "Q_Connect";
                } else if (isEconomyClassSelected) {
                    tableName = "Road_Master";
                } else if (isLocalClassSelected) {
                    tableName = "Bilal_Travels";
                }
            }
            case "Islamabad" -> {

                if (isFirstClassSelected) {
                    tableName = "Higer";
                } else if (isEconomyClassSelected) {
                    tableName = "Daewoo_Express";
                } else if (isLocalClassSelected) {
                    tableName = "Skyways";
                }
            }
            case "Quetta" -> {

                if (isFirstClassSelected) {
                    tableName = "Faisal_Movers";
                } else if (isEconomyClassSelected) {
                    tableName = "Kohistan";
                } else if (isLocalClassSelected) {
                    tableName = "Manthar";
                }
            }
        }

        return tableName;
    }

    private void disableBookedSeats(String tableName, JPanel receiptBox) {

        String query = "SELECT [Seat Number] FROM " + tableName + " WHERE [Status] = 'Booked'";

        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://E://Dream Travels Database//Dream_Travels.accdb");
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int bookedSeatNumber = resultSet.getInt("Seat Number");

                // Disable the checkbox for the booked seat
                Component[] components = receiptBox.getComponents();
                for (Component component : components) {
                    if (component instanceof JCheckBox) {
                        JCheckBox seatCheckBox = (JCheckBox) component;
                        if (Integer.parseInt(seatCheckBox.getText()) == bookedSeatNumber) {
                            seatCheckBox.setEnabled(false);
                            break;
                        }
                    }
                }
            }

        } catch (SQLException sqe) {
            sqe.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking booked seats.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBookingStatus(String tableName, JPanel receiptBox) {

        String updateQuery = "UPDATE " + tableName + " SET [Status] = 'Booked' WHERE [Seat Number] = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://E://Dream Travels Database//Dream_Travels.accdb");
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            Component[] components = receiptBox.getComponents();
            for (Component component : components) {
                if (component instanceof JCheckBox) {
                    JCheckBox seatCheckBox = (JCheckBox) component;
                    if (seatCheckBox.isSelected()) {
                        int seatNumber = Integer.parseInt(seatCheckBox.getText());

                        preparedStatement.setInt(1, seatNumber);
                        preparedStatement.executeUpdate();
                    }
                }
            }

            JOptionPane.showMessageDialog(ServiceBooking.this, "Seats booked successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException sqe) {
            sqe.printStackTrace();
            JOptionPane.showMessageDialog(ServiceBooking.this, "Error updating seat status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ServiceBooking::new);
    }
}