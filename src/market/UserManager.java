package market;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    public static void createClient(String name, String email, String password) {
        String sql = "INSERT INTO users(name, email, password) VALUES(?,?,?)";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating client: " + e.getMessage());
        }
    }

    public static boolean isAdmin(String email) {
        String sql = "SELECT is_admin FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getBoolean("is_admin");
        } catch (SQLException e) {
            System.out.println("Error checking admin status: " + e.getMessage());
            return false;
        }
    }

    public static Client getClient(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Client(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting client: " + e.getMessage());
        }
        return null;
    }

    public static boolean clientExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking client existence: " + e.getMessage());
            return false;
        }
    }

    public static void deleteClient(String email) {
        String sql = "DELETE FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting client: " + e.getMessage());
        }
    }

    public static Map<String, Client> getAllClients() {
        Map<String, Client> clients = new HashMap<>();
        String sql = "SELECT * FROM users WHERE is_admin = FALSE";
        
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Client client = new Client(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
                );
                clients.put(client.getEmail(), client);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all clients: " + e.getMessage());
        }
        return clients;
    }
}