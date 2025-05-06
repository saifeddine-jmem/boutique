package market;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cart {
    private int userId;
    private int cartId;
    
    public Cart(int userId) {
        this.userId = userId;
        initializeCart();
    }
    
    private void initializeCart() {
        String sql = "SELECT id FROM carts WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                cartId = rs.getInt("id");
            } else {
                // Create new cart
                String insertSql = "INSERT INTO carts(user_id) VALUES(?)";
                try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    insertPstmt.setInt(1, userId);
                    insertPstmt.executeUpdate();
                    
                    ResultSet generatedKeys = insertPstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        cartId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error initializing cart: " + e.getMessage());
        }
    }
    
    public void addGame(Game game) {
        // Check if game already exists in cart
        String checkSql = "SELECT id, quantity FROM cart_items WHERE cart_id = ? AND game_id = (SELECT id FROM games WHERE name = ?)";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
            checkPstmt.setInt(1, cartId);
            checkPstmt.setString(2, game.getName());
            ResultSet rs = checkPstmt.executeQuery();
            
            if (rs.next()) {
                // Update quantity
                int newQuantity = rs.getInt("quantity") + 1;
                String updateSql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
                try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                    updatePstmt.setInt(1, newQuantity);
                    updatePstmt.setInt(2, rs.getInt("id"));
                    updatePstmt.executeUpdate();
                }
            } else {
                // Add new item
                String insertSql = "INSERT INTO cart_items(cart_id, game_id) VALUES(?, (SELECT id FROM games WHERE name = ?))";
                try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                    insertPstmt.setInt(1, cartId);
                    insertPstmt.setString(2, game.getName());
                    insertPstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding game to cart: " + e.getMessage());
        }
    }
    
    public void removeGameByName(String gameName) {
        String sql = "DELETE FROM cart_items WHERE cart_id = ? AND game_id = (SELECT id FROM games WHERE name = ?)";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cartId);
            pstmt.setString(2, gameName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error removing game from cart: " + e.getMessage());
        }
    }
    
    public void clear() {
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cartId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        }
    }
    
   public List<Game> getCartItems() {
    List<Game> items = new ArrayList<>();
    String sql = "SELECT g.id, g.name, g.price, g.description, g.image_path, ci.quantity " +
                 "FROM cart_items ci JOIN games g ON ci.game_id = g.id " +
                 "WHERE ci.cart_id = ?";
    
    try (Connection conn = DatabaseHelper.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, cartId);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            Game game = new Game(
                rs.getInt("g.id"),  // Changed from "id" to "g.id"
                rs.getString("g.name"),
                rs.getDouble("g.price"),
                rs.getString("g.description"),
                rs.getString("g.image_path")
            );
            // Add quantity times
            for (int i = 0; i < rs.getInt("ci.quantity"); i++) {
                items.add(game);
            }
        }
    } catch (SQLException e) {
    System.err.println("Error getting cart items for cart ID " + cartId);
    e.printStackTrace();
    // Consider throwing a custom exception or returning empty list
    return Collections.emptyList();
}
    return items;
}
    
    public boolean isEmpty() {
        String sql = "SELECT COUNT(*) FROM cart_items WHERE cart_id = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cartId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking if cart is empty: " + e.getMessage());
        }
        return true;
    }
    
    public double calculateTotal() {
        String sql = "SELECT SUM(g.price * ci.quantity) AS total " +
                     "FROM cart_items ci JOIN games g ON ci.game_id = g.id " +
                     "WHERE ci.cart_id = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cartId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating cart total: " + e.getMessage());
        }
        return 0.0;
    }
    
    public void checkout(String name, String email) {
        try (Connection conn = DatabaseHelper.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);
            
            try {
                // 1. Create order record
                String orderSql = "INSERT INTO orders(user_id, total_price) VALUES(?, ?)";
                int orderId;
                
                try (PreparedStatement orderPstmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                    orderPstmt.setInt(1, userId);
                    orderPstmt.setDouble(2, calculateTotal());
                    orderPstmt.executeUpdate();
                    
                    ResultSet generatedKeys = orderPstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to create order, no ID obtained.");
                    }
                }
                
                // 2. Add order items
                String orderItemsSql = "INSERT INTO order_items(order_id, game_id, quantity, price_at_purchase) " +
                                      "SELECT ?, ci.game_id, ci.quantity, g.price " +
                                      "FROM cart_items ci JOIN games g ON ci.game_id = g.id " +
                                      "WHERE ci.cart_id = ?";
                
                try (PreparedStatement itemsPstmt = conn.prepareStatement(orderItemsSql)) {
                    itemsPstmt.setInt(1, orderId);
                    itemsPstmt.setInt(2, cartId);
                    itemsPstmt.executeUpdate();
                }
                
                // 3. Clear cart
                clear();
                
                // 4. Generate receipt
                ReceiptPanel.Bill(this, calculateTotal(), name, email);
                
                // Commit transaction
                conn.commit();
            } catch (SQLException e) {
                // Rollback transaction on error
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.out.println("Error during checkout: " + e.getMessage());
        }
    }
}