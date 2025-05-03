package market;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.swing.border.EmptyBorder;  // Add this import



public class AdminCode extends JFrame {

    private static final long serialVersionUID = 1L;

    public AdminCode() {
       
        // Frame settings
        setTitle("Admin Panel");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null); // Center the window

        // Title Label
        JLabel titleLabel = new JLabel("Admin Panel", JLabel.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // User Management Button
        JButton userManagementButton = createStyledButton(
            "User Management", 
            "images/user_management_icon.png", 
            new Color(70, 130, 180)
        );
        userManagementButton.addActionListener(e -> openUserManagementPanel());

        // Game Management Button
        JButton gameManagementButton = createStyledButton(
            "Game Management", 
            "images/game_management_icon.png", 
            new Color(34, 139, 34)
        );
        gameManagementButton.addActionListener(e -> openGameManagementPanel());

        // Add buttons to panel
        buttonPanel.add(userManagementButton);
        buttonPanel.add(gameManagementButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Footer
        JLabel footerLabel = new JLabel("© 2024 ShopApp Inc.", JLabel.CENTER);
        footerLabel.setFont(new Font("Roboto", Font.ITALIC, 12));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(footerLabel, BorderLayout.SOUTH);

        // Set visible
        setVisible(true);
    }

    // Method to open User Management Panel (Existing code for managing users)
    private void openUserManagementPanel() {
        // Launch User Management panel (Client management code from your earlier implementation)
     try {
            UserManagementPanel panel = new UserManagementPanel();
            panel.setVisible(true);
        } catch (Exception e) {
            showError("Error opening User Management Panel: " + e.getMessage());
        }
    }
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        return button;
    }
    // Method to open Game Management Panel (Existing game management functionality)
    private void openGameManagementPanel() {
        // Launch Game Management panel (Game management code from your previous AdminCode)
       try {
            GameManagementPanel panel = new GameManagementPanel();
            panel.setVisible(true);
        } catch (Exception e) {
            showError("Error opening Game Management Panel: " + e.getMessage());
        }
    }
     private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
     
    private JButton createStyledButton(String text, String iconPath, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 16));
        button.setIcon(new ImageIcon(iconPath)); // Add your icon image path here
        button.setHorizontalTextPosition(SwingConstants.RIGHT); // Text to the right of the icon
        button.setIconTextGap(10); // Space between icon and text
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        return button;
    }
    private JButton createStyledButton1(String text, Color color) {
    JButton button = new JButton(text);
    button.setFont(new Font("Roboto", Font.BOLD, 14));
    button.setBackground(color);
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    button.setBorder(BorderFactory.createRaisedBevelBorder());

    button.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            button.setBackground(color.darker());
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
            button.setBackground(color);
        }
    });
    return button;
}
    // Inner class for User Management (Same code as before for managing users)
    class UserManagementPanel extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultListModel<String> clientListModel;
    private JList<String> clientList;

    public UserManagementPanel() {
        setTitle("User Management Panel");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Add client button
        JButton addClientButton = new JButton("Add Client");
        addClientButton.addActionListener(e -> showAddClientDialog());
        
        JButton editButton = new JButton("Edit User");
        editButton.addActionListener(e -> editSelectedClient());
        // Delete client button
        JButton deleteClientButton = new JButton("Delete Client");
        deleteClientButton.addActionListener(e -> deleteClient());

        // Client list
        clientListModel = new DefaultListModel<>();
        clientList = new JList<>(clientListModel);
        clientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clientList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = clientList.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        String email = clientListModel.get(selectedIndex).split(" - ")[1];
                        showClientDetails(UserManager.getClient(email));
                    }
                }
            }
        });

        JScrollPane clientListScrollPane = new JScrollPane(clientList);

        // Panel for the buttons (Add and Delete)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));  // Two buttons side by side
        buttonPanel.add(addClientButton);
        buttonPanel.add(deleteClientButton);
        buttonPanel.add(addClientButton);
        buttonPanel.add(editButton);  // Add Edit button
        buttonPanel.add(deleteClientButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(clientListScrollPane, BorderLayout.CENTER);

        updateClientList();
        setVisible(true);
    }

    private void updateClientList() {
        clientListModel.clear();
        try {
                Map<String, Client> clients = UserManager.getAllClients();
                if (clients != null) {
                    for (Client client : clients.values()) {
                        clientListModel.addElement(client.getName() + " - " + client.getEmail());
                    }
                }
            } catch (Exception e) {
                showError("Error updating client list: " + e.getMessage());
            }
    }
    private void editSelectedClient() {
        int selectedIndex = clientList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String email = clientListModel.get(selectedIndex).split(" - ")[1];
            Client client = UserManager.getClient(email);
            if (client != null) {
                showEditClientDialog(client, email);
            }
        } else {
            showError("Please select a client to edit.");
        }
    }
    // Updated dialog for editing clients
    private void showEditClientDialog(Client client, String originalEmail) {
        JTextField nameField = new JTextField(client.getName());
        JTextField emailField = new JTextField(client.getEmail());
        JPasswordField passwordField = new JPasswordField(client.getPassword());
        
        Object[] message = {
            "Name:", nameField,
            "Email:", emailField,
            "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(
            this, 
            message, 
            "Edit Client", 
            JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();

            // Validate inputs
            if (newName.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty()) {
                showError("All fields are required!");
                return;
            }

            if (!isValidEmail(newEmail)) {
                showError("Invalid email format!");
                return;
            }

            try {
                // Update in database
                String sql = "UPDATE users SET name = ?, email = ?, password = ? WHERE email = ?";
                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, newName);
                    pstmt.setString(2, newEmail);
                    pstmt.setString(3, newPassword);
                    pstmt.setString(4, originalEmail);
                    pstmt.executeUpdate();
                }

                // Refresh the list
                updateClientList();
                JOptionPane.showMessageDialog(this, "Client updated successfully!");

            } catch (SQLException e) {
                showError("Error updating client: " + e.getMessage());
            }
        }
    }
    private void showAddClientDialog() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
            "Enter Client Name:", nameField,
            "Enter Client Email:", emailField,
            "Enter Client Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Client", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (!isValidEmail(email)) {
                    showError("Invalid email format!");
                    return;
                }
            
            // Check if the client already exists
           if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    try {
                        if (!UserManager.clientExists(email)) {
                            UserManager.createClient(name, email, password);
                            updateClientList();
                        } else {
                            showError("Client already exists with this email!");
                        }
                    } catch (Exception e) {
                        showError("Error creating client: " + e.getMessage());
                    }
                } else {
                    showError("All fields are required!");
                }
        }
    }

    private void showClientDetails(Client client) {
        JTextField nameField = new JTextField(client.getName());
        JTextField emailField = new JTextField(client.getEmail());
        JPasswordField passwordField = new JPasswordField(client.getPassword());
        Object[] message = {
            "Enter Client Name:", nameField,
            "Enter Client Email:", emailField,
            "Enter Client Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Client", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Update the client details
            client.setName(name);
            client.setEmail(email);
            client.setPassword(password);
            updateClientList();
        }
    }

    // Method to delete the selected client
    private void deleteClient() {
        int selectedIndex = clientList.getSelectedIndex();
        if (selectedIndex >= 0) {
            // Get the email of the selected client
            String email = clientListModel.get(selectedIndex).split(" - ")[1];

            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this client?", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Remove the client from the UserManager
               try {
                        UserManager.deleteClient(email);
                        updateClientList();
                    } catch (Exception e) {
                        showError("Error deleting client: " + e.getMessage());
                    } // Update the client list after deletion
            }
        } else {
                showError("Please select a client to delete.");
        }
    }
      private boolean isValidEmail(String email) {
            String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            return email.matches(regex);
        }
}

    // Inner class for Game Management (Same game management code from your AdminCode)
   // Inner class for Game Management (updated to use GameManager)
class GameManagementPanel extends JFrame {
    private static final long serialVersionUID = 1L;
    private DefaultListModel<String> gameListModel;
    private JList<String> gameList;
    private List<Game> currentGames;
    private Map<String, Integer> gameNameToIdMap;

    public GameManagementPanel() {
        setTitle("Game Management Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Initialize components
        gameListModel = new DefaultListModel<>();
        gameList = new JList<>(gameListModel);
        currentGames = new ArrayList<>();
        gameNameToIdMap = new HashMap<>();

        // Build UI
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton addGameButton = createStyledButton("Add Game", new Color(70, 130, 180));
        addGameButton.addActionListener(e -> showAddGameDialog());

        gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gameList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = gameList.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        showGameDetails(currentGames.get(selectedIndex), selectedIndex);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(gameList);
        mainPanel.add(addGameButton, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        updateGameList();
        setVisible(true);
    }

    private void updateGameList() {
        gameListModel.clear();
        currentGames.clear();
        gameNameToIdMap.clear();

        List<Game> games = GameManager.getAllGames();
        currentGames.addAll(games);

        for (Game game : games) {
            gameListModel.addElement(game.getName() + " - $" + game.getPrice());
        }
    }

    private void showAddGameDialog() {
        showGameDetails(new Game("", "", 0, ""), -1);
    }

    private void showGameDetails(Game game, int listIndex) {
        JDialog dialog = new JDialog(this, listIndex >= 0 ? "Edit Game" : "Add Game", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);

        // Input fields
        JTextField nameField = new JTextField(game.getName());
        JTextField priceField = new JTextField(String.valueOf(game.getPrice()));
        JTextArea descriptionField = new JTextArea(game.getDescription());
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);

        // Image selection
        JButton imageButton = new JButton("Select Image");
        JLabel imagePreview = new JLabel();
        imagePreview.setPreferredSize(new Dimension(200, 200));
        if (!game.getImagePath().isEmpty()) {
            updateImagePreview(imagePreview, game.getImagePath());
        }

        String[] imagePath = {game.getImagePath()};
        imageButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                imagePath[0] = chooser.getSelectedFile().getAbsolutePath();
                updateImagePreview(imagePreview, imagePath[0]);
            }
        });

        // Layout
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(new JScrollPane(descriptionField));
        inputPanel.add(new JLabel("Image:"));
        inputPanel.add(imageButton);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(imagePreview, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        centerPanel.add(imagePanel, BorderLayout.EAST);

        // Buttons
        JButton saveButton = new JButton("Save");
        JButton deleteButton = new JButton("Delete");
        deleteButton.setVisible(listIndex >= 0);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);

        dialog.add(centerPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Save action
        saveButton.addActionListener(e -> {
            try {
                Game updatedGame = new Game(
                    
                    nameField.getText(),
                    descriptionField.getText(),
                    Double.parseDouble(priceField.getText()),
                    imagePath[0]
                );

                if (listIndex >= 0) {
                    // Update existing game
                    GameManager.updateGame(game.getId(), updatedGame); // Assuming IDs start at 1
                } else {
                    // Add new game
                    GameManager.addGame(updatedGame);
                }
                updateGameList();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid price format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Delete action
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                dialog,
                "Are you sure you want to delete this game?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                GameManager.deleteGame(game.getId()); // Assuming IDs start at 1
                updateGameList();
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    private void updateImagePreview(JLabel label, String path) {
        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(scaled));
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        return button;
    }
}
    }
