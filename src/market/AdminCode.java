package market;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Map;
import java.util.List;
import javax.swing.border.EmptyBorder;  // Add this import



public class AdminCode extends JFrame {

    private static final long serialVersionUID = 1L;

    public AdminCode() {
        setTitle("Admin Panel");
        setSize(400, 300); // Adjusted size for simplicity
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10)); // Two buttons stacked vertically

        // Button for User Management
        JButton userManagementButton = new JButton("User Management");
        userManagementButton.setFont(new Font("Arial", Font.BOLD, 16));
        userManagementButton.addActionListener(e -> {
            try {
                openUserManagementPanel();
            } catch (Exception ex) {
                showError("An error occurred while opening User Management: " + ex.getMessage());
            }
        });

        // Button for Game Management
        JButton gameManagementButton = new JButton("Game Management");
        gameManagementButton.setFont(new Font("Arial", Font.BOLD, 16));
        gameManagementButton.addActionListener(e -> {
            try {
                openGameManagementPanel();
            } catch (Exception ex) {
                showError("An error occurred while opening Game Management: " + ex.getMessage());
            }
        });

        // Add buttons to the panel
        buttonPanel.add(userManagementButton);
        buttonPanel.add(gameManagementButton);

        // Add the panel to the main frame
        add(buttonPanel, BorderLayout.CENTER);

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
   public class GameManagementPanel extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultListModel<String> gameListModel;
    private JList<String> gameList;

    public GameManagementPanel() {
        setTitle("Game Management Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Add padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add game button
        JButton addGameButton = createStyledButton("Add Game", new Color(70, 130, 180));
        addGameButton.addActionListener(e -> showAddGameDialog());

        // Game list
        gameListModel = new DefaultListModel<>();
        gameList = new JList<>(gameListModel);
        gameList.setFont(new Font("Arial", Font.PLAIN, 14));
        gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gameList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = gameList.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        showGameDetails(ShopAuthentication.games.get(selectedIndex), selectedIndex);
                    }
                }
            }
        });

        JScrollPane gameListScrollPane = new JScrollPane(gameList);
        gameListScrollPane.setPreferredSize(new Dimension(300, 500));

        mainPanel.add(addGameButton, BorderLayout.NORTH);
        mainPanel.add(gameListScrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        updateGameList();
        setVisible(true);
    }

    private void updateGameList() {
        gameListModel.clear();
        try {
            List<Game> games = ShopAuthentication.games;
            for (Game game : games) {
                gameListModel.addElement(game.getName() + " - $" + game.getPrice());
            }
        } catch (Exception e) {
            showError("Error updating game list: " + e.getMessage());
        }
    }

   private void showAddGameDialog() {
    // Initialize an empty game object
    Game newGame = new Game("", 0, "", "");

    // Pass the game to showGameDetails (but don't add it yet)
    showGameDetails(newGame, -1);

    // Save button will handle adding the game if the name is not empty
    JButton saveButton = new JButton("Save");
    saveButton.addActionListener(e -> {
        if (!newGame.getName().isEmpty()) {
            ShopAuthentication.games.add(newGame);
            updateGameList();
        }
    });
}


    private void showGameDetails(Game game, int gameIndex) {
        JTextField nameField = new JTextField(game.getName());
        JTextField priceField = new JTextField(String.valueOf(game.getPrice()));
        JTextArea descriptionField = new JTextArea(game.getDescription(), 5, 20);
        JButton imageButton = createStyledButton("Select Image", new Color(34, 139, 34));

        final String[] imagePath = {game.getImagePath()};
        JLabel imagePreview = new JLabel();
        imagePreview.setPreferredSize(new Dimension(200, 200));
        updateImagePreview(imagePreview, imagePath[0]);

        imageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                imagePath[0] = selectedFile.getAbsolutePath();
                updateImagePreview(imagePreview, imagePath[0]);
            }
        });

        // Layout for input fields
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.add(new JLabel("Game Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(new JScrollPane(descriptionField));
        inputPanel.add(new JLabel("Image:"));
        inputPanel.add(imageButton);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(imagePreview, BorderLayout.EAST);

        // Buttons
        JButton saveButton = createStyledButton("Save", new Color(70, 130, 180));
        JButton deleteButton = createStyledButton("Delete", new Color(220, 20, 60));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        if (gameIndex >= 0) buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(this, "Edit Game Details", true);
        dialog.setContentPane(mainPanel);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);

        saveButton.addActionListener(e -> {
            if (validateInputs(nameField, priceField, descriptionField, imagePath[0])) {
                try {
                    double price = Double.parseDouble(priceField.getText().trim());
                    game.setName(nameField.getText().trim());
                    game.setPrice(price);
                    game.setDescription(descriptionField.getText().trim());
                    game.setImagePath(imagePath[0]);

                    if (gameIndex == -1) {
                        ShopAuthentication.games.add(game);
                    }
                    updateGameList();
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    showError("Price must be a valid number!");
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this game?", 
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ShopAuthentication.games.remove(gameIndex);
                updateGameList();
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    private boolean validateInputs(JTextField nameField, JTextField priceField, JTextArea descriptionField, String imagePath) {
        if (nameField.getText().trim().isEmpty()) {
            showError("Game name cannot be empty!");
            return false;
        }
        try {
            Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException ex) {
            showError("Price must be a valid number!");
            return false;
        }
        if (descriptionField.getText().trim().isEmpty()) {
            showError("Description cannot be empty!");
            return false;
        }
        if (imagePath == null || imagePath.isEmpty()) {
            showError("An image must be selected!");
            return false;
        }
        return true;
    }

    private void updateImagePreview(JLabel imagePreview, String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            ImageIcon imageIcon = new ImageIcon(imagePath);
            Image scaledImage = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imagePreview.setIcon(new ImageIcon(scaledImage));
        } else {
            imagePreview.setIcon(null);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        return button;
    }
}
    }
