package market;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

class ClientPanel extends JFrame {

    private static final long serialVersionUID = 1L;
    private ArrayList<Game> filteredGames = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> gameList;
    private JTextArea descriptionArea;
    private JTextField searchField;
    private Cart cart;
    private JLabel imageLabel;
    String name;
    String email;

    public ClientPanel(Client client) {
        cart = client.getCart(); // Initialize the Cart
        name = client.getName();
        email = client.getEmail();
        setTitle("Client Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null); // Center the window
        setBackground(new Color(240, 240, 240));

        // Create UI components
        createTopPanel();
        createCenterPanel();
        createBottomPanel();

        // Populate the game list
        setupGameList(ShopAuthentication.games);

        setVisible(true);
    }

    private void createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.addActionListener(e -> searchGames());

        JLabel sortLabel = new JLabel("Sort:");
        sortLabel.setFont(new Font("Arial", Font.BOLD, 14));

        String[] sortOptions = {"Sort by Price (Ascending)", "Sort by Price (Descending)", "Sort by Alphabet (Ascending)", "Sort by Alphabet (Descending)"};
        JComboBox<String> sortMenu = new JComboBox<>(sortOptions);
        sortMenu.setFont(new Font("Arial", Font.BOLD, 14));
        sortMenu.addActionListener(e -> applySort((String) sortMenu.getSelectedItem()));

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        sortPanel.add(sortLabel);
        sortPanel.add(sortMenu);

        topPanel.add(searchLabel, BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);
        topPanel.add(sortPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
    }

    private void createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Game List Panel
        JPanel listPanel = new JPanel(new BorderLayout(10, 10));
        listPanel.setBorder(BorderFactory.createTitledBorder("Available Games"));
        gameList = new JList<>(listModel);
        gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gameList.addListSelectionListener(e -> updateDescription());
        JScrollPane listScrollPane = new JScrollPane(gameList);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        // Description Panel
        JPanel descriptionPanel = new JPanel(new BorderLayout(10, 10));
        descriptionPanel.setBorder(BorderFactory.createTitledBorder("Game Details"));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        descriptionPanel.add(imageLabel, BorderLayout.NORTH);

        descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);

        centerPanel.add(listPanel);
        centerPanel.add(descriptionPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setFont(new Font("Arial", Font.BOLD, 14));
        addToCartButton.addActionListener(e -> addToCart());

        JButton viewCartButton = new JButton("View Cart");
        viewCartButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewCartButton.addActionListener(e -> openCartPanel());
        
        JButton accessFolderButton = new JButton("Access User Folder");
        accessFolderButton.setFont(new Font("Arial", Font.BOLD, 14));
        accessFolderButton.addActionListener(e -> accessUserFolder());


        bottomPanel.add(addToCartButton);
        bottomPanel.add(viewCartButton);
                bottomPanel.add(accessFolderButton);


        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupGameList(ArrayList<Game> gameListSource) {
        listModel.clear();
        filteredGames.clear();

        for (Game game : gameListSource) {
            listModel.addElement(game.getName() + " - $" + game.getPrice());
            filteredGames.add(game); // Update filtered list to match displayed games
        }
    }

    private void updateDescription() {
        int selectedIndex = gameList.getSelectedIndex();
        if (selectedIndex >= 0) {
            Game selectedGame = filteredGames.get(selectedIndex); // Use filteredGames
            descriptionArea.setText(selectedGame.getDescription());
            ImageIcon gameImage = new ImageIcon(selectedGame.getImagePath());
            Image scaledImage = gameImage.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        }
    }

    private void searchGames() {
        String searchText = searchField.getText().toLowerCase();
        listModel.clear();
        filteredGames.clear(); // Ensure this is cleared before adding new results

        for (Game game : ShopAuthentication.games) {
            if (game.getName().toLowerCase().contains(searchText)) {
                listModel.addElement(game.getName() + " - $" + game.getPrice());
                filteredGames.add(game); // Add matching games to filteredGames
            }
        }

        if (listModel.isEmpty()) {
            listModel.addElement("No games found.");
        }
    }

    private void applySort(String sortOption) {
        switch (sortOption) {
            case "Sort by Price (Ascending)":
                filteredGames.sort(Comparator.comparingDouble(Game::getPrice));
                break;
            case "Sort by Price (Descending)":
                filteredGames.sort(Comparator.comparingDouble(Game::getPrice).reversed());
                break;
            case "Sort by Alphabet (Ascending)":
                filteredGames.sort(Comparator.comparing(Game::getName));
                break;
            case "Sort by Alphabet (Descending)":
                filteredGames.sort(Comparator.comparing(Game::getName).reversed());
                break;
        }
        updateGameListDisplay();
    }

    private void updateGameListDisplay() {
        listModel.clear();
        for (Game game : filteredGames) {
            listModel.addElement(game.getName() + " - $" + game.getPrice());
        }
    }

    private void addToCart() {
        int selectedIndex = gameList.getSelectedIndex();
        if (selectedIndex >= 0) {
            Game selectedGame = filteredGames.get(selectedIndex); // Use filteredGames
            cart.addGame(selectedGame);
            JOptionPane.showMessageDialog(this, selectedGame.getName() + " added to cart.");
        } else {
            JOptionPane.showMessageDialog(this, "No game selected.");
        }
    }

    private void openCartPanel() {
        if (!cart.isEmpty()) {
            new CartPanel(cart, name, email);
        } else {
            JOptionPane.showMessageDialog(this, "Your cart is empty.");
        }
    }
    private void accessUserFolder() {
        // Construct the path to the user's folder on the Desktop
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "clients" + File.separator + email;

        // Debug: Log the folder path
        System.out.println("Looking for folder: " + desktopPath);

        // Check if the folder exists
        File userFolder = new File(desktopPath);
        if (userFolder.exists() && userFolder.isDirectory()) {
            // Debug: Log folder found
            System.out.println("Folder found: " + desktopPath);

            // Get all text files in the folder
            File[] textFiles = userFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
            if (textFiles != null && textFiles.length > 0) {
                StringBuilder fileContents = new StringBuilder("Contents of files:\n\n");

                for (File file : textFiles) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        fileContents.append("File: ").append(file.getName()).append("\n");
                        String line;
                        while ((line = reader.readLine()) != null) {
                            fileContents.append(line).append("\n");
                        }
                        fileContents.append("\n");
                    } catch (IOException ex) {
                        ex.printStackTrace(); // Debug: Log the exception
                        JOptionPane.showMessageDialog(this, "Error reading file: " + file.getName(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                // Display the contents in a dialog
                JTextArea textArea = new JTextArea(fileContents.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(600, 400));
                JOptionPane.showMessageDialog(this, scrollPane, "User Folder Contents", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(this, "No text files found in the folder.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // Debug: Log folder not found
            System.out.println("Folder not found: " + desktopPath);
            JOptionPane.showMessageDialog(this, "History of purchse not available", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
