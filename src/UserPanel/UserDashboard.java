package UserPanel;

import AdminPanel.TicketReceipt;
import Utils.Utils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class UserDashboard extends JFrame {
    private static final int CUSTOMER_DETAILS_WIDTH = 300;
    private static final int TUNING_MENU_WIDTH = 150;
    private static final int RECEIPT_WIDTH = 600;
    private static final int BUTTONS_WIDTH = 75;
    private JPanel customerDetailsRectangle;
    private JTextField customerIdField;
    private String[] destinations = {"Lahore", "Islamabad", "Quetta"};
    private JTextField numberOfSeatTextField;
    private JTextField seatNumberTextField;
    private int customerIdCounter = 2;

    public UserDashboard() {
        initializeGUI();

        customerIdCounter = getLastCustomerIdFromDatabase() + 1;
        customerIdField.setText(String.valueOf(customerIdCounter));
        customerIdField.setEditable(false);
    }

    private void initializeGUI() {
        setTitle("Dream Travels Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
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


        customerDetailsRectangle = createTitledRectanglePanel(0, 100, CUSTOMER_DETAILS_WIDTH, getHeight() - 100, "Passenger Details");
        customerDetailsRectangle.setLayout(new GridLayout(0, 2, 1, 70));


        String[] labels = {"Passenger ID:", "First Name:", "Surname:", "CNIC Number:"};
        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setPreferredSize(new Dimension(170, 50));
            if (label.equals("Passenger ID:")) {
                customerIdField = new JTextField();
                customerIdField.setPreferredSize(new Dimension(160, 20));
                customerIdField.setEditable(false);
                customerDetailsRectangle.add(jLabel);
                customerDetailsRectangle.add(customerIdField);
            } else {
                JTextField jTextField = new JTextField();
                jTextField.setPreferredSize(new Dimension(160, 20));
                customerDetailsRectangle.add(jLabel);
                customerDetailsRectangle.add(jTextField);

            }
        }

        JLabel seatNumberLabel = new JLabel("Seat Number:");
        seatNumberTextField = new JTextField();
        seatNumberTextField.setEditable(false);


        customerDetailsRectangle.add(seatNumberLabel);
        customerDetailsRectangle.add(seatNumberTextField);

        add(customerDetailsRectangle, BorderLayout.WEST);


        JPanel tuningMenuRectangle =
                createTitledRectanglePanel(CUSTOMER_DETAILS_WIDTH, 100, TUNING_MENU_WIDTH, getHeight() - 100, "");
        tuningMenuRectangle.setLayout(new GridLayout(3, 3, 1, 20));


        JPanel destinationBox = createTitledRectanglePanel(0, 0, TUNING_MENU_WIDTH, (getHeight() - 100) / 3, "Destination");
        destinationBox.setLayout(new GridLayout(2, 2, 10, 50));
        JLabel fromLabel = new JLabel("From:");
        JTextField fromTextField = new JTextField();
        fromTextField.setText(" Faisalabad");
        fromTextField.setEditable(false);
        fromLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 0));
        fromLabel.setFont(new Font("Arial", Font.CENTER_BASELINE, 14));

        JLabel destinationLabel = new JLabel("Destination:");
        destinationLabel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 15));
        destinationLabel.setFont(new Font("Arial", Font.CENTER_BASELINE, 14));

        JComboBox<String> destinationComboBox = new JComboBox<>(destinations);
        destinationComboBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));

        destinationBox.add(fromLabel);
        destinationBox.add(fromTextField);
        destinationBox.add(destinationLabel);
        destinationBox.add(destinationComboBox);
        tuningMenuRectangle.add(destinationBox);


        JPanel classCategoryBox =
                createTitledRectanglePanel(0, 0, TUNING_MENU_WIDTH, (getHeight() - 100) / 3, "Class Category");
        classCategoryBox.setLayout(new GridLayout(3, 2, 10, 10));
        JCheckBox firstClassCheckBox = new JCheckBox("First Class");
        JCheckBox economyClassCheckBox = new JCheckBox("Economy Class");
        JCheckBox localClassCheckBox = new JCheckBox("Local Class");
        JLabel emptyLabel = new JLabel();

        JLabel pricePerSeat = new JLabel("Ticket Price:");
        JTextField pricePerSeatTextField = new JTextField();
        pricePerSeatTextField.setEditable(false);

        JLabel totalAmount = new JLabel("Total Amount:");
        JTextField totalAmountTextField = new JTextField();
        totalAmountTextField.setEditable(false);

        JLabel numberOfSeat = new JLabel("Number of seats:");
        numberOfSeatTextField = new JTextField();

        JLabel depositAmount = new JLabel("Deposit Amount:");
        JTextField depositAmountTextField = new JTextField();


        classCategoryBox.add(firstClassCheckBox);
        classCategoryBox.add(economyClassCheckBox);
        classCategoryBox.add(localClassCheckBox);
        classCategoryBox.add(emptyLabel);
        classCategoryBox.add(pricePerSeat);
        classCategoryBox.add(pricePerSeatTextField);
        classCategoryBox.add(totalAmount);
        classCategoryBox.add(totalAmountTextField);
        classCategoryBox.add(numberOfSeat);
        classCategoryBox.add(numberOfSeatTextField);
        classCategoryBox.add(depositAmount);
        classCategoryBox.add(depositAmountTextField);

        tuningMenuRectangle.add(classCategoryBox);


        JPanel busDetailsBox =
                createTitledRectanglePanel(0, 0, TUNING_MENU_WIDTH, (getHeight() - 100) / 3, "Bus Details");
        busDetailsBox.setLayout(new GridLayout(3, 2, 10, 10));
        JLabel busNameLabel = new JLabel("Bus Name:");
        JTextField busNameTextField = new JTextField();

        JLabel busNumberLabel = new JLabel("Bus Number:");
        JTextField busNumberTextField = new JTextField();

        JButton checkButton = new JButton("Check");
        JButton assignSeatsButton = new JButton("Assign Seats");

        checkButton.addActionListener(e -> {
            String selectedDestination = (String) destinationComboBox.getSelectedItem();
            boolean isFirstClassSelected = firstClassCheckBox.isSelected();
            boolean isEconomyClassSelected = economyClassCheckBox.isSelected();
            boolean isLocalClassSelected = localClassCheckBox.isSelected();

            System.out.println(selectedDestination);

            assert selectedDestination != null;
            double ticketPrice = calculateTicketPrice(selectedDestination, isFirstClassSelected, isEconomyClassSelected, isLocalClassSelected);
            pricePerSeatTextField.setText(String.valueOf(ticketPrice));

            getBusDetails(selectedDestination, isFirstClassSelected, isEconomyClassSelected, isLocalClassSelected, busNameTextField, busNumberTextField);

            double totalTicketPrice = totalPrice(ticketPrice);

            totalAmountTextField.setText(String.valueOf(totalTicketPrice));

        });

        assignSeatsButton.addActionListener(e -> {

            String selectedDestination = (String) destinationComboBox.getSelectedItem();
            boolean isFirstClassSelected = firstClassCheckBox.isSelected();
            boolean isEconomyClassSelected = economyClassCheckBox.isSelected();
            boolean isLocalClassSelected = localClassCheckBox.isSelected();

            assert selectedDestination != null;
            String tableName = busName(selectedDestination, isFirstClassSelected, isEconomyClassSelected, isLocalClassSelected);

            UserSeatsAssign seatsAssign = new UserSeatsAssign(tableName);

            AssignSeats assignSeats = new AssignSeats(this, seatsAssign);
            assignSeats.setVisible(true);

        });

        busDetailsBox.add(busNameLabel);
        busDetailsBox.add(busNameTextField);
        busDetailsBox.add(busNumberLabel);
        busDetailsBox.add(busNumberTextField);
        busDetailsBox.add(assignSeatsButton);
        busDetailsBox.add(checkButton);

        tuningMenuRectangle.add(busDetailsBox);

        add(tuningMenuRectangle, BorderLayout.CENTER);


        JPanel receiptRectangle =
                createTitledRectanglePanel(CUSTOMER_DETAILS_WIDTH + TUNING_MENU_WIDTH, 100, RECEIPT_WIDTH, getHeight() - 100, "Receipt");
        receiptRectangle.setLayout(new BorderLayout());


        JTextPane receiptTextArea = new JTextPane();
        receiptTextArea.setPreferredSize(new Dimension(280, 80));
        receiptTextArea.setEditable(false);


        JScrollPane scrollPane = new JScrollPane(receiptTextArea);
        receiptRectangle.add(scrollPane, BorderLayout.CENTER);

        add(receiptRectangle, BorderLayout.EAST);

        JPanel buttonsRectangle =
                createRectanglePanel(CUSTOMER_DETAILS_WIDTH + TUNING_MENU_WIDTH + RECEIPT_WIDTH, getHeight() / 2, BUTTONS_WIDTH, getHeight() / 2, "");
        buttonsRectangle.setLayout(new GridLayout(1, 4, 10, 10)); // Adjust as needed

        // Add buttons
        String[] buttonLabels = {"Total", "Print", "Reset", "Schedule Details", "Search Ticket"};
        for (String buttonLabel : buttonLabels) {
            JButton jButton = new JButton(buttonLabel);
            jButton.setPreferredSize(new Dimension(60, 74));
            jButton.setFont(new Font("Arial", Font.BOLD, 21));
            buttonsRectangle.add(jButton);

            if (buttonLabel.equals("Total")) {
                jButton.addActionListener(e -> {
                    calculateTotal(busDetailsBox, classCategoryBox, receiptTextArea, customerDetailsRectangle, destinationComboBox, firstClassCheckBox, economyClassCheckBox, localClassCheckBox);
                });

            } else if (buttonLabel.equals("Reset")) {
                jButton.addActionListener(e -> {
                    resetAll(customerDetailsRectangle, classCategoryBox, busDetailsBox, receiptTextArea, customerDetailsRectangle);
                });

            } else if (buttonLabel.equals("Print")) {
                jButton.addActionListener(e -> {
                    connectDataWithDatabase(busDetailsBox, classCategoryBox, customerDetailsRectangle, destinationComboBox, firstClassCheckBox, economyClassCheckBox, localClassCheckBox);
                    Utils utils = new Utils();
                    utils.printItemsReceipt(receiptTextArea);
                });
            } else if (buttonLabel.equals("Search Ticket")) {
                jButton.addActionListener(e -> {
                    new TicketReceipt();
                });
            } else if (buttonLabel.equals("Schedule Details")) {
                jButton.addActionListener(e -> {
                    new BusScheduleDetails();
                });
            }
        }

        add(buttonsRectangle, BorderLayout.SOUTH);
        setVisible(true);
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

    private void resetAll(JPanel passengerDetailRectangle, JPanel classCategoryRectangle, JPanel busDetailsRectangle, JTextPane textReceiptArea, JPanel customerDetailsRectangle) {

        for (Component component : classCategoryRectangle.getComponents()) {
            if (component instanceof JCheckBox checkBox) {
                checkBox.setSelected(false);
            }
        }

        for (Component component : classCategoryRectangle.getComponents()) {
            if (component instanceof JTextField textField) {
                textField.setText("");
            }
        }

        for (Component component : busDetailsRectangle.getComponents()) {
            if (component instanceof JTextField textField) {
                textField.setText("");
            }
        }

        textReceiptArea.setText("");

        JTextField customerIdField = findTextField("Passenger ID:", customerDetailsRectangle);

        int currentCustomerId = Integer.parseInt(customerIdField.getText());


        for (Component component : passengerDetailRectangle.getComponents()) {
            if (component instanceof JTextField textField) {
                textField.setText("");
            }
        }

        // Update the customer ID field with the next value
        customerIdField.setText(String.valueOf(currentCustomerId + 1));

    }

    private void connectDataWithDatabase(JPanel busDetailsBox, JPanel classCategoryBox, JPanel customerDetailsRectangle, JComboBox<String> destinationComboBox, JCheckBox firstClass, JCheckBox economyClass, JCheckBox localClass) {

        String selectedClass = "";

        customerIdField.setText(String.valueOf(customerIdCounter));

        JTextField passengerIdField = findTextField("Passenger ID:", customerDetailsRectangle);
        JTextField firstNameField = findTextField("First Name:", customerDetailsRectangle);
        JTextField surNameField = findTextField("Surname:", customerDetailsRectangle);
        JTextField nicNumberField = findTextField("CNIC Number:", customerDetailsRectangle);
        JTextField seatNameField = findTextField("Seat Number:", customerDetailsRectangle);

        JTextField ticketPriceField = findTextField("Ticket Price:", classCategoryBox);
        JTextField numberOfSeatField = findTextField("Number of seats:", classCategoryBox);
        JTextField totalAmountField = findTextField("Total Amount:", classCategoryBox);
        JTextField depositAmountField = findTextField("Deposit Amount:", classCategoryBox);

        JTextField busNameField = findTextField("Bus Name:", busDetailsBox);
        JTextField busNumberField = findTextField("Bus Number:", busDetailsBox);

        customerIdCounter++;

        String passengerId = passengerIdField.getText();
        String firstName = firstNameField.getText();
        String surName = surNameField.getText();
        String nicNumber = nicNumberField.getText();
        String seatNumber = seatNameField.getText();
        String from = "Faisalabad";
        String selectedDestination = (String) destinationComboBox.getSelectedItem();

        if (firstClass.isSelected()) {
            selectedClass = "First Class";
        } else if (economyClass.isSelected()) {
            selectedClass = "Economy Class";
        } else if (localClass.isSelected()) {
            selectedClass = "Local Class";
        }

        String ticketPrice = ticketPriceField.getText();
        String numberOfSeats = numberOfSeatField.getText();
        String totalAmount = totalAmountField.getText();
        String depositAmount = depositAmountField.getText();

        double remainingAmount = 0.0;
        double totalAmountD = Double.parseDouble(totalAmount);
        double depositAmountD = Double.parseDouble(depositAmount);

        if (depositAmountD > totalAmountD) {
            remainingAmount = depositAmountD - totalAmountD;
        }

        String remainAmountS = String.valueOf(remainingAmount);

        String busName = busNameField.getText();
        String busNumber = busNumberField.getText();


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);


        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        String currentMonthYear = now.format(monthYearFormatter);

        saveDataToDatabase(customerIdCounter, firstName, surName, nicNumber, seatNumber, from, selectedDestination, selectedClass, ticketPrice, numberOfSeats, totalAmount, depositAmount, remainAmountS, busName, busNumber, currentMonthYear, formattedDateTime);

    }

    private void calculateTotal(JPanel busDetailsBox, JPanel classCategoryBox, JTextPane textReceiptPane, JPanel customerDetailsRectangle, JComboBox<String> destinationComboBox, JCheckBox firstClass, JCheckBox economyClass, JCheckBox localClass) {
        String selectedClass = "";

        JTextField passengerIdField = findTextField("Passenger ID:", customerDetailsRectangle);
        JTextField firstNameField = findTextField("First Name:", customerDetailsRectangle);
        JTextField surNameField = findTextField("Surname:", customerDetailsRectangle);
        JTextField nicNumberField = findTextField("CNIC Number:", customerDetailsRectangle);
        JTextField seatNameField = findTextField("Seat Number:", customerDetailsRectangle);

        JTextField ticketPriceField = findTextField("Ticket Price:", classCategoryBox);
        JTextField numberOfSeatField = findTextField("Number of seats:", classCategoryBox);
        JTextField totalAmountField = findTextField("Total Amount:", classCategoryBox);
        JTextField depositAmountField = findTextField("Deposit Amount:", classCategoryBox);

        JTextField busNameField = findTextField("Bus Name:", busDetailsBox);
        JTextField busNumberField = findTextField("Bus Number:", busDetailsBox);

        String passengerId = passengerIdField.getText();
        String firstName = firstNameField.getText();
        String surName = surNameField.getText();
        String nicNumber = nicNumberField.getText();
        String seatNumber = seatNameField.getText();
        String from = "Faisalabad";
        String selectedDestination = (String) destinationComboBox.getSelectedItem();

        if (firstClass.isSelected()) {
            selectedClass = "First Class";
        } else if (economyClass.isSelected()) {
            selectedClass = "Economy Class";
        } else if (localClass.isSelected()) {
            selectedClass = "Local Class";
        }

        String ticketPrice = ticketPriceField.getText();
        String numberOfSeats = numberOfSeatField.getText();
        String totalAmount = totalAmountField.getText();
        String depositAmount = depositAmountField.getText();

        double remainingAmount = 0.0;
        double totalAmountD = Double.parseDouble(totalAmount);
        double depositAmountD = Double.parseDouble(depositAmount);

        if (depositAmountD > totalAmountD) {
            remainingAmount = depositAmountD - totalAmountD;
        }

        String busName = busNameField.getText();
        String busNumber = busNumberField.getText();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        StyledDocument styledDocument = textReceiptPane.getStyledDocument();

        styledDocument.setCharacterAttributes(0, styledDocument.getLength(), textReceiptPane.getStyle(StyleContext.DEFAULT_STYLE), true);

        Style defaultStyle = textReceiptPane.getStyle(StyleContext.DEFAULT_STYLE);
        Style boldStyle = textReceiptPane.addStyle("Bold", defaultStyle);
        StyleConstants.setBold(boldStyle, true);

        Style largeStyle = textReceiptPane.addStyle("Large", defaultStyle);
        StyleConstants.setFontSize(largeStyle, 18); 
        StyleConstants.setBold(largeStyle, true);

        Style totalStyle = textReceiptPane.addStyle("Bold", defaultStyle);
        StyleConstants.setBold(totalStyle, true);
        StyleConstants.setFontSize(totalStyle, 15);

        Style headingStyle = textReceiptPane.addStyle("Bold", defaultStyle);
        StyleConstants.setBold(headingStyle, true);
        StyleConstants.setFontSize(headingStyle, 14);

        Style spacing = textReceiptPane.addStyle("Large", defaultStyle);
        StyleConstants.setFontSize(spacing, 2);

        try {
            styledDocument.insertString(styledDocument.getLength(), "\n      Dream Travels\n", largeStyle);
            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", spacing);
            styledDocument.insertString(styledDocument.getLength(), "Passenger Details:\n", headingStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), " Passenger Id : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), passengerId + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " First Name : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), firstName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Sur Name : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), surName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " CNIC Number : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), nicNumber + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Seat Number : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), seatNumber + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);

            styledDocument.insertString(styledDocument.getLength(), "\n", spacing);
            styledDocument.insertString(styledDocument.getLength(), "Traveling Details:\n", headingStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), " From : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), from + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Destination : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), selectedDestination + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Bus Class : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), selectedClass + "\n", defaultStyle);

            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);

            styledDocument.insertString(styledDocument.getLength(), "\n", spacing);
            styledDocument.insertString(styledDocument.getLength(), "Ticket Details:\n", headingStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), " Ticket Price : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), ticketPrice + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Number of Seats : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), numberOfSeats + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Total Amount : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), totalAmount + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Deposit Amount : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), depositAmount + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Remaining Amount : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), remainingAmount + "\n", defaultStyle);

            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", spacing);
            styledDocument.insertString(styledDocument.getLength(), "Bus Details:\n", headingStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), " Bus Name : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), busName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Bus Number : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), busNumber + "\n", defaultStyle);

            styledDocument.insertString(styledDocument.getLength(), "\n----------------------------------------------------\n", defaultStyle);

            styledDocument.insertString(styledDocument.getLength(), "\n\n " + formattedDateTime, defaultStyle);


        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private JTextField findTextField(String labelText, JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JLabel && labelText.equals(((JLabel) component).getText())) {
                int index = panel.getComponentZOrder(component);
                if (index < panel.getComponentCount() - 1) {
                    Component nextComponent = panel.getComponent(index + 1);
                    if (nextComponent instanceof JTextField) {
                        return (JTextField) nextComponent;
                    }
                }
            }
        }
        return null;
    }

    private double calculateTicketPrice(String destination, boolean isFirstClass, boolean isEconomyClass, boolean isLocalClass) {
        double basePrice = 0;

        switch (destination) {
            case "Lahore":
                basePrice = 1500;
                break;
            case "Islamabad":
                basePrice = 2500;
                break;
            case "Quetta":
                basePrice = 4500;
                break;
        }

        if (isFirstClass) {
            basePrice += 3000;
        } else if (isEconomyClass) {
            basePrice += 1000;
        } else if (isLocalClass) {
            basePrice += 0;
        }

        return basePrice;
    }

    private void getBusDetails(String selectedDestination, boolean isFirstClassSelected, boolean isEconomyClassSelected, boolean isLocalClassSelected, JTextField busNameTextField, JTextField busNumberTextField) {

        String query = "SELECT [Bus Name], [Bus Number] FROM OurBus WHERE [Destined City] = ? AND ([Bus Type] = ? OR [Bus Type] = ? OR [Bus Type] = ?)";

        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://E://Dream Travels Database//Dream_Travels.accdb");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, selectedDestination);

            if (isFirstClassSelected) {
                preparedStatement.setString(2, "First Class");
                preparedStatement.setString(3, "");
                preparedStatement.setString(4, "");
            } else if (isEconomyClassSelected) {
                preparedStatement.setString(2, "Economy Class");
                preparedStatement.setString(3, "");
                preparedStatement.setString(4, "");
            } else if (isLocalClassSelected) {
                preparedStatement.setString(2, "Local Class");
                preparedStatement.setString(3, "");
                preparedStatement.setString(4, "");
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String busName = resultSet.getString("Bus Name");
                String busNumber = resultSet.getString("Bus Number");
                busNameTextField.setText(busName);
                busNumberTextField.setText(busNumber);
            } else {
                JOptionPane.showMessageDialog(this, "No matching bus found for the selected options.",
                        "Bus Details", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException sqe) {
            sqe.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving bus details.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveDataToDatabase(int passengerId, String firstName, String surName, String nicNumber, String seatNumber, String from, String destination, String busClass, String ticketPrice, String numberOfSeats, String totalAmount, String depositAmount, String remainingAmount, String busName, String busNumber, String billingMonth, String date) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://E://Dream Travels Database//Dream_Travels.accdb");
            String sql = "INSERT INTO [TicketReceipt] ([Passenger ID], [First Name], [Sur Name], [Cnic NUMBER], [Seat Number], [From], [Destination], [Bus Class], [Ticket Price], [Number of Seats], [Total Amount], [Deposit Amount], [Remaining Amount], [Bus Name], [Bus Number], [Billing Month], [Date]) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, passengerId);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, surName);
            preparedStatement.setString(4, nicNumber);
            preparedStatement.setString(5, seatNumber);
            preparedStatement.setString(6, from);
            preparedStatement.setString(7, destination);
            preparedStatement.setString(8, busClass);
            preparedStatement.setString(9, ticketPrice);
            preparedStatement.setString(10, numberOfSeats);
            preparedStatement.setString(11, totalAmount);
            preparedStatement.setString(12, depositAmount);
            preparedStatement.setString(13, remainingAmount);
            preparedStatement.setString(14, busName);
            preparedStatement.setString(15, busNumber);
            preparedStatement.setString(16, billingMonth);
            preparedStatement.setString(17, date);

            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data saved to database successfully!", "Database", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException sqlE) {
            throw new RuntimeException(sqlE);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int getLastCustomerIdFromDatabase() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://E://Dream Travels Database//Dream_Travels.accdb");
            String sql = "SELECT MAX([Passenger ID]) FROM [TicketReceipt]";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException sqlE) {
            throw new RuntimeException(sqlE);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Return 0 if there is no customer ID in the database
        return 0;
    }

    private String busName(String selectedCity, boolean isFirstClassSelected, boolean isEconomyClassSelected, boolean isLocalClassSelected) {

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

    private double totalPrice(double ticketPrice) {
        double numberOfSeats = Double.parseDouble(numberOfSeatTextField.getText());

        return numberOfSeats * ticketPrice;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        numberOfSeatTextField.setText(String.valueOf(numberOfSeats));
    }

    public void selectedNumberOfSeats(ArrayList<String> selectedSeats) {
        String seatsText = String.join(", ", selectedSeats);
        System.out.println("Received Selected Seats: " + seatsText);
        seatNumberTextField.setText(seatsText);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserDashboard::new);
    }
}