package com.aplikasipenjualan.view;

import com.aplikasipenjualan.controller.SalesController;
import com.aplikasipenjualan.model.Product;
import com.aplikasipenjualan.model.Transaction;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel untuk input data penjualan
 * 
 * @author Developer
 * @version 1.0
 */
public class InputPenjualanPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    // Components
    private SalesController salesController;
    private JComboBox<Product> productComboBox;
    private JSpinner quantitySpinner;
    private JButton addToCartButton;
    private JButton clearCartButton;
    private JButton submitTransactionButton;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JTable transactionHistoryTable;
    private DefaultTableModel transactionHistoryTableModel;
    private JLabel totalLabel;
    private JTextArea statisticsArea;
    
    // Data
    private List<Product> currentCart;
    private TransactionAddedListener transactionAddedListener;
    
    /**
     * Interface untuk listener ketika transaksi ditambahkan
     */
    public interface TransactionAddedListener {
        void onTransactionAdded();
    }
    
    /**
     * Constructor
     * @param salesController Controller untuk mengelola penjualan
     */
    public InputPenjualanPanel(SalesController salesController) {
        this.salesController = salesController;
        this.currentCart = new ArrayList<>();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshData();
    }
    
    /**
     * Inisialisasi komponen UI
     */
    private void initializeComponents() {
        // Product selection
        productComboBox = new JComboBox<>();
        productComboBox.setRenderer(new ProductComboBoxRenderer());
        
        // Quantity spinner
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        quantitySpinner.setPreferredSize(new Dimension(80, 25));
        
        // Buttons
        addToCartButton = new JButton("Tambah ke Keranjang");
        addToCartButton.setBackground(new Color(46, 204, 113));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        clearCartButton = new JButton("Kosongkan Keranjang");
        clearCartButton.setBackground(new Color(231, 76, 60));
        clearCartButton.setForeground(Color.WHITE);
        clearCartButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        submitTransactionButton = new JButton("Submit Transaksi");
        submitTransactionButton.setBackground(new Color(52, 152, 219));
        submitTransactionButton.setForeground(Color.WHITE);
        submitTransactionButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        // Cart table
        String[] cartColumns = {"Produk", "Kategori", "Harga", "Jumlah", "Subtotal"};
        cartTableModel = new DefaultTableModel(cartColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable = new JTable(cartTableModel);
        setupTable(cartTable);
        
        // Transaction history table
        String[] historyColumns = {"ID Transaksi", "Tanggal", "Jumlah Item", "Total Harga"};
        transactionHistoryTableModel = new DefaultTableModel(historyColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionHistoryTable = new JTable(transactionHistoryTableModel);
        setupTable(transactionHistoryTable);
        
        // Total label
        totalLabel = new JLabel("Total: Rp 0");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setForeground(new Color(41, 128, 185));
        
        // Statistics area
        statisticsArea = new JTextArea(5, 30);
        statisticsArea.setEditable(false);
        statisticsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statisticsArea.setBackground(new Color(236, 240, 241));
        statisticsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    /**
     * Setup table properties
     * @param table Table yang akan disetup
     */
    private void setupTable(JTable table) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(189, 195, 199));
        
        // Enable sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
    }
    
    /**
     * Setup layout panel
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Input panel (top)
        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.NORTH);
        
        // Main content (center)
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
        
        // Statistics panel (right)
        JPanel statisticsPanel = createStatisticsPanel();
        add(statisticsPanel, BorderLayout.EAST);
    }
    
    /**
     * Membuat input panel
     * @return JPanel input
     */
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 73, 94), 2),
            "Input Penjualan",
            0, 0,
            new Font("SansSerif", Font.BOLD, 14),
            new Color(52, 73, 94)
        ));
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Product label and combo
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Produk:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(productComboBox, gbc);
        
        // Quantity label and spinner
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Jumlah:"), gbc);
        
        gbc.gridx = 3;
        panel.add(quantitySpinner, gbc);
        
        // Add button
        gbc.gridx = 4;
        panel.add(addToCartButton, gbc);
        
        return panel;
    }
    
    /**
     * Membuat main panel
     * @return JPanel main
     */
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Cart panel
        JPanel cartPanel = createCartPanel();
        
        // History panel
        JPanel historyPanel = createHistoryPanel();
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cartPanel, historyPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.6);
        
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Membuat cart panel
     * @return JPanel cart
     */
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            "Keranjang Belanja",
            0, 0,
            new Font("SansSerif", Font.BOLD, 14),
            new Color(46, 204, 113)
        ));
        
        // Table
        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel with total and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Total
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(clearCartButton);
        buttonPanel.add(submitTransactionButton);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Membuat history panel
     * @return JPanel history
     */
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Riwayat Transaksi",
            0, 0,
            new Font("SansSerif", Font.BOLD, 14),
            new Color(52, 152, 219)
        ));
        
        JScrollPane scrollPane = new JScrollPane(transactionHistoryTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Membuat statistics panel
     * @return JPanel statistics
     */
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            "Statistik",
            0, 0,
            new Font("SansSerif", Font.BOLD, 14),
            new Color(155, 89, 182)
        ));
        panel.setPreferredSize(new Dimension(300, 0));
        
        JScrollPane scrollPane = new JScrollPane(statisticsArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        // Add to cart button
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });
        
        // Clear cart button
        clearCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearCart();
            }
        });
        
        // Submit transaction button
        submitTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitTransaction();
            }
        });
        
        // Double click on history table to view details
        transactionHistoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewTransactionDetails();
                }
            }
        });
    }
    
    /**
     * Menambah produk ke keranjang
     */
    private void addToCart() {
        try {
            Product selectedProduct = (Product) productComboBox.getSelectedItem();
            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(this, 
                    "Silakan pilih produk terlebih dahulu!", 
                    "Peringatan", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int quantity = (Integer) quantitySpinner.getValue();
            
            // Tambahkan produk sebanyak quantity ke cart
            for (int i = 0; i < quantity; i++) {
                currentCart.add(selectedProduct);
            }
            
            updateCartTable();
            updateTotal();
            
            // Reset quantity spinner
            quantitySpinner.setValue(1);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error menambah produk ke keranjang: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Mengosongkan keranjang
     */
    private void clearCart() {
        if (!currentCart.isEmpty()) {
            int option = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin mengosongkan keranjang?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
            
            if (option == JOptionPane.YES_OPTION) {
                currentCart.clear();
                updateCartTable();
                updateTotal();
            }
        }
    }
    
    /**
     * Submit transaksi
     */
    private void submitTransaction() {
        try {
            if (currentCart.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Keranjang kosong! Silakan tambahkan produk terlebih dahulu.", 
                    "Peringatan", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Buat transaksi baru
            Transaction transaction = new Transaction();
            for (Product product : currentCart) {
                transaction.addProduct(product);
            }
            
            // Simpan transaksi
            boolean success = salesController.addTransaction(transaction);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Transaksi berhasil disimpan!\nID: " + transaction.getTransactionId(), 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear cart dan refresh data
                currentCart.clear();
                updateCartTable();
                updateTotal();
                refreshTransactionHistory();
                updateStatistics();
                
                // Notify listener
                if (transactionAddedListener != null) {
                    transactionAddedListener.onTransactionAdded();
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Gagal menyimpan transaksi!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error menyimpan transaksi: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Melihat detail transaksi
     */
    private void viewTransactionDetails() {
        int selectedRow = transactionHistoryTable.getSelectedRow();
        if (selectedRow >= 0) {
            String transactionId = (String) transactionHistoryTableModel.getValueAt(selectedRow, 0);
            Transaction transaction = salesController.getTransactionById(transactionId);
            
            if (transaction != null) {
                showTransactionDetailsDialog(transaction);
            }
        }
    }
    
    /**
     * Menampilkan dialog detail transaksi
     * @param transaction Transaksi yang akan ditampilkan
     */
    private void showTransactionDetailsDialog(Transaction transaction) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                   "Detail Transaksi", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JTextArea detailArea = new JTextArea();
        detailArea.setEditable(false);
        detailArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder details = new StringBuilder();
        details.append("ID Transaksi: ").append(transaction.getTransactionId()).append("\n");
        details.append("Tanggal: ").append(transaction.getFormattedDate()).append("\n");
        details.append("Jumlah Item: ").append(transaction.getItemCount()).append("\n");
        details.append("Total Harga: Rp ").append(String.format("%.2f", transaction.getTotalHarga())).append("\n\n");
        details.append("Daftar Produk:\n");
        details.append("================\n");
        
        for (Product product : transaction.getProducts()) {
            details.append("- ").append(product.getNama())
                   .append(" (").append(product.getKategori()).append(")")
                   .append(" - Rp ").append(String.format("%.2f", product.getHarga()))
                   .append("\n");
        }
        
        detailArea.setText(details.toString());
        
        JScrollPane scrollPane = new JScrollPane(detailArea);
        dialog.add(scrollPane);
        dialog.setVisible(true);
    }
    
    /**
     * Update cart table
     */
    private void updateCartTable() {
        cartTableModel.setRowCount(0);
        
        // Group products by name for display
        java.util.Map<String, java.util.List<Product>> groupedProducts = new java.util.HashMap<>();
        for (Product product : currentCart) {
            String key = product.getNama();
            groupedProducts.computeIfAbsent(key, k -> new ArrayList<>()).add(product);
        }
        
        for (java.util.Map.Entry<String, java.util.List<Product>> entry : groupedProducts.entrySet()) {
            Product product = entry.getValue().get(0);
            int quantity = entry.getValue().size();
            double subtotal = product.getHarga() * quantity;
            
            cartTableModel.addRow(new Object[] {
                product.getNama(),
                product.getKategori(),
                String.format("Rp %.2f", product.getHarga()),
                quantity,
                String.format("Rp %.2f", subtotal)
            });
        }
    }
    
    /**
     * Update total harga
     */
    private void updateTotal() {
        double total = currentCart.stream()
                                 .mapToDouble(Product::getHarga)
                                 .sum();
        totalLabel.setText(String.format("Total: Rp %.2f", total));
    }
    
    /**
     * Refresh data
     */
    public void refreshData() {
        refreshProductComboBox();
        refreshTransactionHistory();
        updateStatistics();
    }
    
    /**
     * Refresh product combo box
     */
    private void refreshProductComboBox() {
        productComboBox.removeAllItems();
        List<Product> products = salesController.getAvailableProducts();
        for (Product product : products) {
            productComboBox.addItem(product);
        }
    }
    
    /**
     * Refresh transaction history
     */
    private void refreshTransactionHistory() {
        transactionHistoryTableModel.setRowCount(0);
        List<Transaction> transactions = salesController.getAllTransactions();
        
        for (Transaction transaction : transactions) {
            transactionHistoryTableModel.addRow(new Object[] {
                transaction.getTransactionId(),
                transaction.getFormattedDate(),
                transaction.getItemCount(),
                String.format("Rp %.2f", transaction.getTotalHarga())
            });
        }
    }
    
    /**
     * Update statistics
     */
    private void updateStatistics() {
        String statistics = salesController.getSalesStatistics();
        statisticsArea.setText(statistics);
    }
    
    /**
     * Set listener untuk transaksi yang ditambahkan
     * @param listener Listener
     */
    public void setTransactionAddedListener(TransactionAddedListener listener) {
        this.transactionAddedListener = listener;
    }
    
    /**
     * Custom renderer untuk product combo box
     */
    private class ProductComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Product) {
                Product product = (Product) value;
                setText(String.format("%s - %s (Rp %.2f)", 
                       product.getNama(), 
                       product.getKategori(), 
                       product.getHarga()));
            }
            
            return this;
        }
    }
}
