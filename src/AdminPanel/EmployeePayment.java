package AdminPanel;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class EmployeePayment {

    public void getEmployeeDataFromDatabase(Map<String, JTextField> employeeDetailsMap) {
        String employeeName = employeeDetailsMap.get("Employee Name:").getText();

        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://E://Garage Genius Database//Garage_Genius.accdb");
             Statement statement = connection.createStatement()) {

            String query = "SELECT [Employee ID], [Employee Salary] FROM Employee_Receipt WHERE [Employee Name] = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, employeeName);
                ResultSet resultSet = preparedStatement.executeQuery();
                // Process the result set
                while (resultSet.next()) {
                    String employeeID = resultSet.getString("Employee ID");
                    String employeeSalary = resultSet.getString("Employee Salary");
                    // Set the retrieved values to your JTextFields
                    employeeDetailsMap.get("Employee ID:").setText(employeeID);
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

    public double calculateTotalSalary(Map<String, JTextField> employeeDetailsMap) {
        double employeeSalary = getDoubleValue(employeeDetailsMap.get("Employee Salary:"));
        double deductionAmount = getDoubleValue(employeeDetailsMap.get("Deduction"));
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

    public void payCheckSend(Map<String, JTextField> employeeDetailsMap) {
        StringBuilder employeeDetails = new StringBuilder("Employee Details:\n\n");
        for (Map.Entry<String, JTextField> entry : employeeDetailsMap.entrySet()) {
            String label = entry.getKey();
            String value = entry.getValue().getText();
            employeeDetails.append(label).append(": ").append(value).append("\n");
        }

        // Append Bonus Amount to the employee details
        String bonusAmount = employeeDetailsMap.get("Bonus Amount:").getText();
        employeeDetails.append("Bonus Amount: ").append(bonusAmount).append("\n");

        // Get the current date, day, and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy (EEEE)");
        String formattedDate = now.format(dateFormatter);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        String formattedTime = now.format(timeFormatter);

        // Append the current date, day, and time to the employee details
        employeeDetails.append("Date: ").append(formattedDate).append("\n");
        employeeDetails.append("Time: ").append(formattedTime).append("\n");

        // Get the current month and year
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        String currentMonthYear = now.format(monthYearFormatter);

        // Append the current month and year to the employee details
        employeeDetails.append("Salary Month: ").append(currentMonthYear).append("\n");

        // Open the EmployeePayment window with the employee details
//        new EmployeePayment(employeeDetails.toString());

        addValuesInDatabase(employeeDetailsMap, bonusAmount, formattedDate, formattedTime, currentMonthYear);
    }

    private void addValuesInDatabase(Map<String, JTextField> employeeDetailsMap, String bonusAmount, String formattedDate, String formattedTime, String currentMonthYear) {
        // insert the values into the Employee_Receipt table
        String employeeName = employeeDetailsMap.get("Employee Name:").getText();
        String employeeID = employeeDetailsMap.get("Employee ID:").getText();
        String deduction = employeeDetailsMap.get("Deduction").getText();
        String totalSalary = employeeDetailsMap.get("Total Salary:").getText();

        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://E://Garage Genius Database//Garage_Genius.accdb");
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
                    String insertQuery = "INSERT INTO Employee_Receipt ([Employee ID], [Employee Name], [Deduction], [Bonus Amount], [Paying Amount], [Date], [Time], [Salary Month]) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setString(1, employeeID);
                        insertStatement.setString(2, employeeName);
                        insertStatement.setString(3, deduction);
                        insertStatement.setString(4, bonusAmount);
                        insertStatement.setString(5, totalSalary);
                        insertStatement.setString(6, formattedDate);
                        insertStatement.setString(7, formattedTime);
                        insertStatement.setString(8, currentMonthYear);

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


}
