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
import java.awt.Desktop;

public class ReceiptPanel {

        public static void Bill(Cart cart , double TotalPrice , String name)
        {
            String fileName = "payment_bill.txt"; // Output file name
        LocalDateTime currentDateTime = LocalDateTime.now(); // Get the current date and time

        // Format the current date and time (e.g., "2024-12-15 14:30:45")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Step 1: Write the Title
            writer.write("Payment Bill");
            writer.newLine();
            writer.write("============");
            writer.newLine();

            // Step 2: Add Customer Details
            writer.write(String.format("Customer Name: %-10s" , name));
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
            for(Game game : cart.getCartItems())
            {
            writer.write(String.format("%-20s %-10s$", game.getName() , game.getPrice()));
            writer.newLine();
            }

            // Step 5: Add Total
            writer.write("---------------------------------------------");
            writer.newLine();
            writer.write(String.format("%-20s %-10s$", "Total :",TotalPrice));
            writer.newLine();

            JOptionPane.showMessageDialog(null, "Payment bill TXT file created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
             File file = new File(fileName);
            if (file.exists()) {
                Desktop.getDesktop().open(file); // Open the file with the default application
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        }  
        public ReceiptPanel(){
       
        }
}