package market;
import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private static Map<String, Client> clientStore = new HashMap<>();
    private static Admin admin;

    // Method to create and store a new client
    public static void createClient(String name, String email, String password) {
        if (!clientExists(email)) {
            Client client = new Client(name, email, password);
            clientStore.put(email, client);
            System.out.println("Client created successfully.");
        } else {
            System.out.println("Client with this email already exists.");
        }
    }

    // Method to create and return admin
    public static Admin createAdmin() {
        if (admin == null) {
            admin = new Admin("Admin", "admin@gmail.com", "admin123"); // Default admin credentials
        }
        return admin;
    }

    // Method to get the admin
    public static Admin getAdmin() {
        return admin;
    }

    // Method to check if the provided email is an admin's email
    public static boolean isAdmin(String email) {
        return admin != null && admin.getEmail().equals(email);
    }

    // Method to get a client by email
    public static Client getClient(String email) {
        return clientStore.get(email);
    }

    // Method to check if client exists by email
    public static boolean clientExists(String email) {
        return clientStore.containsKey(email);
    }

    // Method to remove a client by email
    public static void deleteClient(String email) {
        clientStore.remove(email);
    }

    // Method to get all clients (for admin panel)
    public static Map<String, Client> getAllClients() {
        return clientStore;
    }
}
