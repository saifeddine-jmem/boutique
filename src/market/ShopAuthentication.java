package market;

import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class ShopAuthentication {
    public static ArrayList<Game> games = new ArrayList<>();
    public static void main(String[] args) {
        loadGames();
        UserManager.createAdmin();
        // Create the main frame
        JFrame frame = new JFrame("Welcome to Our Shop");
        frame.setSize(700, 394);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Welcome to Our Shop", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(titleLabel, BorderLayout.NORTH);
        
        // Create buttons for Admin and Client
       JButton adminButton = new JButton("Admin");
        adminButton.setFont(new Font("Arial", Font.PLAIN, 16));
        adminButton.setIcon(new ImageIcon("admin_icon.png")); // Replace with your icon path
        adminButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        adminButton.setHorizontalTextPosition(SwingConstants.CENTER);

         JButton clientButton = new JButton("Client");
        clientButton.setFont(new Font("Arial", Font.PLAIN, 16));
        clientButton.setIcon(new ImageIcon("client_icon.png")); // Replace with your icon path
        clientButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        clientButton.setHorizontalTextPosition(SwingConstants.CENTER);
        

       adminButton.addActionListener(e -> showAdminLoginDialog(frame));
        clientButton.addActionListener(e -> showClientLoginDialog(frame));

        // Add buttons to a panel
          JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.add(adminButton);
        buttonPanel.add(clientButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        // Add footer
        JLabel footerLabel = new JLabel("© 2024 ShopApp Inc.", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(footerLabel, BorderLayout.SOUTH);
        JButton signupButton = new JButton("Sign Up");
    signupButton.setFont(new Font("Arial", Font.PLAIN, 16));
    signupButton.setVerticalTextPosition(SwingConstants.BOTTOM);
    signupButton.setHorizontalTextPosition(SwingConstants.CENTER);
    signupButton.addActionListener(e -> showClientSignupDialog(frame));

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


       private static void loadGames() {
    games.add(new Game(
        "The Legend of Zelda: Breath of the Wild",
        59.99, "An open-world action-adventure game where you explore the vast kingdom of Hyrule, solve puzzles, and battle enemies.",
        "images/zelda.jpg"
    ));
    games.add(new Game(
        "The Witcher 3: Wild Hunt",
        49.99, "A story-driven RPG set in a visually stunning fantasy universe filled with meaningful choices and impactful consequences.",
        "images/witcher3.jpg"
    ));
    games.add(new Game(
        "Minecraft",
        29.99, "A sandbox game where you can build, explore, and survive in a blocky, procedurally-generated 3D world.",
        "images/minecraft.jpg"
    ));
    games.add(new Game(
        "Red Dead Redemption 2",
        69.99, "An epic tale of life in America’s unforgiving heartland with a huge, richly detailed open world.",
        "images/rdr2.jpg"
    ));
    games.add(new Game(
        "Cyberpunk 2077",
        49.99, "An open-world action-adventure story set in Night City, a megalopolis obsessed with power, glamour, and body modification.",
        "images/cyberpunk2077.jpg"
    ));
    games.add(new Game(
        "God of War",
        39.99, "An action-adventure game that follows Kratos and his son Atreus on an epic journey through Norse mythology.",
        "images/godofwar.jpg"
    ));
    games.add(new Game(
        "Grand Theft Auto V",
        29.99, "An open-world action game with a gripping story, multiple characters, and countless activities to explore.",
        "images/gta5.jpg"
    ));
    games.add(new Game(
        "Hollow Knight",
        14.99, "A challenging action-adventure game set in a vast interconnected world full of insects and mystery.",
        "images/hollowknight.jpg"
    ));
    games.add(new Game(
        "Dark Souls III",
        39.99, "An action RPG known for its dark fantasy setting, intricate world design, and punishing difficulty.",
        "images/darksouls3.jpg"
    ));
    games.add(new Game(
        "Among Us",
        4.99, "A multiplayer social deduction game where players work together to complete tasks, but beware of impostors!",
        "images/amongus.jpg"
    ));
}
}



