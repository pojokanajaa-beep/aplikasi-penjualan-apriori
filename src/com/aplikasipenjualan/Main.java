package com.aplikasipenjualan;

import com.aplikasipenjualan.view.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Kelas utama untuk menjalankan Aplikasi Penjualan dengan Metode Apriori
 * 
 * @author Developer
 * @version 1.0
 */
public class Main {
    
    /**
     * Method main untuk memulai aplikasi
     * @param args argumen command line
     */
    public static void main(String[] args) {
        // Set Look and Feel ke sistem default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Gagal mengatur Look and Feel: " + e.getMessage());
        }
        
        // Jalankan aplikasi di Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            } catch (Exception e) {
                System.err.println("Error saat memulai aplikasi: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
