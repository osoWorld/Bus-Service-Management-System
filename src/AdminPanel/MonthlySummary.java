package AdminPanel;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MonthlySummary {
    public void generateMonthlySummary() {
        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://E://Dream Travels Database//Dream_Travels.accdb")) {

            // Get current date and month
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String currentDate = now.format(dateFormatter);
            DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
            String currentMonth = now.format(monthFormatter);

            // Retrieve data from TicketReceipt table
            double totalCustomerBill = 0.0;
            try (Statement statement = connection.createStatement()) {
                ResultSet customerResultSet = statement.executeQuery("SELECT [Total Amount] FROM TicketReceipt WHERE [Billing Month] = '" + currentMonth + "'");
                while (customerResultSet.next()) {
                    totalCustomerBill += customerResultSet.getDouble("Total Amount");
                }
            }

            // Retrieve data from Employee_Receipt table
            double totalEmployeePayingAmount = 0.0;
            try (Statement statement = connection.createStatement()) {
                ResultSet employeeResultSet = statement.executeQuery("SELECT [Total Salary] FROM Employee_Receipt WHERE [Salary Month] = '" + currentMonth + "'");
                while (employeeResultSet.next()) {
                    totalEmployeePayingAmount += employeeResultSet.getDouble("Total Salary");
                }
            }

            // Calculate total
            double total = totalCustomerBill + totalEmployeePayingAmount;

            // Calculate Maintenance Cost and Fuel Cost based on the current date
            int maintenanceCostPerDay = 600;
            int fuelCostPerDay = 1100;

            int dayOfMonth = now.getDayOfMonth();
            double maintenanceCost = maintenanceCostPerDay * dayOfMonth;
            double fuelCost = fuelCostPerDay * dayOfMonth;

            // Calculate Total Expense and Total Profit
            double totalExpense = maintenanceCost + fuelCost + totalEmployeePayingAmount;
            double totalProfit = total + totalExpense;

            // Insert data into Monthly_Summary table
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Monthly_Receipt ([Total Amount], [Maintenance Cost], [Fuel Cost], [Total Employee Salary Cost], [Total Expense], [Total Profit], [Summary Month], [Recorded Date]) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                preparedStatement.setDouble(1, totalCustomerBill);
                preparedStatement.setDouble(2, maintenanceCost);
                preparedStatement.setDouble(3, fuelCost);
                preparedStatement.setDouble(4, totalEmployeePayingAmount);
                preparedStatement.setDouble(5, totalExpense);
                preparedStatement.setDouble(6, totalProfit);
                preparedStatement.setString(7, currentMonth);
                preparedStatement.setString(8, currentDate);
                preparedStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Monthly summary generated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
