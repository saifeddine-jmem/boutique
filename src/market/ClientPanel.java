package market;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.Timer;

public class ClientPanel extends JFrame {
    private ArrayList<Game> allGames;
    private ArrayList<Game> filteredGames;
    private DefaultListModel<String> listModel;
    private JList<String> gameList;
    private JTextArea descriptionArea;
    private JTextField searchField;
    private JLabel imageLabel;
    private Cart cart;
    private String name;
    private String email;

    public ClientPanel(Client client) {
        this.cart = client.getCart();
        this.name = client.getName();
        this.email = client.getEmail();
        
        setTitle("Client Panel - " + name);
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
        
        // Initialize game lists
        allGames = new ArrayList<>(GameManager.getAllGames());
        filteredGames = new ArrayList<>(allGames);
        listModel = new DefaultListModel<>();
        
        createTopPanel();
        createCenterPanel();
        createBottomPanel();
        
        updateGameListDisplay();
        setVisible(true);
    }

    private void createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search components
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private Timer timer = new Timer(300, e -> searchGames());
            {
                timer.setRepeats(false);
            }
            
            public void changedUpdate(DocumentEvent e) { triggerSearch(); }
            public void insertUpdate(DocumentEvent e) { triggerSearch(); }
            public void removeUpdate(DocumentEvent e) { triggerSearch(); }
            
            private void triggerSearch() {
                timer.stop();
                timer.start();
            }
        });
        
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchGames());
        
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchField.setText("");
            searchGames();
        });
        
        JPanel searchButtonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        searchButtonPanel.add(searchButton);
        searchButtonPanel.add(clearButton);
        
        searchPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButtonPanel, BorderLayout.EAST);

        // Sort components
        String[] sortOptions = {
            "Sort by Price (Low to High)", 
            "Sort by Price (High to Low)", 
            "Sort A-Z", 
            "Sort Z-A"
        };
        
        JComboBox<String> sortMenu = new JComboBox<>(sortOptions);
        sortMenu.addActionListener(e -> 
            applySort((String) sortMenu.getSelectedItem()));
        
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        sortPanel.add(new JLabel("Sort by:"));
        sortPanel.add(sortMenu);

        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(sortPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
    }

    private void createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Game List Panel
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Available Games"));
        
        gameList = new JList<>(listModel);
        gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gameList.addListSelectionListener(e -> updateGameDetails());
        JScrollPane listScrollPane = new JScrollPane(gameList);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        // Game Details Panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Game Details"));
        
        imageLabel = new JLabel("", JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(250, 250));
        
        descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JScrollPane detailsScrollPane = new JScrollPane(descriptionArea);
        
        detailsPanel.add(imageLabel, BorderLayout.NORTH);
        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);

        centerPanel.add(listPanel);
        centerPanel.add(detailsPanel);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(e -> addToCart());
        
        JButton viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(e -> openCartPanel());
        
        JButton accessFolderButton = new JButton("Purchase History");
        accessFolderButton.addActionListener(e -> accessUserFolder());

        bottomPanel.add(addToCartButton);
        bottomPanel.add(viewCartButton);
        bottomPanel.add(accessFolderButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void updateGameListDisplay() {
        listModel.clear();
        for (Game game : filteredGames) {
            listModel.addElement(String.format("%s - $%.2f", game.getName(), game.getPrice()));
        }
        
        if (filteredGames.isEmpty()) {
            listModel.addElement("No games found");
        }
    }

    private void updateGameDetails() {
        int selectedIndex = gameList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < filteredGames.size()) {
            Game selectedGame = filteredGames.get(selectedIndex);
            descriptionArea.setText(selectedGame.getDescription());
            
            try {
                ImageIcon icon = new ImageIcon(selectedGame.getImagePath());
                Image scaled = icon.getImage().getScaledInstance(
                    imageLabel.getWidth(), 
                    imageLabel.getHeight(), 
                    Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
            } catch (Exception e) {
                imageLabel.setIcon(null);
            }
        }
    }

    private void searchGames() {
        String searchText = searchField.getText().toLowerCase().trim();
        filteredGames.clear();
        
        if (searchText.isEmpty()) {
            filteredGames.addAll(allGames);
        } else {
            for (Game game : allGames) {
                if (game.getName().toLowerCase().contains(searchText) || 
                    game.getDescription().toLowerCase().contains(searchText)) {
                    filteredGames.add(game);
                }
            }
        }
        
        updateGameListDisplay();
    }

    private void applySort(String sortOption) {
        switch (sortOption) {
            case "Sort by Price (Low to High)":
                filteredGames.sort(Comparator.comparingDouble(Game::getPrice));
                break;
            case "Sort by Price (High to Low)":
                filteredGames.sort(Comparator.comparingDouble(Game::getPrice).reversed());
                break;
            case "Sort A-Z":
                filteredGames.sort(Comparator.comparing(Game::getName));
                break;
            case "Sort Z-A":
                filteredGames.sort(Comparator.comparing(Game::getName).reversed());
                break;
        }
        updateGameListDisplay();
    }

    private void addToCart() {
        int selectedIndex = gameList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < filteredGames.size()) {
            Game selectedGame = filteredGames.get(selectedIndex);
            cart.addGame(selectedGame);
            JOptionPane.showMessageDialog(this, 
                selectedGame.getName() + " added to cart.",
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a game first.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void openCartPanel() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Your cart is empty.",
                "Empty Cart",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            new CartPanel(cart, name, email);
        }
    }

    private void accessUserFolder() {
        String folderPath = System.getProperty("user.home") + "/Desktop/clients/" + email;
        File folder = new File(folderPath);
        
        if (folder.exists() && folder.isDirectory()) {
            File[] receipts = folder.listFiles((dir, name) -> name.endsWith(".txt"));
            
            if (receipts != null && receipts.length > 0) {
                StringBuilder content = new StringBuilder();
                for (File receipt : receipts) {
                    content.append("=== ").append(receipt.getName()).append(" ===\n");
                    try (BufferedReader reader = new BufferedReader(new FileReader(receipt))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            content.append(line).append("\n");
                        }
                    } catch (IOException e) {
                        content.append("Error reading file\n");
                    }
                    content.append("\n");
                }
                
                JTextArea textArea = new JTextArea(content.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(600, 400));
                
                JOptionPane.showMessageDialog(this, scrollPane, 
                    "Purchase History", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No purchase history found.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "No purchase history available yet.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}