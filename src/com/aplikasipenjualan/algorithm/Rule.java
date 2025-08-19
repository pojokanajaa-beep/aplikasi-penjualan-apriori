package com.aplikasipenjualan.algorithm;

import com.aplikasipenjualan.model.Product;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Kelas untuk merepresentasikan aturan asosiasi (association rule)
 * dalam algoritma Apriori
 * 
 * @author Developer
 * @version 1.0
 */
public class Rule implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Set<Product> antecedent; // Bagian IF (kondisi)
    private Set<Product> consequent; // Bagian THEN (hasil)
    private double confidence;       // Tingkat kepercayaan
    private double support;          // Tingkat dukungan
    private double lift;             // Lift ratio
    
    /**
     * Constructor default
     */
    public Rule() {
        this.antecedent = new HashSet<>();
        this.consequent = new HashSet<>();
        this.confidence = 0.0;
        this.support = 0.0;
        this.lift = 0.0;
    }
    
    /**
     * Constructor dengan parameter
     * @param antecedent Set produk sebagai kondisi
     * @param consequent Set produk sebagai hasil
     * @param confidence Tingkat kepercayaan
     * @param support Tingkat dukungan
     */
    public Rule(Set<Product> antecedent, Set<Product> consequent, 
                double confidence, double support) {
        this.antecedent = antecedent != null ? new HashSet<>(antecedent) : new HashSet<>();
        this.consequent = consequent != null ? new HashSet<>(consequent) : new HashSet<>();
        this.confidence = confidence;
        this.support = support;
        this.lift = 0.0;
    }
    
    /**
     * Constructor lengkap dengan lift
     * @param antecedent Set produk sebagai kondisi
     * @param consequent Set produk sebagai hasil
     * @param confidence Tingkat kepercayaan
     * @param support Tingkat dukungan
     * @param lift Lift ratio
     */
    public Rule(Set<Product> antecedent, Set<Product> consequent, 
                double confidence, double support, double lift) {
        this.antecedent = antecedent != null ? new HashSet<>(antecedent) : new HashSet<>();
        this.consequent = consequent != null ? new HashSet<>(consequent) : new HashSet<>();
        this.confidence = confidence;
        this.support = support;
        this.lift = lift;
    }
    
    /**
     * Getter untuk antecedent
     * @return Set produk kondisi
     */
    public Set<Product> getAntecedent() {
        return new HashSet<>(antecedent);
    }
    
    /**
     * Setter untuk antecedent
     * @param antecedent Set produk kondisi
     */
    public void setAntecedent(Set<Product> antecedent) {
        this.antecedent = antecedent != null ? new HashSet<>(antecedent) : new HashSet<>();
    }
    
    /**
     * Getter untuk consequent
     * @return Set produk hasil
     */
    public Set<Product> getConsequent() {
        return new HashSet<>(consequent);
    }
    
    /**
     * Setter untuk consequent
     * @param consequent Set produk hasil
     */
    public void setConsequent(Set<Product> consequent) {
        this.consequent = consequent != null ? new HashSet<>(consequent) : new HashSet<>();
    }
    
    /**
     * Getter untuk confidence
     * @return Tingkat kepercayaan (0.0 - 1.0)
     */
    public double getConfidence() {
        return confidence;
    }
    
    /**
     * Setter untuk confidence
     * @param confidence Tingkat kepercayaan
     * @throws IllegalArgumentException jika confidence di luar range 0-1
     */
    public void setConfidence(double confidence) {
        if (confidence < 0.0 || confidence > 1.0) {
            throw new IllegalArgumentException("Confidence harus antara 0.0 dan 1.0");
        }
        this.confidence = confidence;
    }
    
    /**
     * Getter untuk support
     * @return Tingkat dukungan (0.0 - 1.0)
     */
    public double getSupport() {
        return support;
    }
    
    /**
     * Setter untuk support
     * @param support Tingkat dukungan
     * @throws IllegalArgumentException jika support di luar range 0-1
     */
    public void setSupport(double support) {
        if (support < 0.0 || support > 1.0) {
            throw new IllegalArgumentException("Support harus antara 0.0 dan 1.0");
        }
        this.support = support;
    }
    
    /**
     * Getter untuk lift
     * @return Lift ratio
     */
    public double getLift() {
        return lift;
    }
    
    /**
     * Setter untuk lift
     * @param lift Lift ratio
     */
    public void setLift(double lift) {
        this.lift = lift;
    }
    
    /**
     * Mendapatkan nama produk dalam antecedent
     * @return List nama produk kondisi
     */
    public List<String> getAntecedentNames() {
        return antecedent.stream()
                        .map(Product::getNama)
                        .sorted()
                        .collect(Collectors.toList());
    }
    
    /**
     * Mendapatkan nama produk dalam consequent
     * @return List nama produk hasil
     */
    public List<String> getConsequentNames() {
        return consequent.stream()
                        .map(Product::getNama)
                        .sorted()
                        .collect(Collectors.toList());
    }
    
    /**
     * Mendapatkan antecedent sebagai string
     * @return String berisi nama produk kondisi
     */
    public String getAntecedentAsString() {
        return String.join(", ", getAntecedentNames());
    }
    
    /**
     * Mendapatkan consequent sebagai string
     * @return String berisi nama produk hasil
     */
    public String getConsequentAsString() {
        return String.join(", ", getConsequentNames());
    }
    
    /**
     * Mendapatkan representasi aturan dalam format yang mudah dibaca
     * @return String format "IF {antecedent} THEN {consequent}"
     */
    public String getRuleAsString() {
        return String.format("IF {%s} THEN {%s}", 
                           getAntecedentAsString(), 
                           getConsequentAsString());
    }
    
    /**
     * Mendapatkan representasi aturan dengan metrik
     * @return String lengkap dengan confidence, support, dan lift
     */
    public String getDetailedRuleString() {
        return String.format("%s (Confidence: %.2f%%, Support: %.2f%%, Lift: %.2f)", 
                           getRuleAsString(), 
                           confidence * 100, 
                           support * 100, 
                           lift);
    }
    
    /**
     * Mengecek apakah aturan valid
     * @return true jika aturan valid
     */
    public boolean isValid() {
        return !antecedent.isEmpty() && 
               !consequent.isEmpty() && 
               Collections.disjoint(antecedent, consequent) && // Tidak ada overlap
               confidence >= 0.0 && confidence <= 1.0 &&
               support >= 0.0 && support <= 1.0;
    }
    
    /**
     * Mendapatkan ukuran total aturan (antecedent + consequent)
     * @return Jumlah total produk dalam aturan
     */
    public int getTotalSize() {
        return antecedent.size() + consequent.size();
    }
    
    /**
     * Mendapatkan ukuran antecedent
     * @return Jumlah produk dalam kondisi
     */
    public int getAntecedentSize() {
        return antecedent.size();
    }
    
    /**
     * Mendapatkan ukuran consequent
     * @return Jumlah produk dalam hasil
     */
    public int getConsequentSize() {
        return consequent.size();
    }
    
    /**
     * Mengecek apakah aturan mengandung produk tertentu
     * @param product Produk yang dicari
     * @return true jika produk ada dalam antecedent atau consequent
     */
    public boolean containsProduct(Product product) {
        return antecedent.contains(product) || consequent.contains(product);
    }
    
    /**
     * Mengecek apakah produk ada dalam antecedent
     * @param product Produk yang dicari
     * @return true jika produk ada dalam antecedent
     */
    public boolean hasInAntecedent(Product product) {
        return antecedent.contains(product);
    }
    
    /**
     * Mengecek apakah produk ada dalam consequent
     * @param product Produk yang dicari
     * @return true jika produk ada dalam consequent
     */
    public boolean hasInConsequent(Product product) {
        return consequent.contains(product);
    }
    
    /**
     * Mendapatkan kategori interpretasi berdasarkan nilai lift
     * @return String interpretasi lift
     */
    public String getLiftInterpretation() {
        if (lift > 1.0) {
            return "Positif (Saling mendukung)";
        } else if (lift < 1.0) {
            return "Negatif (Saling menghambat)";
        } else {
            return "Netral (Independen)";
        }
    }
    
    /**
     * Mendapatkan tingkat kekuatan aturan berdasarkan confidence
     * @return String tingkat kekuatan
     */
    public String getConfidenceLevel() {
        if (confidence >= 0.8) {
            return "Sangat Kuat";
        } else if (confidence >= 0.6) {
            return "Kuat";
        } else if (confidence >= 0.4) {
            return "Sedang";
        } else if (confidence >= 0.2) {
            return "Lemah";
        } else {
            return "Sangat Lemah";
        }
    }
    
    /**
     * Membandingkan aturan berdasarkan confidence
     * @param other Aturan lain
     * @return Nilai perbandingan
     */
    public int compareByConfidence(Rule other) {
        return Double.compare(other.confidence, this.confidence); // Descending
    }
    
    /**
     * Membandingkan aturan berdasarkan support
     * @param other Aturan lain
     * @return Nilai perbandingan
     */
    public int compareBySupport(Rule other) {
        return Double.compare(other.support, this.support); // Descending
    }
    
    /**
     * Membandingkan aturan berdasarkan lift
     * @param other Aturan lain
     * @return Nilai perbandingan
     */
    public int compareByLift(Rule other) {
        return Double.compare(other.lift, this.lift); // Descending
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rule rule = (Rule) obj;
        return Objects.equals(antecedent, rule.antecedent) && 
               Objects.equals(consequent, rule.consequent);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(antecedent, consequent);
    }
    
    @Override
    public String toString() {
        return getDetailedRuleString();
    }
}
