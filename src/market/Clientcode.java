package market;

// ClientPanel Class
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class ClientPanel extends JFrame {

    private static final long serialVersionUID = 1L;
    private ArrayList<Game> filteredGames = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> gameList;
    private JTextArea descriptionArea;
    private JTextField searchField;
    private Cart cart;
    private JLabel imageLabel;

    public ClientPanel(Client client) {
        cart = client.getCart(); // Initialize the Cart

        setTitle("Client Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null); // Center the window
        setBackground(new Color(240, 240, 240));

        // Load games into the ArrayList
        

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

        topPanel.add(searchLabel, BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

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

        bottomPanel.add(addToCartButton);
        bottomPanel.add(viewCartButton);

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
            new CartPanel(cart);
        } else {
            JOptionPane.showMessageDialog(this, "Your cart is empty.");
        }
    }
}
