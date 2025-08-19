package com.aplikasipenjualan.algorithm;

import com.aplikasipenjualan.model.Product;
import java.io.Serializable;
import java.util.*;

/**
 * Kelas untuk merepresentasikan frequent itemset dalam algoritma Apriori
 * 
 * @author Developer
 * @version 1.0
 */
public class FrequentItemSet implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Set<Product> items;
    private int support;
    private double supportPercentage;
    
    /**
     * Constructor default
     */
    public FrequentItemSet() {
        this.items = new HashSet<>();
        this.support = 0;
        this.supportPercentage = 0.0;
    }
    
    /**
     * Constructor dengan parameter
     * @param items Set produk dalam itemset
     * @param support Nilai support absolut
     * @param totalTransactions Total transaksi untuk menghitung persentase
     */
    public FrequentItemSet(Set<Product> items, int support, int totalTransactions) {
        this.items = new HashSet<>(items);
        this.support = support;
        this.supportPercentage = totalTransactions > 0 ? (double) support / totalTransactions : 0.0;
    }
    
    /**
     * Constructor dengan satu produk
     * @param product Produk tunggal
     * @param support Nilai support
     * @param totalTransactions Total transaksi
     */
    public FrequentItemSet(Product product, int support, int totalTransactions) {
        this.items = new HashSet<>();
        this.items.add(product);
        this.support = support;
        this.supportPercentage = totalTransactions > 0 ? (double) support / totalTransactions : 0.0;
    }
    
    /**
     * Getter untuk items
     * @return Set produk dalam itemset
     */
    public Set<Product> getItems() {
        return new HashSet<>(items);
    }
    
    /**
     * Setter untuk items
     * @param items Set produk
     */
    public void setItems(Set<Product> items) {
        this.items = items != null ? new HashSet<>(items) : new HashSet<>();
    }
    
    /**
     * Menambah produk ke itemset
     * @param product Produk yang akan ditambahkan
     */
    public void addItem(Product product) {
        if (product != null) {
            this.items.add(product);
        }
    }
    
    /**
     * Menghapus produk dari itemset
     * @param product Produk yang akan dihapus
     * @return true jika berhasil dihapus
     */
    public boolean removeItem(Product product) {
        return this.items.remove(product);
    }
    
    /**
     * Getter untuk support
     * @return Nilai support absolut
     */
    public int getSupport() {
        return support;
    }
    
    /**
     * Setter untuk support
     * @param support Nilai support
     * @param totalTransactions Total transaksi untuk menghitung persentase
     */
    public void setSupport(int support, int totalTransactions) {
        this.support = support;
        this.supportPercentage = totalTransactions > 0 ? (double) support / totalTransactions : 0.0;
    }
    
    /**
     * Getter untuk support percentage
     * @return Persentase support (0.0 - 1.0)
     */
    public double getSupportPercentage() {
        return supportPercentage;
    }
    
    /**
     * Mendapatkan ukuran itemset
     * @return Jumlah item dalam itemset
     */
    public int size() {
        return items.size();
    }
    
    /**
     * Mengecek apakah itemset kosong
     * @return true jika kosong
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    /**
     * Mengecek apakah itemset mengandung produk tertentu
     * @param product Produk yang dicari
     * @return true jika mengandung produk
     */
    public boolean contains(Product product) {
        return items.contains(product);
    }
    
    /**
     * Mengecek apakah itemset mengandung semua produk dari itemset lain
     * @param other Itemset lain
     * @return true jika mengandung semua item
     */
    public boolean containsAll(FrequentItemSet other) {
        return other != null && this.items.containsAll(other.items);
    }
    
    /**
     * Membuat union dengan itemset lain
     * @param other Itemset lain
     * @return Itemset baru hasil union
     */
    public FrequentItemSet union(FrequentItemSet other) {
        if (other == null) {
            return new FrequentItemSet(this.items, this.support, 0);
        }
        
        Set<Product> unionItems = new HashSet<>(this.items);
        unionItems.addAll(other.items);
        
        return new FrequentItemSet(unionItems, 0, 0);
    }
    
    /**
     * Membuat intersection dengan itemset lain
     * @param other Itemset lain
     * @return Itemset baru hasil intersection
     */
    public FrequentItemSet intersection(FrequentItemSet other) {
        if (other == null) {
            return new FrequentItemSet();
        }
        
        Set<Product> intersectionItems = new HashSet<>(this.items);
        intersectionItems.retainAll(other.items);
        
        return new FrequentItemSet(intersectionItems, 0, 0);
    }
    
    /**
     * Mendapatkan daftar nama produk dalam itemset
     * @return List nama produk
     */
    public List<String> getItemNames() {
        return items.stream()
                   .map(Product::getNama)
                   .sorted()
                   .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Mendapatkan string representasi itemset untuk display
     * @return String berisi nama-nama produk
     */
    public String getItemsAsString() {
        return String.join(", ", getItemNames());
    }
    
    /**
     * Mengecek apakah itemset dapat digabung dengan itemset lain
     * untuk membentuk kandidat itemset yang lebih besar
     * @param other Itemset lain
     * @return true jika dapat digabung
     */
    public boolean canJoinWith(FrequentItemSet other) {
        if (other == null || this.size() != other.size()) {
            return false;
        }
        
        // Untuk join, itemset harus berbeda hanya pada satu item
        Set<Product> thisItems = new HashSet<>(this.items);
        Set<Product> otherItems = new HashSet<>(other.items);
        
        thisItems.removeAll(other.items);
        otherItems.removeAll(this.items);
        
        return thisItems.size() == 1 && otherItems.size() == 1;
    }
    
    /**
     * Membuat kandidat itemset dengan menggabungkan dua itemset
     * @param other Itemset lain
     * @return Kandidat itemset baru
     */
    public FrequentItemSet joinWith(FrequentItemSet other) {
        if (!canJoinWith(other)) {
            return null;
        }
        
        Set<Product> candidateItems = new HashSet<>(this.items);
        candidateItems.addAll(other.items);
        
        return new FrequentItemSet(candidateItems, 0, 0);
    }
    
    /**
     * Mendapatkan semua subset dengan ukuran tertentu
     * @param subsetSize Ukuran subset yang diinginkan
     * @return List subset
     */
    public List<FrequentItemSet> getSubsets(int subsetSize) {
        List<FrequentItemSet> subsets = new ArrayList<>();
        
        if (subsetSize <= 0 || subsetSize >= this.size()) {
            return subsets;
        }
        
        List<Product> itemList = new ArrayList<>(items);
        generateSubsets(itemList, subsetSize, 0, new ArrayList<>(), subsets);
        
        return subsets;
    }
    
    /**
     * Helper method untuk generate subsets secara rekursif
     */
    private void generateSubsets(List<Product> items, int subsetSize, int start, 
                               List<Product> current, List<FrequentItemSet> result) {
        if (current.size() == subsetSize) {
            result.add(new FrequentItemSet(new HashSet<>(current), 0, 0));
            return;
        }
        
        for (int i = start; i < items.size(); i++) {
            current.add(items.get(i));
            generateSubsets(items, subsetSize, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FrequentItemSet that = (FrequentItemSet) obj;
        return Objects.equals(items, that.items);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
    
    @Override
    public String toString() {
        return String.format("FrequentItemSet{items=[%s], support=%d, supportPct=%.2f%%}", 
                           getItemsAsString(), support, supportPercentage * 100);
    }
}
