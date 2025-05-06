package market;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class UserManagementPanel extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultListModel<String> clientListModel;
    private JList<String> clientList;

    public UserManagementPanel() {
        setTitle("User Management Panel");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton addClientButton = new JButton("Add Client");
        addClientButton.addActionListener(e -> showAddClientDialog());

        JButton editButton = new JButton("Edit Client");
        editButton.addActionListener(e -> editSelectedClient());

        JButton deleteClientButton = new JButton("Delete Client");
        deleteClientButton.addActionListener(e -> deleteClient());

        clientListModel = new DefaultListModel<>();
        clientList = new JList<>(clientListModel);
        clientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane clientListScrollPane = new JScrollPane(clientList);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));
        buttonPanel.add(addClientButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteClientButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(clientListScrollPane, BorderLayout.CENTER);

        updateClientList();
        setVisible(true);
    }

    private void updateClientList() {
        clientListModel.clear();
        try {
            Map<String, Client> clients = UserManager.getAllClients();
            if (clients != null) {
                for (Client client : clients.values()) {
                    clientListModel.addElement(client.getName() + " - " + client.getEmail());
                }
            }
        } catch (Exception e) {
            showError("Error updating client list: " + e.getMessage());
        }
    }

    private void editSelectedClient() {
        int selectedIndex = clientList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String email = clientListModel.get(selectedIndex).split(" - ")[1];
            Client client = UserManager.getClient(email);
            if (client != null) {
                showEditClientDialog(client, email);
            }
        } else {
            showError("Please select a client to edit.");
        }
    }

    private void showEditClientDialog(Client client, String originalEmail) {
        JTextField nameField = new JTextField(client.getName());
        JTextField emailField = new JTextField(client.getEmail());
        JPasswordField passwordField = new JPasswordField(client.getPassword());

        Object[] message = {
            "Name:", nameField,
            "Email:", emailField,
            "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(
            this,
            message,
            "Edit Client",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();

            if (newName.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty()) {
                showError("All fields are required!");
                return;
            }

            try {
                String sql = "UPDATE users SET name = ?, email = ?, password = ? WHERE email = ?";
                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, newName);
                    pstmt.setString(2, newEmail);
                    pstmt.setString(3, newPassword);
                    pstmt.setString(4, originalEmail);
                    pstmt.executeUpdate();
                }

                updateClientList();
                JOptionPane.showMessageDialog(this, "Client updated successfully!");

            } catch (SQLException e) {
                showError("Error updating client: " + e.getMessage());
            }
        }
    }

    private void showAddClientDialog() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
            "Name:", nameField,
            "Email:", emailField,
            "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Client", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (!isValidEmail(email)) {
                showError("Invalid email format!");
                return;
            }

            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                try {
                    if (!UserManager.clientExists(email)) {
                        UserManager.createClient(name, email, password);
                        updateClientList();
                        JOptionPane.showMessageDialog(this, "Client added successfully!");
                    } else {
                        showError("Client already exists with this email!");
                    }
                } catch (Exception e) {
                    showError("Error creating client: " + e.getMessage());
                }
            } else {
                showError("All fields are required!");
            }
        }
    }

    private void deleteClient() {
        int selectedIndex = clientList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String email = clientListModel.get(selectedIndex).split(" - ")[1];
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this client?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    UserManager.deleteClient(email);
                    updateClientList();
                } catch (Exception e) {
                    showError("Error deleting client: " + e.getMessage());
                }
            }
        } else {
            showError("Please select a client to delete.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(regex);
    }
}
