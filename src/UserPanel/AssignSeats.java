package UserPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class AssignSeats extends JFrame {
    private static final int CUSTOMER_DETAILS_WIDTH = 300;
    private static final int TUNING_MENU_WIDTH = 150;
    private static final int BUTTONS_WIDTH = 75;
    private UserDashboard userDashboard;
    private UserSeatsAssign seatsAssign;
    private String tableName;
    private ArrayList<String> mySeatTexts = new ArrayList<>();

    public AssignSeats(UserDashboard userDashboard, UserSeatsAssign seatsAssign) {
        this.userDashboard = userDashboard;
        this.seatsAssign = seatsAssign;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Dream Travels Dashboard");
        setSize(550, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setUndecorated(false);

        // Set background color to white
        getContentPane().setBackground(Color.WHITE);

        JPanel topRectangle = createRectanglePanel(0, 0, getWidth(), 100, "Dream Travels");
        topRectangle.setLayout(new BorderLayout());


        JLabel iconLabel = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/bus_stop64.png"))));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topRectangle.add(iconLabel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Dream Travels");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topRectangle.add(titleLabel, BorderLayout.SOUTH);

        add(topRectangle, BorderLayout.NORTH);


        JPanel seatAssignmentRectangle =
                createTitledRectanglePanel(CUSTOMER_DETAILS_WIDTH, 100, TUNING_MENU_WIDTH, getHeight() - 100, "Seat Assignment");
        seatAssignmentRectangle.setLayout(new GridLayout(5, 6, 10, 10));

        for (int i = 1; i <= 30; i++) {
            JCheckBox seatCheckBox = new JCheckBox(String.valueOf(i));
            seatCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
            seatAssignmentRectangle.add(seatCheckBox);
        }

        add(seatAssignmentRectangle, BorderLayout.CENTER);

        JButton assignButton = new JButton("Assign Button");
        assignButton.setPreferredSize(new Dimension(20, 38));

        assignButton.addActionListener(e -> {

            int selectedSeats = numberOfSeats(seatAssignmentRectangle);
            userDashboard.setNumberOfSeats(selectedSeats);


            updateBookingStatus(tableName, seatAssignmentRectangle);
            dispose();

            ArrayList<String> selectedSeatTexts = getSelectedSeatTexts();

            nameOfSeats(seatAssignmentRectangle);
            dispose();

            // Pass the selected seat texts to the UserDashboard
            userDashboard.selectedNumberOfSeats(selectedSeatTexts);

            printSelectedSeats();

        });

        add(assignButton, BorderLayout.SOUTH);

        setVisible(true);


        //BackEnd Database Work

        tableName = seatsAssign.getTableName();
        disableBookedSeats(tableName, seatAssignmentRectangle);
    }

    private JPanel createRectanglePanel(int x, int y, int width, int height, String title) {
        JPanel rectanglePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };

        rectanglePanel.setBounds(x, y, width, height);
        rectanglePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adjust as needed

        if (!title.isEmpty()) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            rectanglePanel.add(titleLabel, BorderLayout.NORTH);
        }

        return rectanglePanel;
    }

    private JPanel createTitledRectanglePanel(int x, int y, int width, int height, String title) {
        JPanel titledRectanglePanel = new JPanel();
        titledRectanglePanel.setBounds(x, y, width, height);
        titledRectanglePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title,
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16)));

        return titledRectanglePanel;
    }

    public int numberOfSeats(JPanel seatAssignmentRectangle) {
        int selectedCount = 0;

        // Iterate through all components in seatAssignmentRectangle
        for (Component component : seatAssignmentRectangle.getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                if (checkBox.isSelected()) {
                    selectedCount++;
                }
            }
        }

        return selectedCount;
    }

    public void nameOfSeats(JPanel seatAssignmentRectangle) {

        // Store selected seat numbers
        for (Component component : seatAssignmentRectangle.getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                if (checkBox.isSelected()) {
                    mySeatTexts.add(checkBox.getText());
                }
            }
        }
    }

    public ArrayList<String> getSelectedSeatTexts() {
        return mySeatTexts;
    }

    private void printSelectedSeats() {
        System.out.print("Selected Seats: ");
        System.out.println(String.join(", ", mySeatTexts));
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

        String updateQuery = "UPDATE " + tableName + " SET [Status] = '' WHERE ([Status] = 'Booked' AND " +
                "(Time() >= [Arrival At Destination] AND Time() <= [WayBack Departure Time]) OR " +
                "(Time() >= [Arrival At Home]))";

        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://E://Dream Travels Database//Dream_Travels.accdb");
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(updateQuery);

            JOptionPane.showMessageDialog(AssignSeats.this, "Seats reset successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException sqe) {
            sqe.printStackTrace();
//            JOptionPane.showMessageDialog(AssignSeats.this, "Error resetting seat status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AssignSeats(null,null));
    }
}
