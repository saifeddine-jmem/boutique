package market;

import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;



public class ShopAuthentication {
    public static ArrayList<Game> games = new ArrayList<>();
    public static void main(String[] args) {
        DatabaseHelper.initializeDatabase();
        
        // Create the main frame
        JFrame frame = new JFrame("Welcome to Our Shop");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Center the frame

        JLabel titleLabel = new JLabel("Welcome to Our Shop", JLabel.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        frame.add(titleLabel, BorderLayout.NORTH);
        
        // Create buttons for Admin and Client
       JButton adminButton = createStyledButton("Admin", "images/admin_icon.png");
        JButton clientButton = createStyledButton("Client", "images/client_icon.png");
        JButton signupButton = createStyledButton("Sign Up", "images/signup_icon.png");
        

       adminButton.addActionListener(e -> showAdminLoginDialog(frame));
        clientButton.addActionListener(e -> showClientLoginDialog(frame));
        signupButton.addActionListener(e -> showClientSignupDialog(frame));


        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.add(adminButton);
        buttonPanel.add(clientButton);
        buttonPanel.add(signupButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        JLabel footerLabel = new JLabel("© 2024 ShopApp Inc.", JLabel.CENTER);
        footerLabel.setFont(new Font("Roboto", Font.ITALIC, 15));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(footerLabel, BorderLayout.SOUTH);

        // Set frame visible
        frame.setVisible(true);

// Add the signup button to your panel
buttonPanel.add(signupButton);
        // Set frame to be visible
        frame.setVisible(true);
    }

   private static void showAdminLoginDialog(JFrame parentFrame) {
    JPasswordField passwordField = new JPasswordField();
    JTextField emailField = new JTextField();
    Object[] message = {
        "Enter Admin Email:", emailField,
        "Enter Admin Password:", passwordField
    };

    int option = JOptionPane.showConfirmDialog(parentFrame, message, "Admin Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (option == JOptionPane.OK_OPTION) {
        String email = emailField.getText().trim();
        String enteredPassword = new String(passwordField.getPassword());

        // Check if email matches the admin's and password is correct
        if (UserManager.isAdmin(email) && enteredPassword.equals("admin123")) {
            JOptionPane.showMessageDialog(parentFrame, "Access Granted!", "Success", JOptionPane.INFORMATION_MESSAGE);
            openAdminPanel(); // Open Admin Panel
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Incorrect Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

   private static void showClientLoginDialog(JFrame parentFrame) {
    JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    
    Object[] message = {
        "Enter Your Email:", emailField,
        "Enter Your Password:", passwordField
    };

    int option = JOptionPane.showConfirmDialog(parentFrame, message, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (option == JOptionPane.OK_OPTION) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Check if email or password is empty
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Both fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate email format
        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            JOptionPane.showMessageDialog(parentFrame, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (UserManager.isAdmin(email)) {
            // Admin login
            if (password.equals("admin123")) {  // Check if admin password matches
                JOptionPane.showMessageDialog(parentFrame, "Admin login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                openAdminPanel();  // Open admin panel
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Incorrect admin password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Client login
            Client client = UserManager.getClient(email);
            if (client != null && client.getPassword().equals(password)) {
                JOptionPane.showMessageDialog(parentFrame, "Client login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Open the client panel
                openClientPanel(client);
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Invalid email or password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

   
    private static void showClientSignupDialog(JFrame parentFrame) {
    JTextField nameField = new JTextField();
    JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    
    Object[] message = {
        "Enter Your Name:", nameField,
        "Enter Your Email:", emailField,
        "Enter Your Password:", passwordField
    };

    int option = JOptionPane.showConfirmDialog(parentFrame, message, "Client Signup", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (option == JOptionPane.OK_OPTION) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Check if any fields are empty
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate email format
        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            JOptionPane.showMessageDialog(parentFrame, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check password strength (e.g., minimum 8 characters)
        if (password.length() < 8) {
            JOptionPane.showMessageDialog(parentFrame, "Password must be at least 8 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the client already exists
        if (UserManager.clientExists(email)) {
            JOptionPane.showMessageDialog(parentFrame, "This email is already registered. Please choose another one.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Create new client and save in UserManager
            UserManager.createClient(name, email, password);
            JOptionPane.showMessageDialog(parentFrame, "Signup successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}



     private static void openAdminPanel() {
        // Admin panel window
        SwingUtilities.invokeLater(() -> new AdminCode());
    }

    private static void openClientPanel(Client client) {
    // Launch the ClientPanel and pass the specific client with their own cart
    SwingUtilities.invokeLater(() -> new ClientPanel(client));
}


      
       private static JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.PLAIN, 25));
        button.setIcon(new ImageIcon(iconPath)); // Add your image path
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 110, 160));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
        return button;
    }

    
}



