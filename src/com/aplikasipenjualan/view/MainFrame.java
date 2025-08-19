package com.aplikasipenjualan.view;

import com.aplikasipenjualan.controller.SalesController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * Frame utama aplikasi penjualan dengan metode Apriori
 * 
 * @author Developer
 * @version 1.0
 */
public class MainFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    // Components
    private JTabbedPane tabbedPane;
    private InputPenjualanPanel inputPenjualanPanel;
    private AprioriPanel aprioriPanel;
    private SalesController salesController;
    
    // Constants
    private static final String TITLE = "Aplikasi Penjualan dengan Metode Apriori";
    private static final int DEFAULT_WIDTH = 1000;
    private static final int DEFAULT_HEIGHT = 700;
    
    /**
     * Constructor
     */
    public MainFrame() {
        initializeController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
    }
    
    /**
     * Inisialisasi controller
     */
    private void initializeController() {
        try {
            salesController = new SalesController();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error menginisialisasi controller: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    /**
     * Inisialisasi komponen UI
     */
    private void initializeComponents() {
        // Buat tabbed pane
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        // Buat panel-panel
        inputPenjualanPanel = new InputPenjualanPanel(salesController);
        aprioriPanel = new AprioriPanel(salesController);
        
        // Tambahkan tab
        tabbedPane.addTab("Input Penjualan", null, inputPenjualanPanel, "Input data penjualan");
        tabbedPane.addTab("Analisis Apriori", null, aprioriPanel, "Analisis dengan algoritma Apriori");
        
        // Set tab yang aktif
        tabbedPane.setSelectedIndex(0);
    }
    
    
    /**
     * Setup layout frame
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        add(tabbedPane, BorderLayout.CENTER);
        
        // Footer panel
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Membuat header panel
     * @return JPanel header
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185)); // Biru
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Title
        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setOpaque(false);
        
        JLabel infoLabel = new JLabel("Sistem Analisis Pola Pembelian");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        infoLabel.setForeground(Color.WHITE);
        infoPanel.add(infoLabel);
        
        headerPanel.add(infoPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Membuat footer panel
     * @return JPanel footer
     */
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(52, 73, 94)); // Abu-abu gelap
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Status label
        JLabel statusLabel = new JLabel("Siap");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        statusLabel.setForeground(Color.WHITE);
        footerPanel.add(statusLabel, BorderLayout.WEST);
        
        // Version info
        JLabel versionLabel = new JLabel("Versi 1.0 - Java NetBeans");
        versionLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        versionLabel.setForeground(Color.LIGHT_GRAY);
        footerPanel.add(versionLabel, BorderLayout.EAST);
        
        return footerPanel;
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        // Tab change listener
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            
            // Refresh data ketika pindah ke tab Apriori
            if (selectedIndex == 1) { // Apriori tab
                aprioriPanel.refreshData();
            }
        });
        
        // Window closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
        
        // Setup komunikasi antar panel
        setupPanelCommunication();
    }
    
    /**
     * Setup komunikasi antar panel
     */
    private void setupPanelCommunication() {
        // Ketika ada transaksi baru di input panel, refresh apriori panel
        inputPenjualanPanel.setTransactionAddedListener(() -> {
            aprioriPanel.refreshData();
        });
    }
    
    /**
     * Handle window closing event
     */
    private void handleWindowClosing() {
        int option = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin keluar dari aplikasi?",
            "Konfirmasi Keluar",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            // Cleanup resources jika diperlukan
            dispose();
            System.exit(0);
        }
    }
    
    /**
     * Setup frame properties
     */
    private void setupFrame() {
        setTitle(TITLE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        
        // Set minimum size
        setMinimumSize(new Dimension(800, 600));
        
        // Set icon (menggunakan default Java icon)
        try {
            // Bisa diganti dengan icon custom jika ada
            setIconImage(createDefaultIcon());
        } catch (Exception e) {
            // Ignore jika gagal set icon
        }
        
        // Set look and feel properties
        setupLookAndFeel();
    }
    
    /**
     * Membuat default icon untuk aplikasi
     * @return Image icon
     */
    private Image createDefaultIcon() {
        // Membuat icon sederhana 32x32
        Image icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) icon.getGraphics();
        
        // Background
        g2d.setColor(new Color(41, 128, 185));
        g2d.fillRect(0, 0, 32, 32);
        
        // Simple chart icon
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        
        // Draw simple bar chart
        g2d.fillRect(6, 20, 4, 8);
        g2d.fillRect(12, 16, 4, 12);
        g2d.fillRect(18, 12, 4, 16);
        g2d.fillRect(24, 18, 4, 10);
        
        g2d.dispose();
        return icon;
    }
    
    /**
     * Setup look and feel properties
     */
    private void setupLookAndFeel() {
        // Set UI properties untuk konsistensi
        UIManager.put("TabbedPane.selected", new Color(236, 240, 241));
        UIManager.put("TabbedPane.background", new Color(189, 195, 199));
        UIManager.put("TabbedPane.foreground", new Color(44, 62, 80));
        
        // Button properties
        UIManager.put("Button.background", new Color(52, 152, 219));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("SansSerif", Font.BOLD, 12));
        
        // Table properties
        UIManager.put("Table.gridColor", new Color(189, 195, 199));
        UIManager.put("Table.selectionBackground", new Color(52, 152, 219));
        UIManager.put("Table.selectionForeground", Color.WHITE);
    }
    
    /**
     * Mendapatkan sales controller
     * @return SalesController instance
     */
    public SalesController getSalesController() {
        return salesController;
    }
    
    /**
     * Refresh semua panel
     */
    public void refreshAllPanels() {
        inputPenjualanPanel.refreshData();
        aprioriPanel.refreshData();
    }
    
    /**
     * Pindah ke tab tertentu
     * @param tabIndex Index tab (0: Input, 1: Apriori)
     */
    public void switchToTab(int tabIndex) {
        if (tabIndex >= 0 && tabIndex < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(tabIndex);
        }
    }
    
    /**
     * Menampilkan pesan error
     * @param message Pesan error
     * @param title Judul dialog
     */
    public void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Menampilkan pesan informasi
     * @param message Pesan informasi
     * @param title Judul dialog
     */
    public void showInfoMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Menampilkan dialog konfirmasi
     * @param message Pesan konfirmasi
     * @param title Judul dialog
     * @return true jika user memilih Yes
     */
    public boolean showConfirmDialog(String message, String title) {
        int result = JOptionPane.showConfirmDialog(
            this, message, title, 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }
}
