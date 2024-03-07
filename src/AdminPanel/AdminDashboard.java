package AdminPanel;

import Utils.Utils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdminDashboard extends JFrame {
    private static final int CUSTOMER_DETAILS_WIDTH = 300;
    private static final int TUNING_MENU_WIDTH = 150;
    private static final int RECEIPT_WIDTH = 600; // Increased width for Receipt
    private static final int BUTTONS_WIDTH = 75;
    private final Map<String, JTextField> employeeDetailsMap;
    private JTextField employeeNameTF, employeeTotalSalary, employeeId;
    private JTextPane receiptTextArea;
    private String[] cities = {"Lahore", "Islamabad", "Quetta"};
    private String[] busTypes = {"First Class", "Economy Class", "Local Class"};

    public AdminDashboard() {
        employeeDetailsMap = new HashMap<>();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Dream Travels Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the frame to be maximized
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        // Set background color to white
        getContentPane().setBackground(Color.WHITE);

        // Create a rectangle box at the top
        JPanel topRectangle = createRectanglePanel(0, 0, getWidth(), 100, "Admin Dream Travels");
        topRectangle.setLayout(new BorderLayout());

        // Add an icon
        JLabel iconLabel = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/bus_stop64.png")))); // Replace with your icon file
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topRectangle.add(iconLabel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Admin Dream Travels");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topRectangle.add(titleLabel, BorderLayout.SOUTH);

        add(topRectangle, BorderLayout.NORTH);

        // Create the "Customer Details" rectangle
        JPanel customerDetailsRectangle = createTitledRectanglePanel(0, 100, CUSTOMER_DETAILS_WIDTH, getHeight() - 100, "Employee Payroll");
        customerDetailsRectangle.setLayout(new GridLayout(0, 2, 1, 20)); // Adjust as needed

        // Add labels and text fields
        String[] labels = {"Employee ID:", "Employee Name:", "Employee Role:", "Employee Salary:", "Deduction:", "Bonus Amount:", "Total Salary:"};
        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setPreferredSize(new Dimension(170, 50));
            JTextField jTextField = new JTextField();
            jTextField.setPreferredSize(new Dimension(160, 35));
            customerDetailsRectangle.add(jLabel);
            customerDetailsRectangle.add(jTextField);

            if (label.equals("Total Salary:") || label.equals("Employee ID:") || label.equals("Employee Salary:") || label.equals("Employee Role:")) {
                jTextField.setEditable(false);
            }

            employeeDetailsMap.put(label, jTextField);

            if (label.equals("Employee Name:")) {
                employeeNameTF = new JTextField();
            }

            if (label.equals("Total Salary:")) {
                employeeTotalSalary = new JTextField();
            }
            if (label.equals("Employee ID:")) {
                employeeId = new JTextField();
            }
        }

        JButton checkButton = new JButton("Check");
        JButton payButton = new JButton("Pay");

        JButton totalButton = new JButton("Total Salary");
        JButton addEmployee = new JButton("Add A New Employee");

        checkButton.addActionListener(e -> {
            getEmployeeDataFromDatabase();

        });

        payButton.addActionListener(e -> {
            showEmployeeReceipt(customerDetailsRectangle, receiptTextArea);
        });

        totalButton.addActionListener(e -> {

            double totalSalary = calculateTotalSalary();
            JTextField totalSalaryField = employeeDetailsMap.get("Total Salary:");
            totalSalaryField.setText(String.format("%.2f", totalSalary));
        });

        addEmployee.addActionListener(e -> {
            new AddNewEmployee();
        });

        customerDetailsRectangle.add(checkButton);
        customerDetailsRectangle.add(payButton);
        customerDetailsRectangle.add(totalButton);
        customerDetailsRectangle.add(addEmployee);

        add(customerDetailsRectangle, BorderLayout.WEST);

        // Create the "Tuning Menu" rectangle
        JPanel tuningMenuRectangle = createTitledRectanglePanel(CUSTOMER_DETAILS_WIDTH, 100, TUNING_MENU_WIDTH, getHeight() - 100, "Bus Tracker");
        tuningMenuRectangle.setLayout(new GridLayout(10, 3, 1, 10));

        JLabel destinedCity = new JLabel("Destined City:");
        JComboBox<String> destinedComboBox = new JComboBox<>(cities);

        JLabel busType = new JLabel("Bus Status");
        JComboBox<String> busTypeComboBox = new JComboBox<>(busTypes);

        tuningMenuRectangle.add(destinedCity);
        tuningMenuRectangle.add(destinedComboBox);
        tuningMenuRectangle.add(busType);
        tuningMenuRectangle.add(busTypeComboBox);

        // Add Tracking Fields
        String[] tracker = {"Bus Name:", "Bus Number:","Bus Status:", "Departure Time:", "Arrival At Destination:", "Enroute to Back at:", "Arrival at Home:"};

        for (String trackerFields : tracker) {
            JLabel label = new JLabel(trackerFields);
            label.setPreferredSize(new Dimension(100, 60));
            JTextField jTextField = new JTextField();
            jTextField.setPreferredSize(new Dimension(160, 35));
            jTextField.setEditable(false);

            employeeDetailsMap.put(trackerFields, jTextField);

            tuningMenuRectangle.add(label);
            tuningMenuRectangle.add(jTextField);
        }

        JButton trackerCheckButton = new JButton("Track Bus");
//        JButton addNewBusButton = new JButton("Add New Bus");

        trackerCheckButton.addActionListener(e -> {
            tracKBus(destinedComboBox, busTypeComboBox);
        });

        tuningMenuRectangle.add(trackerCheckButton);
//        tuningMenuRectangle.add(addNewBusButton);

        add(tuningMenuRectangle, BorderLayout.CENTER);

        // Create the "Receipt" rectangle
        JPanel receiptRectangle =
                createTitledRectanglePanel(CUSTOMER_DETAILS_WIDTH + TUNING_MENU_WIDTH, 100, RECEIPT_WIDTH, getHeight() - 100, "Employee Receipt");
        receiptRectangle.setLayout(new BorderLayout());

        // Create a JTextArea for receipt details
        receiptTextArea = new JTextPane();
        receiptTextArea.setPreferredSize(new Dimension(280, 80));
        receiptTextArea.setEditable(false);

        // Add JTextArea to a JScrollPane for scrolling if needed
        JScrollPane scrollPane = new JScrollPane(receiptTextArea);
        receiptRectangle.add(scrollPane, BorderLayout.CENTER);

        add(receiptRectangle, BorderLayout.EAST);

        // Create the "Buttons" rectangle
        JPanel buttonsRectangle = createRectanglePanel(CUSTOMER_DETAILS_WIDTH + TUNING_MENU_WIDTH + RECEIPT_WIDTH, getHeight() / 2, BUTTONS_WIDTH, getHeight() / 2, "");
        buttonsRectangle.setLayout(new GridLayout(1, 4, 10, 10));

        // Add buttons
        String[] buttonLabels = {"Reset", "Search Ticket", "Print", "Search Receipt", "Monthly Summary"};
        for (String buttonLabel : buttonLabels) {
            JButton jButton = new JButton(buttonLabel);
            jButton.setPreferredSize(new Dimension(60, 74));
            jButton.setFont(new Font("Arial", Font.BOLD, 21));
            buttonsRectangle.add(jButton);

            if (buttonLabel.equals("Search Ticket")) {
                jButton.addActionListener(e -> {
                    new TicketReceipt();
                });
            } else if (buttonLabel.equals("Reset")) {
                jButton.addActionListener(e -> resetAll(customerDetailsRectangle, tuningMenuRectangle, receiptTextArea));
            } else if (buttonLabel.equals("Print")) {
                jButton.addActionListener(e -> {
                    Utils utils = new Utils();
                    utils.printItemsReceipt(receiptTextArea);
                });
            } else if (buttonLabel.equals("Monthly Summary")) {
                jButton.addActionListener(e -> {
                    MonthlySummary summary = new MonthlySummary();
                    summary.generateMonthlySummary();
                });
            } else if (buttonLabel.equals("Search Receipt")) {
                jButton.addActionListener(e -> {
                    new EmployeeSearchReceipt();
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
        rectanglePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

    private double calculateTotalSalary() {
        double employeeSalary = getDoubleValue(employeeDetailsMap.get("Employee Salary:"));
        double deductionAmount = getDoubleValue(employeeDetailsMap.get("Deduction:"));
        double bonusAmount = getDoubleValue(employeeDetailsMap.get("Bonus Amount:"));

        // Calculate total salary
        double totalSalary = employeeSalary - deductionAmount + bonusAmount;

        return totalSalary;
    }

    private double getDoubleValue(JTextField textField) {
        String text = textField.getText().trim();
        if (!text.isEmpty() && text.matches("^\\d*\\.?\\d*$")) {
            return Double.parseDouble(text);
        }
        return 0.0;
    }

    private void resetAll(JPanel customerDetailRectangle, JPanel tuningMenuRectangle, JTextPane textReceiptArea) {

        for (Component component : tuningMenuRectangle.getComponents()) {
            if (component instanceof JTextField textField) {
                textField.setText("");
            }
        }

        textReceiptArea.setText("");

        for (Component component : customerDetailRectangle.getComponents()) {
            if (component instanceof JTextField textField) {
                textField.setText("");
            }
        }

    }

    private void getEmployeeDataFromDatabase() {
        String employeeName = employeeDetailsMap.get("Employee Name:").getText();

        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://E://Dream Travels Database//Dream_Travels.accdb");
             Statement statement = connection.createStatement()) {

            String query = "SELECT [Employee ID], [Employee Role], [Employee Salary] FROM Employee_Receipt WHERE [Employee Name] = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, employeeName);
                ResultSet resultSet = preparedStatement.executeQuery();
                // Process the result set
                while (resultSet.next()) {
                    String employeeID = resultSet.getString("Employee ID");
                    String employeeRole = resultSet.getString("Employee Role");
                    String employeeSalary = resultSet.getString("Employee Salary");
                    // Set the retrieved values to your JTextFields
                    employeeDetailsMap.get("Employee ID:").setText(employeeID);
                    employeeDetailsMap.get("Employee Role:").setText(employeeRole);
                    employeeDetailsMap.get("Employee Salary:").setText(employeeSalary);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the SQL exception appropriately
        }
    }

    private void addValuesInDatabase(String bonusAmount, String formattedDate, String formattedTime, String currentMonthYear) {
        // insert the values into the Employee_Receipt table
        String employeeName = employeeDetailsMap.get("Employee Name:").getText();
        String employeeID = employeeDetailsMap.get("Employee ID:").getText();
        String employeeRole = employeeDetailsMap.get("Employee Role:").getText();
        String employeeSalary = employeeDetailsMap.get("Employee Salary:").getText();
        String deduction = employeeDetailsMap.get("Deduction:").getText();
        String totalSalary = employeeDetailsMap.get("Total Salary:").getText();

        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://E://Dream Travels Database//Dream_Travels.accdb");
             Statement statement = connection.createStatement()) {

            // Check if there is an entry for the current month
            String checkQuery = "SELECT * FROM Employee_Receipt WHERE [Employee Name] = ? AND [Salary Month] = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, employeeName);
                checkStatement.setString(2, currentMonthYear);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    // Entry for the current month already exists
                    JOptionPane.showMessageDialog(null, "An entry for the current month already exists for employee: " + employeeName);
                } else {
                    // Insert a new row for the current month
                    String insertQuery = "INSERT INTO Employee_Receipt ([Employee ID], [Employee Name], [Employee Role], [Employee Salary], [Deduction], [Bonus Amount], [Total Salary], [Date], [Time], [Salary Month]) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setString(1, employeeID);
                        insertStatement.setString(2, employeeName);
                        insertStatement.setString(3, employeeRole);
                        insertStatement.setString(4, employeeSalary);
                        insertStatement.setString(5, deduction);
                        insertStatement.setString(6, bonusAmount);
                        insertStatement.setString(7, totalSalary);
                        insertStatement.setString(8, formattedDate);
                        insertStatement.setString(9, formattedTime);
                        insertStatement.setString(10, currentMonthYear);

                        int rowsAffected = insertStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "New entry added to Employee_Receipt table.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to add new entry to Employee_Receipt table.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showEmployeeReceipt(JPanel customerDetailsRectangle, JTextPane textReceiptPane) {

        JTextField employeeIdField = findTextField("Employee ID:", customerDetailsRectangle);
        JTextField employeeNameField = findTextField("Employee Name:", customerDetailsRectangle);
        JTextField employeeRoleField = findTextField("Employee Role:", customerDetailsRectangle);
        JTextField employeeSalaryField = findTextField("Employee Salary:", customerDetailsRectangle);
        JTextField deductionField = findTextField("Deduction:", customerDetailsRectangle);
        JTextField bonusAmountField = findTextField("Bonus Amount:", customerDetailsRectangle);
        JTextField totalSalaryField = findTextField("Total Salary:", customerDetailsRectangle);

        String employeeId = employeeIdField.getText();
        String employeeName = employeeNameField.getText();
        String employeeRole = employeeRoleField.getText();
        String employeeSalary = employeeSalaryField.getText();
        String deduction = deductionField.getText();
        String bonusAmount = bonusAmountField.getText();
        String totalSalary = totalSalaryField.getText();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        // Create a styled document
        StyledDocument styledDocument = textReceiptPane.getStyledDocument();

        // Clear previous content
        styledDocument.setCharacterAttributes(0, styledDocument.getLength(), textReceiptPane.getStyle(StyleContext.DEFAULT_STYLE), true);

        // Create different styles
        Style defaultStyle = textReceiptPane.getStyle(StyleContext.DEFAULT_STYLE);
        Style boldStyle = textReceiptPane.addStyle("Bold", defaultStyle);
        StyleConstants.setBold(boldStyle, true);

        Style largeStyle = textReceiptPane.addStyle("Large", defaultStyle);
        StyleConstants.setFontSize(largeStyle, 18); // Adjust the size as needed
        StyleConstants.setBold(largeStyle, true);

        Style totalStyle = textReceiptPane.addStyle("Bold", defaultStyle);
        StyleConstants.setBold(totalStyle, true);
        StyleConstants.setFontSize(totalStyle, 15);

        Style headingStyle = textReceiptPane.addStyle("Bold", defaultStyle);
        StyleConstants.setBold(headingStyle, true);
        StyleConstants.setFontSize(headingStyle, 14);

        Style spacing = textReceiptPane.addStyle("Large", defaultStyle);
        StyleConstants.setFontSize(spacing, 2);

        // Insert text with different styles
        try {
            styledDocument.insertString(styledDocument.getLength(), "\n      Dream Travels\n", largeStyle);
            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", spacing);
            styledDocument.insertString(styledDocument.getLength(), "Employee Details:\n", headingStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), " Employee Id : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), employeeId + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Employee Name : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), employeeName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Employee Role : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), employeeRole + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Employee Salary : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), employeeSalary + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Deduction : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), deduction + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Bonus Amount : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), bonusAmount + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Total Salary : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), totalSalary + "\n", defaultStyle);

            styledDocument.insertString(styledDocument.getLength(), "\n----------------------------------------------------\n", defaultStyle);

            styledDocument.insertString(styledDocument.getLength(), "\n\n " + formattedDateTime, defaultStyle);


            //Add Values in Database
            //Its related changes

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy (EEEE)");
            String formattedDate = now.format(dateFormatter);

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            String formattedTime = now.format(timeFormatter);

            DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
            String currentMonthYear = now.format(monthYearFormatter);

            addValuesInDatabase(bonusAmount, formattedDate, formattedTime, currentMonthYear);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void tracKBus(JComboBox<String> destinationComboBox, JComboBox<String> busType) {
        String selectedDestination = (String) destinationComboBox.getSelectedItem();
        String selectedBusClass = (String) busType.getSelectedItem();

        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://E://Dream Travels Database//Dream_Travels.accdb");
             Statement statement = connection.createStatement()) {

            String query = "SELECT [Bus Name], [Bus Number], [Departure Time], [Arrival At Destination], [WayBack Departure Time], [Arrival At Home] FROM OurBus WHERE [Destined City] = ? AND [Bus Type] = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, selectedDestination);
                preparedStatement.setString(2, selectedBusClass);
                ResultSet resultSet = preparedStatement.executeQuery();

                // Process the result set
                while (resultSet.next()) {
                    String busName = resultSet.getString("Bus Name");
                    String busNumber = resultSet.getString("Bus Number");
                    LocalTime departureTime = resultSet.getTime("Departure Time").toLocalTime();
                    LocalTime arrivalAtDestination = resultSet.getTime("Arrival At Destination").toLocalTime();
                    LocalTime wayBackDepartureTime = resultSet.getTime("WayBack Departure Time").toLocalTime();
                    LocalTime arrivalAtHome = resultSet.getTime("Arrival At Home").toLocalTime();

                    LocalTime currentTime = LocalTime.now();


                    String busStatus;
                    if (currentTime.isAfter(departureTime) && currentTime.isBefore(arrivalAtDestination)) {
                        busStatus = "Going towards Destination";
                    } else if (currentTime.isAfter(wayBackDepartureTime) && currentTime.isBefore(arrivalAtHome)) {
                        busStatus = "Coming Back";
                    } else {
                        busStatus = "Stopped";
                    }

                    employeeDetailsMap.get("Bus Number:").setText(busNumber);
                    employeeDetailsMap.get("Bus Name:").setText(busName);
                    employeeDetailsMap.get("Departure Time:").setText(departureTime.toString());
                    employeeDetailsMap.get("Arrival At Destination:").setText(arrivalAtDestination.toString());
                    employeeDetailsMap.get("Enroute to Back at:").setText(wayBackDepartureTime.toString());
                    employeeDetailsMap.get("Arrival at Home:").setText(arrivalAtHome.toString());
                    employeeDetailsMap.get("Bus Status:").setText(busStatus);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the SQL exception appropriately
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDashboard::new);
    }
}
