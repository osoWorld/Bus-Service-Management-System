package AdminPanel;

import Utils.Utils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.sql.*;

public class EmployeeSearchReceipt extends JFrame {
    private JTextPane receiptTextPane;

    public EmployeeSearchReceipt() {
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
        JPanel infoBox = createTitledRectanglePanel(0, 0, getWidth(), getHeight() / 3, "Employee Receipt Info");
        JPanel receiptBox = createTitledRectanglePanel(0, getHeight() / 3, getWidth(), getHeight() * 2 / 3, "Employee Receipt");


        JLabel employeeLabel = new JLabel("Employee Receipt ID:");
        JTextField employeeTextField = new JTextField(20);
        employeeLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JButton printButton = new JButton("Print");
        JButton employeeCheckButton = new JButton("Employee Check");
        JButton resetButton = new JButton(" Reset ");
        JButton closeButton = new JButton("Close");

        printButton.addActionListener(e -> {
            Utils utils = new Utils();
            utils.printItemsReceipt(receiptTextPane);
        });

        employeeCheckButton.addActionListener(e -> {
            String employeeId = employeeTextField.getText().trim();
            getEmployeeInfo(employeeId);
        });

        resetButton.addActionListener(e -> {
            resetAll(employeeTextField);
        });

        closeButton.addActionListener(e -> {
            dispose();
        });

        // Add components to "Info" box
        infoBox.setLayout(new GridLayout(4, 2, 10, 10));

        infoBox.add(employeeLabel);
        infoBox.add(employeeTextField);
        infoBox.add(printButton);
        infoBox.add(employeeCheckButton);
        infoBox.add(resetButton);
        infoBox.add(closeButton);

        // Create components for "Receipt" box
        receiptTextPane = new JTextPane();
        receiptTextPane.setEditable(false);
        JScrollPane receiptScrollPane = new JScrollPane(receiptTextPane);

        // Add components to "Receipt" box
        receiptBox.setLayout(new BorderLayout());
        receiptBox.add(receiptScrollPane, BorderLayout.CENTER);

        // Add "Info" and "Receipt" boxes to the main panel
        mainPanel.add(infoBox, BorderLayout.NORTH);
        mainPanel.add(receiptBox, BorderLayout.CENTER);


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

    private void getEmployeeInfo(String employeeId) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://E://Dream Travels Database//Dream_Travels.accdb");
            Statement statement = connection.createStatement();

            String query = "SELECT [Employee Name], [Employee Role], [Employee Salary], [Deduction], [Bonus Amount], [Total Salary], [Date], [Time], [Salary Month] FROM Employee_Receipt WHERE [Employee ID] = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String employeeName = resultSet.getString("Employee Name");
                String employeeRole = resultSet.getString("Employee Role");
                String employeeSalary = resultSet.getString("Employee Salary");
                String deduction = resultSet.getString("Deduction");
                String bonusAmount = resultSet.getString("Bonus Amount");
                String totalSalary = resultSet.getString("Total Salary");
                String date = resultSet.getString("Date");
                String time = resultSet.getString("Time");
                String salaryMonth = resultSet.getString("Salary Month");


                displayEmployeeReceipt(employeeId, employeeName, employeeRole, employeeSalary, deduction, bonusAmount, totalSalary, date, time, salaryMonth);
            } else {
                JOptionPane.showMessageDialog(this, "Customer ID not found in the database.");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayEmployeeReceipt(String employeeID, String employeeName, String employeeRole, String employeeSalary, String deduction, String bonusAmount, String totalAmount, String date, String time, String salaryMonth) {
        StyledDocument styledDocument = receiptTextPane.getStyledDocument();

        // Clear previous content
        styledDocument.setCharacterAttributes(0, styledDocument.getLength(), receiptTextPane.getStyle(StyleContext.DEFAULT_STYLE), true);

        // Create different styles
        Style defaultStyle = receiptTextPane.getStyle(StyleContext.DEFAULT_STYLE);
        Style boldStyle = receiptTextPane.addStyle("Bold", defaultStyle);
        StyleConstants.setBold(boldStyle, true);

        Style largeStyle = receiptTextPane.addStyle("Large", defaultStyle);
        StyleConstants.setFontSize(largeStyle, 18);

        try {
            styledDocument.insertString(styledDocument.getLength(), "\nEmployee Receipt\n", largeStyle);
            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Employee Id : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), employeeID + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Employee Name : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), employeeName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Employee Role : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), employeeRole + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " employeeSalary : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), employeeSalary + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Deduction : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), deduction + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Bonus Amount : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), bonusAmount + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Total Salary : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), totalAmount + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Payment Date : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), date + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Payment Time : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), time + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Salary Month : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), salaryMonth + "\n", defaultStyle);

            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void resetAll(JTextField employeeTextField) {
        employeeTextField.setText("");
        receiptTextPane.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeSearchReceipt::new);
    }
}
