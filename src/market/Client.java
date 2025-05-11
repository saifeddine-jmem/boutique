package market;

import java.sql.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jmem
 */
public class Client {
    private String name;
    private String email;
    private String password;
    private Cart cart;

    public Client(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.cart = new Cart(getUserId()); // Initialize cart with user ID
    }
    
    private int getUserId() {
    String sql = "SELECT id FROM users WHERE email = ?";
    try (Connection conn = (Connection) DatabaseHelper.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, this.email);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1; // or throw an exception
}
    // Getter and Setter methods
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Cart getCart() {
        return cart;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}