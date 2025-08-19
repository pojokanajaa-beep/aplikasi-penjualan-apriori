package com.aplikasipenjualan.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Kelas untuk merepresentasikan produk dalam sistem penjualan
 * 
 * @author Developer
 * @version 1.0
 */
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nama;
    private String kategori;
    private double harga;
    
    /**
     * Constructor default
     */
    public Product() {
    }
    
    /**
     * Constructor dengan parameter
     * @param id ID produk
     * @param nama Nama produk
     * @param kategori Kategori produk
     * @param harga Harga produk
     * @throws IllegalArgumentException jika harga <= 0
     */
    public Product(int id, String nama, String kategori, double harga) {
        this.id = id;
        this.nama = nama;
        this.kategori = kategori;
        setHarga(harga);
    }
    
    /**
     * Getter untuk ID
     * @return ID produk
     */
    public int getId() {
        return id;
    }
    
    /**
     * Setter untuk ID
     * @param id ID produk
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Getter untuk nama
     * @return Nama produk
     */
    public String getNama() {
        return nama;
    }
    
    /**
     * Setter untuk nama
     * @param nama Nama produk
     */
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    /**
     * Getter untuk kategori
     * @return Kategori produk
     */
    public String getKategori() {
        return kategori;
    }
    
    /**
     * Setter untuk kategori
     * @param kategori Kategori produk
     */
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
    
    /**
     * Getter untuk harga
     * @return Harga produk
     */
    public double getHarga() {
        return harga;
    }
    
    /**
     * Setter untuk harga dengan validasi
     * @param harga Harga produk
     * @throws IllegalArgumentException jika harga <= 0
     */
    public void setHarga(double harga) {
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga harus lebih dari 0");
        }
        this.harga = harga;
    }
    
    /**
     * Method untuk validasi produk
     * @return true jika produk valid
     */
    public boolean isValid() {
        return nama != null && !nama.trim().isEmpty() && 
               kategori != null && !kategori.trim().isEmpty() && 
               harga > 0;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return id == product.id && 
               Double.compare(product.harga, harga) == 0 && 
               Objects.equals(nama, product.nama) && 
               Objects.equals(kategori, product.kategori);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, nama, kategori, harga);
    }
    
    @Override
    public String toString() {
        return String.format("Product{id=%d, nama='%s', kategori='%s', harga=%.2f}", 
                           id, nama, kategori, harga);
    }
}
