package com.aplikasipenjualan.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Kelas untuk merepresentasikan transaksi penjualan
 * 
 * @author Developer
 * @version 1.0
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String transactionId;
    private LocalDateTime tanggal;
    private List<Product> products;
    private double totalHarga;
    
    /**
     * Constructor default
     */
    public Transaction() {
        this.products = new ArrayList<>();
        this.tanggal = LocalDateTime.now();
        this.transactionId = generateTransactionId();
    }
    
    /**
     * Constructor dengan parameter
     * @param transactionId ID transaksi
     * @param tanggal Tanggal transaksi
     * @param products Daftar produk
     */
    public Transaction(String transactionId, LocalDateTime tanggal, List<Product> products) {
        this.transactionId = transactionId;
        this.tanggal = tanggal;
        this.products = products != null ? new ArrayList<>(products) : new ArrayList<>();
        calculateTotalHarga();
    }
    
    /**
     * Generate ID transaksi otomatis
     * @return ID transaksi unik
     */
    private String generateTransactionId() {
        return "TRX" + System.currentTimeMillis();
    }
    
    /**
     * Getter untuk transaction ID
     * @return ID transaksi
     */
    public String getTransactionId() {
        return transactionId;
    }
    
    /**
     * Setter untuk transaction ID
     * @param transactionId ID transaksi
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    /**
     * Getter untuk tanggal
     * @return Tanggal transaksi
     */
    public LocalDateTime getTanggal() {
        return tanggal;
    }
    
    /**
     * Setter untuk tanggal
     * @param tanggal Tanggal transaksi
     */
    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }
    
    /**
     * Getter untuk daftar produk
     * @return Daftar produk
     */
    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }
    
    /**
     * Setter untuk daftar produk
     * @param products Daftar produk
     */
    public void setProducts(List<Product> products) {
        this.products = products != null ? new ArrayList<>(products) : new ArrayList<>();
        calculateTotalHarga();
    }
    
    /**
     * Menambah produk ke transaksi
     * @param product Produk yang akan ditambahkan
     * @throws IllegalArgumentException jika produk null atau tidak valid
     */
    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Produk tidak boleh null");
        }
        if (!product.isValid()) {
            throw new IllegalArgumentException("Produk tidak valid");
        }
        this.products.add(product);
        calculateTotalHarga();
    }
    
    /**
     * Menghapus produk dari transaksi
     * @param product Produk yang akan dihapus
     * @return true jika berhasil dihapus
     */
    public boolean removeProduct(Product product) {
        boolean removed = this.products.remove(product);
        if (removed) {
            calculateTotalHarga();
        }
        return removed;
    }
    
    /**
     * Getter untuk total harga
     * @return Total harga transaksi
     */
    public double getTotalHarga() {
        return totalHarga;
    }
    
    /**
     * Menghitung total harga dari semua produk
     */
    private void calculateTotalHarga() {
        this.totalHarga = products.stream()
                                .mapToDouble(Product::getHarga)
                                .sum();
    }
    
    /**
     * Mendapatkan jumlah item dalam transaksi
     * @return Jumlah produk
     */
    public int getItemCount() {
        return products.size();
    }
    
    /**
     * Validasi transaksi
     * @return true jika transaksi valid
     */
    public boolean isValid() {
        return transactionId != null && !transactionId.trim().isEmpty() && 
               tanggal != null && 
               products != null && !products.isEmpty() &&
               products.stream().allMatch(Product::isValid);
    }
    
    /**
     * Mendapatkan tanggal dalam format string
     * @return Tanggal dalam format dd/MM/yyyy HH:mm:ss
     */
    public String getFormattedDate() {
        if (tanggal == null) return "";
        return tanggal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction that = (Transaction) obj;
        return Objects.equals(transactionId, that.transactionId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
    
    @Override
    public String toString() {
        return String.format("Transaction{id='%s', tanggal=%s, items=%d, total=%.2f}", 
                           transactionId, getFormattedDate(), getItemCount(), totalHarga);
    }
}
