package market;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

class CartPanel extends JFrame {
    private DefaultListModel<String> cartModel;
    private JList<String> cartList;
    private JLabel totalLabel;
    private JButton removeButton;
    private JButton checkoutButton;
    private Cart cart;

    public CartPanel(Cart cart, String name, String email) {
        this.cart = cart;
        setTitle("Cart");
        setSize(700, 394);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // Initialize components
        cartModel = new DefaultListModel<>();
        updateCartModel();

        cartList = new JList<>(cartModel);
        cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(cartList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Cart Items"));

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Total Price Section
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalLabel = new JLabel("Total: $" + calculateTotal());
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalLabel);

        // Action Buttons Section
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        removeButton = new JButton("Remove Selected");
        removeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        removeButton.addActionListener(this::removeSelectedItem);
        
        checkoutButton = new JButton("Checkout");
        checkoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        checkoutButton.addActionListener(e -> proceedToCheckout(name, email));

        buttonPanel.add(removeButton);
        buttonPanel.add(checkoutButton);

        bottomPanel.add(totalPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        // Add components to frame
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateButtonStates();
        setVisible(true);
    }

    private void updateCartModel() {
        cartModel.clear();
        if (cart.isEmpty()) {
            cartModel.addElement("Your cart is empty!");
        } else {
            for (Game game : cart.getCartItems()) {
                cartModel.addElement(game.getName() + " - $" + game.getPrice());
            }
        }
    }

    private void updateButtonStates() {
        boolean cartEmpty = cart.isEmpty();
        removeButton.setEnabled(!cartEmpty);
        checkoutButton.setEnabled(!cartEmpty);
    }

    private double calculateTotal() {
        return cart.isEmpty() ? 0.0 : Math.round(cart.calculateTotal() * 100.0) / 100.0;
    }

    private void removeSelectedItem(ActionEvent e) {
        int selectedIndex = cartList.getSelectedIndex();
        if (selectedIndex >= 0 && !cart.isEmpty()) {
            String selectedValue = cartModel.get(selectedIndex);
            cart.removeGameByName(selectedValue.split(" - ")[0]);
            updateCartModel();
            totalLabel.setText("Total: $" + calculateTotal());
            updateButtonStates();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select an item to remove.",
                    "No Item Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void proceedToCheckout(String name, String email) {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Your cart is empty! Add items to proceed.",
                    "Empty Cart", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Thank you for your purchase! Total: $" + calculateTotal(),
                    "Checkout Complete", JOptionPane.INFORMATION_MESSAGE);
            ReceiptPanel.Bill(cart, calculateTotal(), name, email);
            cart.clear();
            updateCartModel();
            updateButtonStates();
            totalLabel.setText("Total: $0.00");
        }
    }
}