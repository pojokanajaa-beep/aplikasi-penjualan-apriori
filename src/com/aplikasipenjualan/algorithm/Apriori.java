package com.aplikasipenjualan.algorithm;

import com.aplikasipenjualan.model.Product;
import com.aplikasipenjualan.model.Transaction;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementasi algoritma Apriori untuk menemukan frequent itemsets
 * dan menghasilkan association rules
 * 
 * @author Developer
 * @version 1.0
 */
public class Apriori {
    
    private List<Transaction> transactions;
    private double minSupport;
    private double minConfidence;
    private List<List<FrequentItemSet>> allFrequentItemSets;
    private List<Rule> associationRules;
    
    /**
     * Constructor
     */
    public Apriori() {
        this.transactions = new ArrayList<>();
        this.allFrequentItemSets = new ArrayList<>();
        this.associationRules = new ArrayList<>();
    }
    
    /**
     * Constructor dengan parameter
     * @param transactions Daftar transaksi
     * @param minSupport Minimum support threshold (0.0 - 1.0)
     * @param minConfidence Minimum confidence threshold (0.0 - 1.0)
     */
    public Apriori(List<Transaction> transactions, double minSupport, double minConfidence) {
        this();
        setTransactions(transactions);
        setMinSupport(minSupport);
        setMinConfidence(minConfidence);
    }
    
    /**
     * Setter untuk transactions dengan validasi
     * @param transactions Daftar transaksi
     * @throws IllegalArgumentException jika transactions null atau kosong
     */
    public void setTransactions(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            throw new IllegalArgumentException("Daftar transaksi tidak boleh null atau kosong");
        }
        this.transactions = new ArrayList<>(transactions);
    }
    
    /**
     * Setter untuk minimum support dengan validasi
     * @param minSupport Minimum support (0.0 - 1.0)
     * @throws IllegalArgumentException jika minSupport di luar range
     */
    public void setMinSupport(double minSupport) {
        if (minSupport < 0.0 || minSupport > 1.0) {
            throw new IllegalArgumentException("Minimum support harus antara 0.0 dan 1.0");
        }
        this.minSupport = minSupport;
    }
    
    /**
     * Setter untuk minimum confidence dengan validasi
     * @param minConfidence Minimum confidence (0.0 - 1.0)
     * @throws IllegalArgumentException jika minConfidence di luar range
     */
    public void setMinConfidence(double minConfidence) {
        if (minConfidence < 0.0 || minConfidence > 1.0) {
            throw new IllegalArgumentException("Minimum confidence harus antara 0.0 dan 1.0");
        }
        this.minConfidence = minConfidence;
    }
    
    /**
     * Menjalankan algoritma Apriori lengkap
     * @return List aturan asosiasi yang ditemukan
     * @throws IllegalStateException jika parameter belum diset
     */
    public List<Rule> runApriori() {
        validateParameters();
        
        // Reset hasil sebelumnya
        allFrequentItemSets.clear();
        associationRules.clear();
        
        // Generate frequent itemsets
        generateFrequentItemsets();
        
        // Generate association rules
        generateAssociationRules();
        
        return new ArrayList<>(associationRules);
    }
    
    /**
     * Validasi parameter sebelum menjalankan algoritma
     * @throws IllegalStateException jika parameter tidak valid
     */
    private void validateParameters() {
        if (transactions == null || transactions.isEmpty()) {
            throw new IllegalStateException("Transaksi belum diset atau kosong");
        }
        if (minSupport < 0.0 || minSupport > 1.0) {
            throw new IllegalStateException("Minimum support tidak valid");
        }
        if (minConfidence < 0.0 || minConfidence > 1.0) {
            throw new IllegalStateException("Minimum confidence tidak valid");
        }
    }
    
    /**
     * Generate frequent itemsets menggunakan algoritma Apriori
     */
    private void generateFrequentItemsets() {
        // Generate 1-itemsets
        List<FrequentItemSet> frequentOneItemSets = generateFrequent1ItemSets();
        if (frequentOneItemSets.isEmpty()) {
            return;
        }
        allFrequentItemSets.add(frequentOneItemSets);
        
        // Generate k-itemsets (k > 1)
        List<FrequentItemSet> currentFrequentItemSets = frequentOneItemSets;
        int k = 2;
        
        while (!currentFrequentItemSets.isEmpty()) {
            List<FrequentItemSet> candidateItemSets = generateCandidateItemSets(currentFrequentItemSets, k);
            currentFrequentItemSets = pruneInfrequentItemSets(candidateItemSets);
            
            if (!currentFrequentItemSets.isEmpty()) {
                allFrequentItemSets.add(currentFrequentItemSets);
            }
            k++;
        }
    }
    
    /**
     * Generate frequent 1-itemsets
     * @return List frequent 1-itemsets
     */
    private List<FrequentItemSet> generateFrequent1ItemSets() {
        Map<Product, Integer> itemCounts = new HashMap<>();
        
        // Hitung frekuensi setiap produk
        for (Transaction transaction : transactions) {
            for (Product product : transaction.getProducts()) {
                itemCounts.put(product, itemCounts.getOrDefault(product, 0) + 1);
            }
        }
        
        // Filter berdasarkan minimum support
        int minSupportCount = (int) Math.ceil(minSupport * transactions.size());
        List<FrequentItemSet> frequentItemSets = new ArrayList<>();
        
        for (Map.Entry<Product, Integer> entry : itemCounts.entrySet()) {
            if (entry.getValue() >= minSupportCount) {
                FrequentItemSet itemSet = new FrequentItemSet(
                    entry.getKey(), 
                    entry.getValue(), 
                    transactions.size()
                );
                frequentItemSets.add(itemSet);
            }
        }
        
        return frequentItemSets;
    }
    
    /**
     * Generate candidate itemsets dari frequent itemsets sebelumnya
     * @param frequentItemSets Frequent itemsets dari iterasi sebelumnya
     * @param k Ukuran itemset yang akan di-generate
     * @return List candidate itemsets
     */
    private List<FrequentItemSet> generateCandidateItemSets(List<FrequentItemSet> frequentItemSets, int k) {
        List<FrequentItemSet> candidates = new ArrayList<>();
        
        for (int i = 0; i < frequentItemSets.size(); i++) {
            for (int j = i + 1; j < frequentItemSets.size(); j++) {
                FrequentItemSet itemSet1 = frequentItemSets.get(i);
                FrequentItemSet itemSet2 = frequentItemSets.get(j);
                
                FrequentItemSet candidate = itemSet1.joinWith(itemSet2);
                if (candidate != null && candidate.size() == k) {
                    // Prune berdasarkan Apriori property
                    if (hasInfrequentSubset(candidate, frequentItemSets)) {
                        continue;
                    }
                    candidates.add(candidate);
                }
            }
        }
        
        return candidates;
    }
    
    /**
     * Mengecek apakah candidate itemset memiliki subset yang tidak frequent
     * @param candidate Candidate itemset
     * @param frequentItemSets Frequent itemsets dari iterasi sebelumnya
     * @return true jika memiliki infrequent subset
     */
    private boolean hasInfrequentSubset(FrequentItemSet candidate, List<FrequentItemSet> frequentItemSets) {
        List<FrequentItemSet> subsets = candidate.getSubsets(candidate.size() - 1);
        
        for (FrequentItemSet subset : subsets) {
            boolean found = false;
            for (FrequentItemSet frequent : frequentItemSets) {
                if (frequent.equals(subset)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Prune candidate itemsets yang tidak memenuhi minimum support
     * @param candidateItemSets Candidate itemsets
     * @return List frequent itemsets
     */
    private List<FrequentItemSet> pruneInfrequentItemSets(List<FrequentItemSet> candidateItemSets) {
        List<FrequentItemSet> frequentItemSets = new ArrayList<>();
        int minSupportCount = (int) Math.ceil(minSupport * transactions.size());
        
        for (FrequentItemSet candidate : candidateItemSets) {
            int support = calculateSupport(candidate);
            if (support >= minSupportCount) {
                candidate.setSupport(support, transactions.size());
                frequentItemSets.add(candidate);
            }
        }
        
        return frequentItemSets;
    }
    
    /**
     * Menghitung support untuk itemset tertentu
     * @param itemSet Itemset yang akan dihitung supportnya
     * @return Nilai support absolut
     */
    private int calculateSupport(FrequentItemSet itemSet) {
        int count = 0;
        
        for (Transaction transaction : transactions) {
            if (transaction.getProducts().containsAll(itemSet.getItems())) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Generate association rules dari frequent itemsets
     */
    private void generateAssociationRules() {
        // Mulai dari 2-itemsets (minimal untuk membuat rule)
        for (int i = 1; i < allFrequentItemSets.size(); i++) {
            List<FrequentItemSet> frequentItemSets = allFrequentItemSets.get(i);
            
            for (FrequentItemSet itemSet : frequentItemSets) {
                generateRulesFromItemSet(itemSet);
            }
        }
        
        // Sort rules berdasarkan confidence (descending)
        associationRules.sort((r1, r2) -> Double.compare(r2.getConfidence(), r1.getConfidence()));
    }
    
    /**
     * Generate rules dari satu frequent itemset
     * @param itemSet Frequent itemset
     */
    private void generateRulesFromItemSet(FrequentItemSet itemSet) {
        Set<Product> items = itemSet.getItems();
        
        // Generate semua subset non-empty sebagai antecedent
        for (int subsetSize = 1; subsetSize < items.size(); subsetSize++) {
            List<FrequentItemSet> subsets = itemSet.getSubsets(subsetSize);
            
            for (FrequentItemSet subset : subsets) {
                Set<Product> antecedent = subset.getItems();
                Set<Product> consequent = new HashSet<>(items);
                consequent.removeAll(antecedent);
                
                if (!consequent.isEmpty()) {
                    double confidence = calculateConfidence(antecedent, consequent);
                    
                    if (confidence >= minConfidence) {
                        double support = itemSet.getSupportPercentage();
                        double lift = calculateLift(antecedent, consequent, support);
                        
                        Rule rule = new Rule(antecedent, consequent, confidence, support, lift);
                        associationRules.add(rule);
                    }
                }
            }
        }
    }
    
    /**
     * Menghitung confidence untuk rule
     * @param antecedent Antecedent items
     * @param consequent Consequent items
     * @return Nilai confidence
     */
    private double calculateConfidence(Set<Product> antecedent, Set<Product> consequent) {
        int antecedentSupport = 0;
        int ruleSupport = 0;
        
        for (Transaction transaction : transactions) {
            Set<Product> transactionItems = new HashSet<>(transaction.getProducts());
            
            if (transactionItems.containsAll(antecedent)) {
                antecedentSupport++;
                
                if (transactionItems.containsAll(consequent)) {
                    ruleSupport++;
                }
            }
        }
        
        return antecedentSupport > 0 ? (double) ruleSupport / antecedentSupport : 0.0;
    }
    
    /**
     * Menghitung lift untuk rule
     * @param antecedent Antecedent items
     * @param consequent Consequent items
     * @param ruleSupport Support dari rule lengkap
     * @return Nilai lift
     */
    private double calculateLift(Set<Product> antecedent, Set<Product> consequent, double ruleSupport) {
        int consequentSupport = 0;
        
        for (Transaction transaction : transactions) {
            if (transaction.getProducts().containsAll(consequent)) {
                consequentSupport++;
            }
        }
        
        double consequentSupportRatio = (double) consequentSupport / transactions.size();
        
        return consequentSupportRatio > 0 ? ruleSupport / consequentSupportRatio : 0.0;
    }
    
    /**
     * Getter untuk semua frequent itemsets
     * @return List semua frequent itemsets per level
     */
    public List<List<FrequentItemSet>> getAllFrequentItemSets() {
        return new ArrayList<>(allFrequentItemSets);
    }
    
    /**
     * Getter untuk association rules
     * @return List association rules
     */
    public List<Rule> getAssociationRules() {
        return new ArrayList<>(associationRules);
    }
    
    /**
     * Mendapatkan frequent itemsets dengan ukuran tertentu
     * @param size Ukuran itemset
     * @return List frequent itemsets dengan ukuran tersebut
     */
    public List<FrequentItemSet> getFrequentItemSetsBySize(int size) {
        if (size <= 0 || size > allFrequentItemSets.size()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(allFrequentItemSets.get(size - 1));
    }
    
    /**
     * Mendapatkan rules yang mengandung produk tertentu
     * @param product Produk yang dicari
     * @return List rules yang mengandung produk
     */
    public List<Rule> getRulesContainingProduct(Product product) {
        return associationRules.stream()
                              .filter(rule -> rule.containsProduct(product))
                              .collect(Collectors.toList());
    }
    
    /**
     * Mendapatkan statistik hasil analisis
     * @return String berisi statistik
     */
    public String getAnalysisStatistics() {
        int totalFrequentItemSets = allFrequentItemSets.stream()
                                                      .mapToInt(List::size)
                                                      .sum();
        
        return String.format(
            "Statistik Analisis Apriori:\n" +
            "Total Transaksi: %d\n" +
            "Minimum Support: %.2f%%\n" +
            "Minimum Confidence: %.2f%%\n" +
            "Total Frequent Itemsets: %d\n" +
            "Total Association Rules: %d\n" +
            "Level Itemsets: %d",
            transactions.size(),
            minSupport * 100,
            minConfidence * 100,
            totalFrequentItemSets,
            associationRules.size(),
            allFrequentItemSets.size()
        );
    }
}
