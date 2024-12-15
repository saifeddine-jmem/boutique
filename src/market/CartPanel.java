package market;


import java.awt.*;
import javax.swing.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jmem
 */
class CartPanel extends JFrame {
    private DefaultListModel<String> cartModel;
    private JList<String> cartList;
    private JLabel totalLabel;

    public CartPanel(Cart cart , String name) {
        setTitle("Cart");
        setSize(700, 394);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null); // Center the window

        // Initialize components
        cartModel = new DefaultListModel<>();
        for (Game game : cart.getCartItems()) {
            cartModel.addElement(game.getName() + " - $" + game.getPrice());
        }

        cartList = new JList<>(cartModel);
        cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(cartList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Cart Items"));

        // Bottom panel: Total price and action buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Total Price Section
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalLabel = new JLabel("Total: $" + calculateTotal(cart));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalLabel);

        // Action Buttons Section
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton removeButton = new JButton("Remove Selected");
        removeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        removeButton.addActionListener(e -> removeSelectedItem(cart));
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        checkoutButton.addActionListener(e -> proceedToCheckout(cart ,name));

        buttonPanel.add(removeButton);
        buttonPanel.add(checkoutButton);

        bottomPanel.add(totalPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        // Add components to frame
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private double calculateTotal(Cart cart) {
    double total = cart.getCartItems().stream().mapToDouble(Game::getPrice).sum();
    return Math.round(total * 100.0) / 100.0;
    }

    private void removeSelectedItem(Cart cart) {
        int selectedIndex = cartList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String selectedValue = cartModel.get(selectedIndex);
            cart.removeGameByName(selectedValue.split(" - ")[0]); // Assuming Cart has a removeGameByName method
            cartModel.remove(selectedIndex);
            totalLabel.setText("Total: $" + calculateTotal(cart));
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select an item to remove.",
                    "No Item Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void proceedToCheckout(Cart cart ,String name) {
        if (cart.getCartItems().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Your cart is empty! Add items to proceed.",
                    "Empty Cart", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Thank you for your purchase! Total: $" + calculateTotal(cart),
                    "Checkout Complete", JOptionPane.INFORMATION_MESSAGE);
            ReceiptPanel.Bill(cart , calculateTotal(cart), name);
            cart.clear(); // Assuming Cart has a clear method
            dispose(); // Close the cart window
        }
    }
}
