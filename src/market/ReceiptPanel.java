package market;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.io.File;

public class ReceiptPanel {

    public static void Bill(Cart cart, double TotalPrice, String name, String email) {
        // Determine the Desktop directory and create the "clients" folder
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
        File clientsFolder = new File(desktopPath, "clients");
        if (!clientsFolder.exists()) {
            clientsFolder.mkdirs(); // Create the "clients" folder if it doesn't exist
        }

        // Create a folder for the client using their email
        File clientFolder = new File(clientsFolder, email);
        if (!clientFolder.exists()) {
            clientFolder.mkdirs(); // Create the client's folder if it doesn't exist
        }

        // Generate a unique bill file name with the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now(); // Get the current date and time
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = currentDateTime.format(dateFormatter);
        String fileName = "payment_bill_" + timestamp + ".txt";

        File billFile = new File(clientFolder, fileName);

        // Format the current date and time for the bill content
        DateTimeFormatter contentFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(contentFormatter);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(billFile))) {
            // Step 1: Write the Title
            writer.write("Payment Bill");
            writer.newLine();
            writer.write("============");
            writer.newLine();

            // Step 2: Add Customer Details
            writer.write(String.format("Customer Name: %-10s", name));
            writer.newLine();
            writer.write("Date: " + formattedDateTime); // Write the current date and time
            writer.newLine();
            writer.newLine();

            // Step 3: Add Table Header
            writer.write(String.format("%-20s %-10s", "Item", "Price"));
            writer.newLine();
            writer.write("---------------------------------------------");
            writer.newLine();

            // Step 4: Add Items
            for (Game game : cart.getCartItems()) {
                writer.write(String.format("%-20s %-10s$", game.getName(), game.getPrice()));
                writer.newLine();
            }

            // Step 5: Add Total
            writer.write("---------------------------------------------");
            writer.newLine();
            writer.write(String.format("%-20s %-10s$", "Total:", TotalPrice));
            writer.newLine();

            JOptionPane.showMessageDialog(null, "Payment bill created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Open the file with the default application
            if (Desktop.isDesktopSupported() && billFile.exists()) {
                Desktop.getDesktop().open(billFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ReceiptPanel() {

    }
}
