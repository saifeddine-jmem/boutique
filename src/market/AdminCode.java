package market;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

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
        userManagementButton.addActionListener(e -> openUserManagementPanel());

        // Button for Game Management
        JButton gameManagementButton = new JButton("Game Management");
        gameManagementButton.setFont(new Font("Arial", Font.BOLD, 16));
        gameManagementButton.addActionListener(e -> openGameManagementPanel());

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
        UserManagementPanel userManagementPanel = new UserManagementPanel();
        userManagementPanel.setVisible(true);
    }

    // Method to open Game Management Panel (Existing game management functionality)
    private void openGameManagementPanel() {
        // Launch Game Management panel (Game management code from your previous AdminCode)
        GameManagementPanel gameManagementPanel = new GameManagementPanel();
        gameManagementPanel.setVisible(true);
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
        for (Client client : UserManager.getAllClients().values()) {
            clientListModel.addElement(client.getName() + " - " + client.getEmail());
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

            // Check if the client already exists
            if (!UserManager.clientExists(email)) {
                UserManager.createClient(name, email, password); // Create new client
                updateClientList();
            } else {
                JOptionPane.showMessageDialog(this, "Client already exists with this email!", "Error", JOptionPane.ERROR_MESSAGE);
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
                UserManager.deleteClient(email);
                updateClientList(); // Update the client list after deletion
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a client to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    // Inner class for Game Management (Same game management code from your AdminCode)
    class GameManagementPanel extends JFrame {

        private static final long serialVersionUID = 1L;
        private DefaultListModel<String> gameListModel;
        private JList<String> gameList;

        public GameManagementPanel() {
            setTitle("Game Management Panel");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());

            // Add game button
            JButton addGameButton = new JButton("Add Game");
            addGameButton.addActionListener(e -> showAddGameDialog());

            // Game list
            gameListModel = new DefaultListModel<>();
            gameList = new JList<>(gameListModel);
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
            add(addGameButton, BorderLayout.NORTH);
            add(gameListScrollPane, BorderLayout.CENTER);

            updateGameList();
            setVisible(true);
        }

        private void updateGameList() {
            gameListModel.clear();
            for (Game game : ShopAuthentication.games) {
                gameListModel.addElement(game.getName() + " - $" + game.getPrice());
            }
        }

        private void showAddGameDialog() {
            Game newGame = new Game("", 0, "", "");
            showGameDetails(newGame, -1);
            if (!newGame.getName().isEmpty()) {
                ShopAuthentication.games.add(newGame);
                updateGameList();
            }
        }

        private void showGameDetails(Game game, int gameIndex) {
            JTextField nameField = new JTextField(game.getName());
            JTextField priceField = new JTextField(String.valueOf(game.getPrice()));
            JTextArea descriptionField = new JTextArea(game.getDescription(), 5, 20);
            JButton imageButton = new JButton("Change Image");

            final String[] imagePath = {game.getImagePath()};

            // Create a label to display the current image
            JLabel imagePreview = new JLabel();
            updateImagePreview(imagePreview, imagePath[0]);

            // Image selection action
            imageButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int option = fileChooser.showOpenDialog(this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    imagePath[0] = selectedFile.getAbsolutePath();
                    updateImagePreview(imagePreview, imagePath[0]); // Update the preview
                    JOptionPane.showMessageDialog(this, "Image Updated: " + imagePath[0]);
                }
            });

            JPanel panel = new JPanel(new BorderLayout());

            JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 5, 5));
            fieldsPanel.add(new JLabel("Game Name:"));
            fieldsPanel.add(nameField);
            fieldsPanel.add(new JLabel("Price:"));
            fieldsPanel.add(priceField);
            fieldsPanel.add(new JLabel("Description:"));
            JScrollPane descriptionScrollPane = new JScrollPane(descriptionField);
            fieldsPanel.add(descriptionScrollPane);
            fieldsPanel.add(new JLabel("Image:"));
            fieldsPanel.add(imageButton);

            panel.add(fieldsPanel, BorderLayout.CENTER);
            panel.add(imagePreview, BorderLayout.EAST);

            JPanel buttonsPanel = new JPanel();
            JButton saveButton = new JButton("Save");
            JButton deleteButton = new JButton("Delete");

            buttonsPanel.add(saveButton);
            if (gameIndex >= 0) {
                buttonsPanel.add(deleteButton);
            }
            panel.add(buttonsPanel, BorderLayout.SOUTH);

            JDialog dialog = new JDialog(this, "Edit Game Details", true);
            dialog.setContentPane(panel);
            dialog.setSize(800, 600);
            dialog.setLocationRelativeTo(this);

            saveButton.addActionListener(e -> {
                if (validateInputs(nameField, priceField, descriptionField, imagePath[0])) {
                    try {
                        double price = Double.parseDouble(priceField.getText().trim());
                        game.setName(nameField.getText().trim());
                        game.setPrice(price);
                        game.setDescription(descriptionField.getText().trim());
                        game.setImagePath(imagePath[0]);

                        updateGameList();
                        dialog.dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid price format!", "Error",
                                JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Name is required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            try {
                Double.parseDouble(priceField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Price must be a valid number!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if (descriptionField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Description is required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if (imagePath == null || imagePath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "An image must be selected!", "Validation Error", JOptionPane.WARNING_MESSAGE);
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
    }
}
