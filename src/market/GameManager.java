package market;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameManager {
    public static Map<Integer, Game> getAllGamesWithIds() {
        Map<Integer, Game> games = new LinkedHashMap<>(); // Preserves insertion order
        String sql = "SELECT * FROM games ORDER BY id";
        
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                games.put(rs.getInt("id"), new Game(
                        rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_path")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error getting games: " + e.getMessage());
        }
        return games;
    }
    public static Game getGameById(int id) {
        String sql = "SELECT * FROM games WHERE id = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Game(
                        rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_path")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting game: " + e.getMessage());
        }
        return null;
    }
    // Update game by database ID
    public static void updateGameById(Game game, int id) {
        String sql = "UPDATE games SET name = ?, price = ?, description = ?, image_path = ? WHERE id = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, game.getName());
            pstmt.setDouble(2, game.getPrice());
            pstmt.setString(3, game.getDescription());
            pstmt.setString(4, game.getImagePath());
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating game: " + e.getMessage());
        }
    }
    // Delete game by database ID
    public static void deleteGameById(int id) {
        String sql = "DELETE FROM games WHERE id = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting game: " + e.getMessage());
        }
    }

    public static boolean addGame(Game game) {
        String sql = "INSERT INTO games(name, price, description, image_path) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, game.getName());
            pstmt.setDouble(2, game.getPrice());
            pstmt.setString(3, game.getDescription());
            pstmt.setString(4, game.getImagePath());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding game: " + e.getMessage());
            return false;
        }
    }

    

    public static boolean deleteGame(int gameId) {
        String sql = "DELETE FROM games WHERE id=?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gameId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting game: " + e.getMessage());
            return false;
        }
    }
    public static Game getGameAtIndex(int index) {
        String sql = "SELECT * FROM games ORDER BY id LIMIT 1 OFFSET ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, index);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Game(
                     rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_path")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting game at index: " + e.getMessage());
        }
        return null;
    }
    public static boolean updateGame(int gameId, Game game) {
        String sql = "UPDATE games SET name=?, price=?, description=?, image_path=? WHERE id=?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, game.getName());
            pstmt.setDouble(2, game.getPrice());
            pstmt.setString(3, game.getDescription());
            pstmt.setString(4, game.getImagePath());
            pstmt.setInt(5, gameId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating game: " + e.getMessage());
            return false;
        }
    }
    public static List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM games ORDER BY id";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                games.add(new Game(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_path")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error getting games: " + e.getMessage());
        }
        return games;
    }
}