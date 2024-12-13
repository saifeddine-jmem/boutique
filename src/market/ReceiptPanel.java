package market;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class ReceiptPanel extends JFrame {
    public ReceiptPanel(List<Game> purchasedItems, double totalCost) {
        setTitle("Receipt");
        setSize(700, 394);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null); // Center the window

        // Title Label
        JLabel titleLabel = new JLabel("Thank You for Your Purchase!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Items Section
        DefaultListModel<String> receiptModel = new DefaultListModel<>();
        for (Game game : purchasedItems) {
            receiptModel.addElement(game.getName() + " - $" + game.getPrice());
        }
        JList<String> receiptList = new JList<>(receiptModel);
        receiptList.setEnabled(false); // Disable selection
        JScrollPane scrollPane = new JScrollPane(receiptList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Purchased Items"));
        add(scrollPane, BorderLayout.CENTER);

        // Total Section
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel totalLabel = new JLabel("Total: $" + totalCost, SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalLabel, BorderLayout.EAST);

        add(totalPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
