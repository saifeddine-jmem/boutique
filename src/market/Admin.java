/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package market;

import javax.swing.*;

/**
 *
 * @author jmem
 */
public class Admin extends Client {

    public Admin(String name, String email, String password) {
        super(name, email, password); // Inherit from Client class
    }

    // Admin-specific functionality: access the admin panel
    public void openAdminPanel() {
        // Logic to open the Admin Panel
        System.out.println("Admin " + getName() + " has accessed the Admin Panel.");
    }
}
