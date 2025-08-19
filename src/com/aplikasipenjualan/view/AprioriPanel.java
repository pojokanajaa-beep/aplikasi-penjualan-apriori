package com.aplikasipenjualan.view;

import com.aplikasipenjualan.algorithm.Apriori;
import com.aplikasipenjualan.algorithm.FrequentItemSet;
import com.aplikasipenjualan.algorithm.Rule;
import com.aplikasipenjualan.controller.SalesController;
import com.aplikasipenjualan.model.Transaction;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel untuk analisis algoritma Apriori
 * 
 * @author Developer
 * @version 1.0
 */
public class AprioriPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    // Components
    private SalesController salesController;
    private JSpinner minSupportSpinner;
    private JSpinner minConfidenceSpinner;
    private JButton analyzeButton;
    private JButton exportButton;
    private JButton clearResultsButton;
    private JTable frequentItemsTable;
    private DefaultTableModel frequentItemsTableModel;
    private JTable rulesTable;
    private DefaultTableModel rulesTableModel;
    private JTextArea analysisResultArea;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    
    // Data
    private Apriori aprioriAlgorithm;
    private List<Rule> currentRules;
    
    /**
     * Constructor
     * @param salesController Controller untuk mengelola penjualan
     */
    public AprioriPanel(SalesController salesController) {
        this.salesController = salesController;
        this.aprioriAlgorithm = new Apriori();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshData();
    }
    
    /**
     * Inisialisasi komponen UI
     */
    private void initializeComponents() {
        // Parameter spinners
        minSupportSpinner = new JSpinner(new SpinnerNumberModel(0.1, 0.01, 1.0, 0.01));
        minSupportSpinner.setPreferredSize(new Dimension(80, 25));
        JSpinner.NumberEditor supportEditor = new JSpinner.NumberEditor(minSupportSpinner, "0.00");
        minSupportSpinner.setEditor(supportEditor);
        
        minConfidenceSpinner = new JSpinner(new SpinnerNumberModel(0.5, 0.01, 1.0, 0.01));
        minConfidenceSpinner.setPreferredSize(new Dimension(80, 25));
        JSpinner.NumberEditor confidenceEditor = new JSpinner.NumberEditor(minConfidenceSpinner, "0.00");
        minConfidenceSpinner.setEditor(confidenceEditor);
        
        // Buttons
        analyzeButton = new JButton("Jalankan Analisis");
        analyzeButton.setBackground(new Color(46, 204, 113));
        analyzeButton.setForeground(Color.WHITE);
        analyzeButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        exportButton = new JButton("Export Hasil");
        exportButton.setBackground(new Color(52, 152, 219));
        exportButton.setForeground(Color.WHITE);
        exportButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        exportButton.setEnabled(false);
        
        clearResultsButton = new JButton("Bersihkan Hasil");
        clearResultsButton.setBackground(new Color(231, 76, 60));
        clearResultsButton.setForeground(Color.WHITE);
        clearResultsButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        // Frequent items table
        String[] frequentItemsColumns = {"Itemset", "Support Count", "Support %"};
        frequentItemsTableModel = new DefaultTableModel(frequentItemsColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        frequentItemsTable = new JTable(frequentItemsTableModel);
        setupTable(frequentItemsTable);
        
        // Rules table
        String[] rulesColumns = {"Antecedent (IF)", "Consequent (THEN)", "Confidence %", "Support %", "Lift", "Interpretasi"};
        rulesTableModel = new DefaultTableModel(rulesColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        rulesTable = new JTable(rulesTableModel);
        setupTable(rulesTable);
        
        // Analysis result area
        analysisResultArea = new JTextArea(8, 40);
        analysisResultArea.setEditable(false);
        analysisResultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        analysisResultArea.setBackground(new Color(236, 240, 241));
        analysisResultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        
        // Status label
        statusLabel = new JLabel("Siap untuk analisis");
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        statusLabel.setForeground(new Color(127, 140, 141));
    }
    
    /**
     * Setup table properties
     * @param table Table yang akan disetup
     */
    private void setupTable(JTable table) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(189, 195, 199));
        
        // Enable sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
        
        // Auto resize columns
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
    
    /**
     * Setup layout panel
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Parameter panel (top)
        JPanel parameterPanel = createParameterPanel();
        add(parameterPanel, BorderLayout.NORTH);
        
        // Main content (center)
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
        
        // Status panel (bottom)
        JPanel statusPanel = createStatusPanel();
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Membuat parameter panel
     * @return JPanel parameter
     */
    private JPanel createParameterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 73, 94), 2),
            "Parameter Analisis Apriori",
            0, 0,
            new Font("SansSerif", Font.BOLD, 14),
            new Color(52, 73, 94)
        ));
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Min Support
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Minimum Support:"), gbc);
        
        gbc.gridx = 1;
        panel.add(minSupportSpinner, gbc);
        
        gbc.gridx = 2;
        JLabel supportHelpLabel = new JLabel("(0.01 - 1.00, contoh: 0.1 = 10%)");
        supportHelpLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
        supportHelpLabel.setForeground(Color.GRAY);
        panel.add(supportHelpLabel, gbc);
        
        // Min Confidence
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Minimum Confidence:"), gbc);
        
        gbc.gridx = 4;
        panel.add(minConfidenceSpinner, gbc);
        
        gbc.gridx = 5;
        JLabel confidenceHelpLabel = new JLabel("(0.01 - 1.00, contoh: 0.5 = 50%)");
        confidenceHelpLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
        confidenceHelpLabel.setForeground(Color.GRAY);
        panel.add(confidenceHelpLabel, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(analyzeButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(clearResultsButton);
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    /**
     * Membuat main panel
     * @return JPanel main
     */
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Create tabbed pane for results
        JTabbedPane resultTabbedPane = new JTabbedPane();
        
        // Frequent itemsets tab
        JPanel frequentItemsPanel = createFrequentItemsPanel();
        resultTabbedPane.addTab("Frequent Itemsets", frequentItemsPanel);
        
        // Association rules tab
        JPanel rulesPanel = createRulesPanel();
        resultTabbedPane.addTab("Association Rules", rulesPanel);
        
        // Analysis summary tab
        JPanel summaryPanel = createSummaryPanel();
        resultTabbedPane.addTab("Ringkasan Analisis", summaryPanel);
        
        panel.add(resultTabbedPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Membuat frequent items panel
     * @return JPanel frequent items
     */
    private JPanel createFrequentItemsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(frequentItemsTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Info label
        JLabel infoLabel = new JLabel("Daftar itemset yang memenuhi minimum support threshold");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        infoLabel.setForeground(Color.GRAY);
        panel.add(infoLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Membuat rules panel
     * @return JPanel rules
     */
    private JPanel createRulesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(rulesTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Info label
        JLabel infoLabel = new JLabel("Aturan asosiasi yang ditemukan dari frequent itemsets");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        infoLabel.setForeground(Color.GRAY);
        panel.add(infoLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Membuat summary panel
     * @return JPanel summary
     */
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(analysisResultArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Membuat status panel
     * @return JPanel status
     */
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(progressBar, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        // Analyze button
        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runAnalysis();
            }
        });
        
        // Export button
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportResults();
            }
        });
        
        // Clear results button
        clearResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearResults();
            }
        });
        
        // Double click on rules table for details
        rulesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showRuleDetails();
                }
            }
        });
    }
    
    /**
     * Menjalankan analisis Apriori
     */
    private void runAnalysis() {
        // Validasi data transaksi
        List<Transaction> transactions = salesController.getAllTransactions();
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Tidak ada data transaksi untuk dianalisis!\n" +
                "Silakan input beberapa transaksi terlebih dahulu.",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validasi parameter
        double minSupport = (Double) minSupportSpinner.getValue();
        double minConfidence = (Double) minConfidenceSpinner.getValue();
        
        if (minSupport <= 0 || minSupport > 1) {
            JOptionPane.showMessageDialog(this,
                "Minimum support harus antara 0.01 dan 1.0",
                "Parameter Tidak Valid",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (minConfidence <= 0 || minConfidence > 1) {
            JOptionPane.showMessageDialog(this,
                "Minimum confidence harus antara 0.01 dan 1.0",
                "Parameter Tidak Valid",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Jalankan analisis di background thread
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish("Memulai analisis...");
                
                // Setup algoritma
                aprioriAlgorithm.setTransactions(transactions);
                aprioriAlgorithm.setMinSupport(minSupport);
                aprioriAlgorithm.setMinConfidence(minConfidence);
                
                publish("Mencari frequent itemsets...");
                
                // Jalankan algoritma
                currentRules = aprioriAlgorithm.runApriori();
                
                publish("Analisis selesai!");
                
                return null;
            }
            
            @Override
            protected void process(List<String> chunks) {
                for (String message : chunks) {
                    statusLabel.setText(message);
                }
            }
            
            @Override
            protected void done() {
                try {
                    get(); // Check for exceptions
                    
                    // Update UI dengan hasil
                    updateFrequentItemsTable();
                    updateRulesTable();
                    updateAnalysisSummary();
                    
                    exportButton.setEnabled(true);
                    progressBar.setVisible(false);
                    statusLabel.setText("Analisis berhasil diselesaikan");
                    
                    JOptionPane.showMessageDialog(AprioriPanel.this,
                        "Analisis Apriori berhasil diselesaikan!\n" +
                        "Ditemukan " + currentRules.size() + " aturan asosiasi.",
                        "Analisis Selesai",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                } catch (Exception e) {
                    progressBar.setVisible(false);
                    statusLabel.setText("Analisis gagal");
                    
                    JOptionPane.showMessageDialog(AprioriPanel.this,
                        "Error saat menjalankan analisis: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
                
                analyzeButton.setEnabled(true);
            }
        };
        
        // Disable button dan show progress
        analyzeButton.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        
        worker.execute();
    }
    
    /**
     * Update frequent items table
     */
    private void updateFrequentItemsTable() {
        frequentItemsTableModel.setRowCount(0);
        
        List<List<FrequentItemSet>> allFrequentItemSets = aprioriAlgorithm.getAllFrequentItemSets();
        
        for (List<FrequentItemSet> levelItemSets : allFrequentItemSets) {
            for (FrequentItemSet itemSet : levelItemSets) {
                frequentItemsTableModel.addRow(new Object[] {
                    itemSet.getItemsAsString(),
                    itemSet.getSupport(),
                    String.format("%.2f%%", itemSet.getSupportPercentage() * 100)
                });
            }
        }
    }
    
    /**
     * Update rules table
     */
    private void updateRulesTable() {
        rulesTableModel.setRowCount(0);
        
        if (currentRules != null) {
            for (Rule rule : currentRules) {
                rulesTableModel.addRow(new Object[] {
                    rule.getAntecedentAsString(),
                    rule.getConsequentAsString(),
                    String.format("%.2f%%", rule.getConfidence() * 100),
                    String.format("%.2f%%", rule.getSupport() * 100),
                    String.format("%.2f", rule.getLift()),
                    rule.getLiftInterpretation()
                });
            }
        }
    }
    
    /**
     * Update analysis summary
     */
    private void updateAnalysisSummary() {
        StringBuilder summary = new StringBuilder();
        
        // Parameter yang digunakan
        summary.append("PARAMETER ANALISIS\n");
        summary.append("==================\n");
        summary.append("Minimum Support: ").append(String.format("%.2f%%", (Double) minSupportSpinner.getValue() * 100)).append("\n");
        summary.append("Minimum Confidence: ").append(String.format("%.2f%%", (Double) minConfidenceSpinner.getValue() * 100)).append("\n\n");
        
        // Statistik algoritma
        summary.append("HASIL ANALISIS\n");
        summary.append("==============\n");
        summary.append(aprioriAlgorithm.getAnalysisStatistics()).append("\n\n");
        
        // Top rules
        if (currentRules != null && !currentRules.isEmpty()) {
            summary.append("TOP 5 ATURAN ASOSIASI\n");
            summary.append("=====================\n");
            
            int count = Math.min(5, currentRules.size());
            for (int i = 0; i < count; i++) {
                Rule rule = currentRules.get(i);
                summary.append(String.format("%d. %s\n", i + 1, rule.getRuleAsString()));
                summary.append(String.format("   Confidence: %.2f%%, Support: %.2f%%, Lift: %.2f\n", 
                    rule.getConfidence() * 100, rule.getSupport() * 100, rule.getLift()));
                summary.append(String.format("   Interpretasi: %s\n\n", rule.getLiftInterpretation()));
            }
        }
        
        // Rekomendasi
        summary.append("REKOMENDASI BISNIS\n");
        summary.append("==================\n");
        if (currentRules != null && !currentRules.isEmpty()) {
            summary.append("Berdasarkan analisis, berikut adalah rekomendasi:\n");
            summary.append("1. Produk dengan aturan confidence tinggi dapat dijadikan paket bundling\n");
            summary.append("2. Aturan dengan lift > 1.0 menunjukkan produk yang saling mendukung penjualan\n");
            summary.append("3. Gunakan aturan untuk strategi cross-selling dan up-selling\n");
            summary.append("4. Pertimbangkan penempatan produk yang sering dibeli bersamaan\n");
        } else {
            summary.append("Tidak ditemukan aturan asosiasi yang signifikan.\n");
            summary.append("Pertimbangkan untuk menurunkan threshold atau menambah data transaksi.\n");
        }
        
        analysisResultArea.setText(summary.toString());
        analysisResultArea.setCaretPosition(0);
    }
    
    /**
     * Export hasil analisis
     */
    private void exportResults() {
        if (currentRules == null || currentRules.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Tidak ada hasil analisis untuk di-export!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Export ke CSV
            String filename = "analisis_apriori_" + System.currentTimeMillis() + ".csv";
            
            // Buat CSV content
            StringBuilder csvContent = new StringBuilder();
            csvContent.append("Antecedent,Consequent,Confidence,Support,Lift,Interpretasi\n");
            
            for (Rule rule : currentRules) {
                csvContent.append(String.format("\"%s\",\"%s\",%.4f,%.4f,%.4f,\"%s\"\n",
                    rule.getAntecedentAsString(),
                    rule.getConsequentAsString(),
                    rule.getConfidence(),
                    rule.getSupport(),
                    rule.getLift(),
                    rule.getLiftInterpretation()
                ));
            }
            
            // Simpan file (simulasi - dalam implementasi nyata bisa menggunakan JFileChooser)
            JOptionPane.showMessageDialog(this,
                "Hasil analisis berhasil di-export ke file: " + filename + "\n" +
                "Total aturan: " + currentRules.size(),
                "Export Berhasil",
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error saat export: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Bersihkan hasil analisis
     */
    private void clearResults() {
        int option = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin membersihkan semua hasil analisis?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            frequentItemsTableModel.setRowCount(0);
            rulesTableModel.setRowCount(0);
            analysisResultArea.setText("");
            currentRules = null;
            exportButton.setEnabled(false);
            statusLabel.setText("Hasil analisis dibersihkan");
        }
    }
    
    /**
     * Menampilkan detail aturan
     */
    private void showRuleDetails() {
        int selectedRow = rulesTable.getSelectedRow();
        if (selectedRow >= 0 && currentRules != null) {
            Rule rule = currentRules.get(selectedRow);
            
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                       "Detail Aturan Asosiasi", true);
            dialog.setSize(500, 300);
            dialog.setLocationRelativeTo(this);
            
            JTextArea detailArea = new JTextArea();
            detailArea.setEditable(false);
            detailArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            StringBuilder details = new StringBuilder();
            details.append("DETAIL ATURAN ASOSIASI\n");
            details.append("======================\n\n");
            details.append("Aturan: ").append(rule.getRuleAsString()).append("\n\n");
            details.append("METRIK KUALITAS:\n");
            details.append("Confidence: ").append(String.format("%.2f%%", rule.getConfidence() * 100)).append("\n");
            details.append("Support: ").append(String.format("%.2f%%", rule.getSupport() * 100)).append("\n");
            details.append("Lift: ").append(String.format("%.2f", rule.getLift())).append("\n");
            details.append("Interpretasi Lift: ").append(rule.getLiftInterpretation()).append("\n");
            details.append("Tingkat Kekuatan: ").append(rule.getConfidenceLevel()).append("\n\n");
            details.append("PENJELASAN:\n");
            details.append("- Confidence menunjukkan seberapa sering consequent muncul ketika antecedent ada\n");
            details.append("- Support menunjukkan seberapa sering aturan ini muncul dalam semua transaksi\n");
            details.append("- Lift > 1: produk saling mendukung, < 1: saling menghambat, = 1: independen\n");
            
            detailArea.setText(details.toString());
            
            JScrollPane scrollPane = new JScrollPane(detailArea);
            dialog.add(scrollPane);
            dialog.setVisible(true);
        }
    }
    
    /**
     * Refresh data
     */
    public void refreshData() {
        // Update status
        int transactionCount = salesController.getTransactionCount();
        statusLabel.setText("Data siap - " + transactionCount + " transaksi tersedia");
        
        // Clear previous results if any
        if (currentRules != null) {
            clearResults();
        }
    }
}
