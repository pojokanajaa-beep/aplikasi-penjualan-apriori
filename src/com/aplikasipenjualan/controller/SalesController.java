package com.aplikasipenjualan.controller;

import com.aplikasipenjualan.model.Product;
import com.aplikasipenjualan.model.Transaction;
import com.aplikasipenjualan.util.DataLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller untuk mengelola logika penjualan dan transaksi
 * 
 * @author Developer
 * @version 1.0
 */
public class SalesController {
    
    private List<Transaction> transactions;
    private List<Product> availableProducts;
    private DataLoader dataLoader;
    
    /**
     * Constructor
     */
    public SalesController() {
        this.transactions = new ArrayList<>();
        this.availableProducts = new ArrayList<>();
        this.dataLoader = new DataLoader();
        initializeDefaultProducts();
        loadTransactions();
    }
    
    /**
     * Inisialisasi produk default untuk demo
     */
    private void initializeDefaultProducts() {
        availableProducts.add(new Product(1, "Roti Tawar", "Makanan", 15000));
        availableProducts.add(new Product(2, "Susu UHT", "Minuman", 8000));
        availableProducts.add(new Product(3, "Telur Ayam", "Protein", 25000));
        availableProducts.add(new Product(4, "Minyak Goreng", "Bumbu", 18000));
        availableProducts.add(new Product(5, "Beras Premium", "Makanan Pokok", 45000));
        availableProducts.add(new Product(6, "Gula Pasir", "Bumbu", 12000));
        availableProducts.add(new Product(7, "Kopi Instan", "Minuman", 22000));
        availableProducts.add(new Product(8, "Teh Celup", "Minuman", 15000));
        availableProducts.add(new Product(9, "Sabun Mandi", "Kebersihan", 8500));
        availableProducts.add(new Product(10, "Pasta Gigi", "Kebersihan", 12500));
    }
    
    /**
     * Menambah transaksi baru
     * @param transaction Transaksi yang akan ditambahkan
     * @return true jika berhasil ditambahkan
     * @throws IllegalArgumentException jika transaksi tidak valid
     */
    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaksi tidak boleh null");
        }
        
        if (!transaction.isValid()) {
            throw new IllegalArgumentException("Transaksi tidak valid");
        }
        
        boolean added = transactions.add(transaction);
        if (added) {
            saveTransactions();
        }
        return added;
    }
    
    /**
     * Mendapatkan semua transaksi
     * @return Daftar semua transaksi
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }
    
    /**
     * Mendapatkan transaksi berdasarkan ID
     * @param transactionId ID transaksi
     * @return Transaksi jika ditemukan, null jika tidak
     */
    public Transaction getTransactionById(String transactionId) {
        return transactions.stream()
                          .filter(t -> t.getTransactionId().equals(transactionId))
                          .findFirst()
                          .orElse(null);
    }
    
    /**
     * Menghapus transaksi
     * @param transactionId ID transaksi yang akan dihapus
     * @return true jika berhasil dihapus
     */
    public boolean removeTransaction(String transactionId) {
        boolean removed = transactions.removeIf(t -> t.getTransactionId().equals(transactionId));
        if (removed) {
            saveTransactions();
        }
        return removed;
    }
    
    /**
     * Mendapatkan semua produk yang tersedia
     * @return Daftar produk yang tersedia
     */
    public List<Product> getAvailableProducts() {
        return new ArrayList<>(availableProducts);
    }
    
    /**
     * Menambah produk baru
     * @param product Produk yang akan ditambahkan
     * @return true jika berhasil ditambahkan
     * @throws IllegalArgumentException jika produk tidak valid
     */
    public boolean addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Produk tidak boleh null");
        }
        
        if (!product.isValid()) {
            throw new IllegalArgumentException("Produk tidak valid");
        }
        
        // Cek apakah ID sudah ada
        boolean idExists = availableProducts.stream()
                                          .anyMatch(p -> p.getId() == product.getId());
        if (idExists) {
            throw new IllegalArgumentException("ID produk sudah ada: " + product.getId());
        }
        
        return availableProducts.add(product);
    }
    
    /**
     * Mencari produk berdasarkan ID
     * @param productId ID produk
     * @return Produk jika ditemukan, null jika tidak
     */
    public Product getProductById(int productId) {
        return availableProducts.stream()
                               .filter(p -> p.getId() == productId)
                               .findFirst()
                               .orElse(null);
    }
    
    /**
     * Mencari produk berdasarkan nama (case insensitive)
     * @param nama Nama produk
     * @return Daftar produk yang cocok
     */
    public List<Product> searchProductsByName(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String searchTerm = nama.toLowerCase().trim();
        return availableProducts.stream()
                               .filter(p -> p.getNama().toLowerCase().contains(searchTerm))
                               .collect(Collectors.toList());
    }
    
    /**
     * Mendapatkan produk berdasarkan kategori
     * @param kategori Kategori produk
     * @return Daftar produk dalam kategori tersebut
     */
    public List<Product> getProductsByCategory(String kategori) {
        if (kategori == null || kategori.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return availableProducts.stream()
                               .filter(p -> p.getKategori().equalsIgnoreCase(kategori.trim()))
                               .collect(Collectors.toList());
    }
    
    /**
     * Mendapatkan semua kategori yang tersedia
     * @return Daftar kategori unik
     */
    public List<String> getAllCategories() {
        return availableProducts.stream()
                               .map(Product::getKategori)
                               .distinct()
                               .sorted()
                               .collect(Collectors.toList());
    }
    
    /**
     * Mendapatkan statistik penjualan
     * @return String berisi statistik
     */
    public String getSalesStatistics() {
        int totalTransactions = transactions.size();
        double totalRevenue = transactions.stream()
                                        .mapToDouble(Transaction::getTotalHarga)
                                        .sum();
        
        int totalItems = transactions.stream()
                                   .mapToInt(Transaction::getItemCount)
                                   .sum();
        
        double averageTransaction = totalTransactions > 0 ? totalRevenue / totalTransactions : 0;
        
        return String.format(
            "Statistik Penjualan:\n" +
            "Total Transaksi: %d\n" +
            "Total Pendapatan: Rp %.2f\n" +
            "Total Item Terjual: %d\n" +
            "Rata-rata per Transaksi: Rp %.2f",
            totalTransactions, totalRevenue, totalItems, averageTransaction
        );
    }
    
    /**
     * Menyimpan transaksi ke file
     */
    private void saveTransactions() {
        try {
            dataLoader.saveTransactions(transactions);
        } catch (Exception e) {
            System.err.println("Error menyimpan transaksi: " + e.getMessage());
        }
    }
    
    /**
     * Memuat transaksi dari file
     */
    private void loadTransactions() {
        try {
            List<Transaction> loadedTransactions = dataLoader.loadTransactions();
            if (loadedTransactions != null) {
                this.transactions = loadedTransactions;
            }
        } catch (Exception e) {
            System.err.println("Error memuat transaksi: " + e.getMessage());
            // Jika gagal memuat, gunakan list kosong
            this.transactions = new ArrayList<>();
        }
    }
    
    /**
     * Membersihkan semua data transaksi
     */
    public void clearAllTransactions() {
        transactions.clear();
        saveTransactions();
    }
    
    /**
     * Mendapatkan jumlah transaksi
     * @return Jumlah transaksi
     */
    public int getTransactionCount() {
        return transactions.size();
    }
}
