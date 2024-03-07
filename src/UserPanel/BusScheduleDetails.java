package UserPanel;

import Utils.Utils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;

public class BusScheduleDetails extends JFrame {
    private JTextPane receiptTextPane;
    private String[] cities = {"Lahore", "Islamabad", "Quetta"};

    public BusScheduleDetails() {
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Search Ticket");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create "Info" and "Receipt" boxes
        JPanel infoBox = createTitledRectanglePanel(0, 0, getWidth(), getHeight() / 3, "");
        JPanel receiptBox = createTitledRectanglePanel(0, getHeight() / 3, getWidth(), getHeight() * 2 / 3, "Bus Schedule Details");

        // Create components for "Info" box
        JLabel customerLabel = new JLabel("Destination City:");
        JComboBox<String> destinationBox = new JComboBox<>(cities);
        JLabel emptyLabel = new JLabel();
        JLabel emptyLabel2 = new JLabel();
        customerLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));


        JCheckBox firstClassCheckBox = new JCheckBox("First Class");
        JCheckBox economyClassCheckBox = new JCheckBox("Economy Class");
        JCheckBox localClassCheckBox = new JCheckBox("Local Class");


        JButton customerCheckButton = new JButton("Ticket Check");
        JButton printButton = new JButton("Print");
        JButton resetButton = new JButton(" Reset ");
        JButton closeButton = new JButton("Close");

        customerCheckButton.addActionListener(e -> {
            connectWithUI(destinationBox, firstClassCheckBox, economyClassCheckBox, localClassCheckBox);
        });

        printButton.addActionListener(e -> {
            Utils utils = new Utils();
            utils.printItemsReceipt(receiptTextPane);
        });

        resetButton.addActionListener(e -> {
            resetAll(infoBox);
        });

        closeButton.addActionListener(e -> {
            dispose();
        });

        // Add components to "Info" box
        infoBox.setLayout(new GridLayout(4, 3, 10, 10));
        infoBox.add(customerLabel);
        infoBox.add(destinationBox);
        infoBox.add(emptyLabel);
        infoBox.add(firstClassCheckBox);
        infoBox.add(economyClassCheckBox);
        infoBox.add(localClassCheckBox);
        infoBox.add(customerCheckButton);
        infoBox.add(printButton);
        infoBox.add(resetButton);
        infoBox.add(emptyLabel2);
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

    private void connectWithUI(JComboBox<String> destinationComboBox, JCheckBox firstClassCheckBox, JCheckBox economyClassCheckBox, JCheckBox localClassCheckBox) {
        String selectedDestination = (String) destinationComboBox.getSelectedItem();
        boolean isFirstClassSelected = firstClassCheckBox.isSelected();
        boolean isEconomyClassSelected = economyClassCheckBox.isSelected();
        boolean isLocalClassSelected = localClassCheckBox.isSelected();

        assert selectedDestination != null;
        displayCustomerReceipt(selectedDestination, isFirstClassSelected, isEconomyClassSelected, isLocalClassSelected);

    }

    private void displayCustomerReceipt(String selectedDestination, boolean isFirstClassSelected, boolean isEconomyClassSelected, boolean isLocalClassSelected) {

        String busName = "";
        String busNumber = "";
        String busClass = "";
        String ticketPrice = "";
        String from = "Faisalabad";
        String totalTime = "";
        String enrouteTiming = "";
        String enrouteArrival = "";
        String onWayBackTiming = "";
        String onWayBackArrival = "";

        switch (selectedDestination) {
            case "Lahore":
                if (isFirstClassSelected) {
                    busName = "Q Connect";
                    busNumber = "5248";
                    busClass = "First Class";
                    ticketPrice = "4500";
                    totalTime = "2 Hours 30 Minutes";
                    enrouteTiming = "8:00 Am";
                    enrouteArrival = "10:30 Am";
                    onWayBackTiming = "12:30 Pm";
                    onWayBackArrival = "03:00 Pm";

                } else if (isEconomyClassSelected) {
                    busName = "Road Master";
                    busNumber = "6478";
                    busClass = "Economy Class";
                    ticketPrice = "2500";
                    totalTime = "2 Hours 45 Minutes";
                    enrouteTiming = "8:20 Am";
                    enrouteArrival = "10:50 Am";
                    onWayBackTiming = "12:50 Pm";
                    onWayBackArrival = "03:35 Pm";

                } else if (isLocalClassSelected) {
                    busName = "Bilal Travels";
                    busNumber = "5432";
                    busClass = "Local Class";
                    ticketPrice = "1500";
                    totalTime = "3 Hours 15 Minutes";
                    enrouteTiming = "9:00 Am";
                    enrouteArrival = "11:30 Am";
                    onWayBackTiming = "01:30 Pm";
                    onWayBackArrival = "04:15 Pm";
                }
                break;
            case "Islamabad":
                if (isFirstClassSelected) {
                    busName = "Higer";
                    busNumber = "7722";
                    busClass = "First Class";
                    ticketPrice = "5500";
                    totalTime = "4 Hours 00 Minutes";
                    enrouteTiming = "8:15 Am";
                    enrouteArrival = "12:15 Am";
                    onWayBackTiming = "2:15 Pm";
                    onWayBackArrival = "6:15 Pm";
                } else if (isEconomyClassSelected) {
                    busName = "Daewoo Express";
                    busNumber = "5956";
                    busClass = "Economy Class";
                    ticketPrice = "3500";
                    totalTime = "4 Hours 15 Minutes";
                    enrouteTiming = "8:45 Am";
                    enrouteArrival = "12:45 Am";
                    onWayBackTiming = "2:45 Pm";
                    onWayBackArrival = "07:00 Pm";
                } else if (isLocalClassSelected) {
                    busName = "Skyway";
                    busNumber = "7674";
                    busClass = "Local Class";
                    ticketPrice = "2500";
                    totalTime = "4 Hours 45 Minutes";
                    enrouteTiming = "9:15 Am";
                    enrouteArrival = "1:15 Pm";
                    onWayBackTiming = "3:15 Pm";
                    onWayBackArrival = "08:00 Pm";
                }
                break;
            case "Quetta":
                if (isFirstClassSelected) {
                    busName = "Faisal Movers";
                    busNumber = "4378";
                    busClass = "First Class";
                    ticketPrice = "7500";
                    totalTime = "12 Hours 40 Minutes";
                    enrouteTiming = "10:00 Am";
                    enrouteArrival = "10:40 Pm";
                    onWayBackTiming = "12:40 Am";
                    onWayBackArrival = "01:20 Pm";
                } else if (isEconomyClassSelected) {
                    busName = "Kohistan";
                    busNumber = "8996";
                    busClass = "Economy Class";
                    ticketPrice = "5500";
                    totalTime = "13 Hours 05 Minutes";
                    enrouteTiming = "10:40 Am";
                    enrouteArrival = "11:20 Pm";
                    onWayBackTiming = "01:20 Am";
                    onWayBackArrival = "02:25 Pm";
                } else if (isLocalClassSelected) {
                    busName = "Manthar";
                    busNumber = "5639";
                    busClass = "Local Class";
                    ticketPrice = "4500";
                    totalTime = "13 Hours 35 Minutes";
                    enrouteTiming = "11:20 Am";
                    enrouteArrival = "01:15 Am";
                    onWayBackTiming = "03:15 Am";
                    onWayBackArrival = "04:35 Pm";
                }
                break;
        }


        StyledDocument styledDocument = receiptTextPane.getStyledDocument();

        styledDocument.setCharacterAttributes(0, styledDocument.getLength(), receiptTextPane.getStyle(StyleContext.DEFAULT_STYLE), true);

        Style defaultStyle = receiptTextPane.getStyle(StyleContext.DEFAULT_STYLE);
        Style boldStyle = receiptTextPane.addStyle("Bold", defaultStyle);
        StyleConstants.setBold(boldStyle, true);

        Style largeStyle = receiptTextPane.addStyle("Large", defaultStyle);
        StyleConstants.setFontSize(largeStyle, 18);

        try {
            styledDocument.insertString(styledDocument.getLength(), "\nBus Schedule Timings\n", largeStyle);
            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n From : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), from + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Destination : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), selectedDestination + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Bus Class : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), busClass + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Ticket Price : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), ticketPrice + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Bus Name : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), busName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Bus Number: ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), busNumber + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Total Time: ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), totalTime + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Enrouting Time: ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), enrouteTiming + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Arrival At Destination: ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), enrouteArrival + "\n", defaultStyle);
//            styledDocument.insertString(styledDocument.getLength(), "\n Total Time: ", boldStyle);
//            styledDocument.insertString(styledDocument.getLength(), totalTime + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Enrouting Back Time: ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), onWayBackTiming + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Back Arrival At Home: ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), onWayBackArrival + "\n", defaultStyle);


            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void resetAll(JPanel infoBox) {

        for (Component component : infoBox.getComponents()) {
            if (component instanceof JCheckBox checkBox) {
                checkBox.setSelected(false);
            }
        }

        receiptTextPane.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BusScheduleDetails::new);
    }
}
