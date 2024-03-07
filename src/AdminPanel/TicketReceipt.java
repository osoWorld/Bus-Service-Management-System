package AdminPanel;

import Utils.Utils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.sql.*;

public class TicketReceipt extends JFrame {
    private JTextPane receiptTextPane;
    public TicketReceipt(){
        initializeGUI();
    }
    private void initializeGUI(){
        setTitle("Search Ticket");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create "Info" and "Receipt" boxes
        JPanel infoBox = createTitledRectanglePanel(0, 0, getWidth(), getHeight() / 3, "Ticket Info");
        JPanel receiptBox = createTitledRectanglePanel(0, getHeight() / 3, getWidth(), getHeight() * 2 / 3, "Ticket Receipt");

        // Create components for "Info" box
        JLabel customerLabel = new JLabel("Passenger Receipt ID:");
        JTextField customerTextField = new JTextField(20);
        customerLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));


        JButton customerCheckButton = new JButton("Ticket Check");
        JButton printButton = new JButton("Print");
        JButton resetButton = new JButton(" Reset ");
        JButton closeButton = new JButton("Close");

        customerCheckButton.addActionListener(e -> {
            String customerId = customerTextField.getText().trim();

            getCustomerInfo(customerId);

        });

        printButton.addActionListener(e -> {
            Utils utils = new Utils();
            utils.printItemsReceipt(receiptTextPane);
        });

        resetButton.addActionListener(e -> {
            resetAll(customerTextField);
        });

        closeButton.addActionListener(e -> {
            dispose();
        });

        // Add components to "Info" box
        infoBox.setLayout(new GridLayout(4, 2, 10, 10));
        infoBox.add(customerLabel);
        infoBox.add(customerTextField);
        infoBox.add(customerCheckButton);
        infoBox.add(printButton);
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

    private void getCustomerInfo(String customerId) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://E://Dream Travels Database//Dream_Travels.accdb");
            Statement statement = connection.createStatement();

            String query = "SELECT [First Name], [Sur Name], [Cnic Number], [Seat Number], [From], [Destination], [Bus Class], [Ticket Price], [Number of Seats], [Total Amount], [Deposit Amount], [Remaining Amount], [Bus Name], [Bus Number], [Billing Month], [Date] FROM TicketReceipt WHERE [Passenger ID] = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, customerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("First Name");
                String surName = resultSet.getString("Sur Name");
                String cnicNumber = resultSet.getString("Cnic Number");
                String seatNumber = resultSet.getString("Seat Number");
                String from = resultSet.getString("From");
                String destination = resultSet.getString("Destination");
                String busClass = resultSet.getString("Bus Class");
                String ticketPrice = resultSet.getString("Ticket Price");
                String numberOfSeats = resultSet.getString("Number of Seats");
                String totalAmount = resultSet.getString("Total Amount");
                String depositAmount = resultSet.getString("Deposit Amount");
                String remainingAmount = resultSet.getString("Remaining Amount");
                String busName = resultSet.getString("Bus Name");
                String busNumber = resultSet.getString("Bus Number");
                String billingMonth = resultSet.getString("Billing Month");
                String date = resultSet.getString("Date");


                displayCustomerReceipt(customerId,firstName,surName,cnicNumber,seatNumber,from,destination,busClass,ticketPrice,numberOfSeats,totalAmount,depositAmount,remainingAmount,busName,busNumber,billingMonth,date);
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

    private void displayCustomerReceipt(String customerId, String firstName, String surName, String cnicNumber, String seatNumber, String from, String destination, String busClass, String ticketPrice, String numberOfSeats, String totalAmount, String depositAmount, String remainingAmount, String busName, String busNumber, String billingMonth, String date) {

        StyledDocument styledDocument = receiptTextPane.getStyledDocument();

        styledDocument.setCharacterAttributes(0, styledDocument.getLength(), receiptTextPane.getStyle(StyleContext.DEFAULT_STYLE), true);

        Style defaultStyle = receiptTextPane.getStyle(StyleContext.DEFAULT_STYLE);
        Style boldStyle = receiptTextPane.addStyle("Bold", defaultStyle);
        StyleConstants.setBold(boldStyle, true);

        Style largeStyle = receiptTextPane.addStyle("Large", defaultStyle);
        StyleConstants.setFontSize(largeStyle, 18);

        try {
            styledDocument.insertString(styledDocument.getLength(), "\nTicket Receipt\n", largeStyle);
            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Passenger ID : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), customerId + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n First Name : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), firstName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Sur Name : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), surName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n CNIC Number : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), cnicNumber + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Seat Number : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), seatNumber + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n From : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), from + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Destination : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), destination + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Bus Class : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), busClass + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Ticket Price : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), ticketPrice + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Number of Seats : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), numberOfSeats + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Total Amount : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), totalAmount + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Deposit Amount : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), depositAmount + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Remaining Amount : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), remainingAmount + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Bus Name : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), busName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Bus Number : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), busNumber + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Billing Month : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), billingMonth + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Date : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), date + "\n", defaultStyle);

            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void resetAll(JTextField customerTextField) {
        customerTextField.setText("");
        receiptTextPane.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicketReceipt::new);
    }
}
