package market;

import java.sql.*;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gameshop";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL,"root","");
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Create tables if they don't exist
            Statement stmt = conn.createStatement();
            
            // Create users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "name TEXT NOT NULL," +
                "email varchar(20) UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "is_admin BOOLEAN DEFAULT FALSE)");
            
            // Create games table
            stmt.execute("CREATE TABLE IF NOT EXISTS games (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "price REAL NOT NULL," +
                "image_path TEXT)");
                
            // Create carts table
            stmt.execute("CREATE TABLE IF NOT EXISTS carts (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "user_id INTEGER NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users(id))");
                
            // Create cart_items table
            stmt.execute("CREATE TABLE IF NOT EXISTS cart_items (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "cart_id INTEGER NOT NULL," +
                "game_id INTEGER NOT NULL," +
                "quantity INTEGER DEFAULT 1," +
                "FOREIGN KEY (cart_id) REFERENCES carts(id)," +
                "FOREIGN KEY (game_id) REFERENCES games(id))");
                
            // Create admin user if not exists
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE is_admin = TRUE");
if (rs.next() && rs.getInt(1) == 0) {  
    stmt.executeUpdate("INSERT INTO users (name, email, password, is_admin) VALUES " +
        "('Admin', 'admin@gmail.com', 'admin123', TRUE)");
}

// Insert sample games if empty
rs = stmt.executeQuery("SELECT COUNT(*) FROM games");
if (rs.next() && rs.getInt(1) == 0) {  
    stmt.executeUpdate("INSERT INTO games (name, price, description, image_path) VALUES " +
        "('The Legend of Zelda: Breath of the Wild', 59.99, 'An open-world action-adventure game...', 'images/zelda.jpg')");
    // Add more games if needed
    stmt.executeUpdate("INSERT INTO games (name, price, description, image_path) VALUES " +
        "('Elden Ring', 59.99, 'A challenging open-world RPG...', 'images/elden_ring.jpg')");
}
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}