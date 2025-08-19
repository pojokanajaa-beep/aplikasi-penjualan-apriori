package com.aplikasipenjualan.util;

import com.aplikasipenjualan.model.Transaction;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class untuk menyimpan dan memuat data dari file
 * 
 * @author Developer
 * @version 1.0
 */
public class DataLoader {
    
    private static final Logger LOGGER = Logger.getLogger(DataLoader.class.getName());
    private static final String DATA_DIRECTORY = "data";
    private static final String TRANSACTIONS_FILE = DATA_DIRECTORY + File.separator + "transactions.dat";
    
    /**
     * Constructor - membuat direktori data jika belum ada
     */
    public DataLoader() {
        createDataDirectory();
    }
    
    /**
     * Membuat direktori data jika belum ada
     */
    private void createDataDirectory() {
        File dataDir = new File(DATA_DIRECTORY);
        if (!dataDir.exists()) {
            boolean created = dataDir.mkdirs();
            if (created) {
                LOGGER.info("Direktori data berhasil dibuat: " + DATA_DIRECTORY);
            } else {
                LOGGER.warning("Gagal membuat direktori data: " + DATA_DIRECTORY);
            }
        }
    }
    
    /**
     * Menyimpan daftar transaksi ke file
     * @param transactions Daftar transaksi yang akan disimpan
     * @throws IOException jika terjadi error saat menyimpan
     */
    public void saveTransactions(List<Transaction> transactions) throws IOException {
        if (transactions == null) {
            throw new IllegalArgumentException("Daftar transaksi tidak boleh null");
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(TRANSACTIONS_FILE)))) {
            
            oos.writeObject(transactions);
            oos.flush();
            LOGGER.info("Berhasil menyimpan " + transactions.size() + " transaksi ke file");
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error menyimpan transaksi ke file: " + TRANSACTIONS_FILE, e);
            throw e;
        }
    }
    
    /**
     * Memuat daftar transaksi dari file
     * @return Daftar transaksi yang dimuat dari file
     * @throws IOException jika terjadi error saat memuat
     * @throws ClassNotFoundException jika class tidak ditemukan saat deserialisasi
     */
    @SuppressWarnings("unchecked")
    public List<Transaction> loadTransactions() throws IOException, ClassNotFoundException {
        File file = new File(TRANSACTIONS_FILE);
        
        if (!file.exists()) {
            LOGGER.info("File transaksi tidak ditemukan, mengembalikan list kosong");
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            
            List<Transaction> transactions = (List<Transaction>) ois.readObject();
            LOGGER.info("Berhasil memuat " + transactions.size() + " transaksi dari file");
            return transactions;
            
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error memuat transaksi dari file: " + TRANSACTIONS_FILE, e);
            throw e;
        }
    }
    
    /**
     * Menyimpan transaksi ke file CSV untuk keperluan export
     * @param transactions Daftar transaksi
     * @param filename Nama file CSV
     * @throws IOException jika terjadi error saat menyimpan
     */
    public void exportTransactionsToCSV(List<Transaction> transactions, String filename) throws IOException {
        if (transactions == null) {
            throw new IllegalArgumentException("Daftar transaksi tidak boleh null");
        }
        
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama file tidak boleh kosong");
        }
        
        String csvFile = DATA_DIRECTORY + File.separator + filename;
        if (!csvFile.endsWith(".csv")) {
            csvFile += ".csv";
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            // Header CSV
            writer.println("Transaction ID,Tanggal,Jumlah Item,Total Harga,Produk");
            
            // Data transaksi
            for (Transaction transaction : transactions) {
                StringBuilder productNames = new StringBuilder();
                transaction.getProducts().forEach(product -> {
                    if (productNames.length() > 0) {
                        productNames.append(";");
                    }
                    productNames.append(product.getNama());
                });
                
                writer.printf("%s,%s,%d,%.2f,\"%s\"%n",
                    transaction.getTransactionId(),
                    transaction.getFormattedDate(),
                    transaction.getItemCount(),
                    transaction.getTotalHarga(),
                    productNames.toString()
                );
            }
            
            LOGGER.info("Berhasil export " + transactions.size() + " transaksi ke CSV: " + csvFile);
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error export transaksi ke CSV: " + csvFile, e);
            throw e;
        }
    }
    
    /**
     * Menghapus file transaksi
     * @return true jika berhasil dihapus
     */
    public boolean deleteTransactionsFile() {
        File file = new File(TRANSACTIONS_FILE);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                LOGGER.info("File transaksi berhasil dihapus: " + TRANSACTIONS_FILE);
            } else {
                LOGGER.warning("Gagal menghapus file transaksi: " + TRANSACTIONS_FILE);
            }
            return deleted;
        }
        return true; // File tidak ada, dianggap berhasil
    }
    
    /**
     * Mengecek apakah file transaksi ada
     * @return true jika file ada
     */
    public boolean transactionsFileExists() {
        return new File(TRANSACTIONS_FILE).exists();
    }
    
    /**
     * Mendapatkan ukuran file transaksi dalam bytes
     * @return Ukuran file dalam bytes, -1 jika file tidak ada
     */
    public long getTransactionsFileSize() {
        File file = new File(TRANSACTIONS_FILE);
        return file.exists() ? file.length() : -1;
    }
    
    /**
     * Membuat backup file transaksi
     * @return true jika backup berhasil dibuat
     */
    public boolean backupTransactionsFile() {
        File sourceFile = new File(TRANSACTIONS_FILE);
        if (!sourceFile.exists()) {
            LOGGER.warning("File transaksi tidak ada, tidak dapat membuat backup");
            return false;
        }
        
        String backupFileName = TRANSACTIONS_FILE + ".backup." + System.currentTimeMillis();
        File backupFile = new File(backupFileName);
        
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(backupFile)) {
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            
            LOGGER.info("Backup berhasil dibuat: " + backupFileName);
            return true;
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error membuat backup file transaksi", e);
            return false;
        }
    }
    
    /**
     * Mendapatkan informasi file transaksi
     * @return String berisi informasi file
     */
    public String getFileInfo() {
        File file = new File(TRANSACTIONS_FILE);
        if (!file.exists()) {
            return "File transaksi tidak ditemukan";
        }
        
        return String.format(
            "Informasi File Transaksi:\n" +
            "Lokasi: %s\n" +
            "Ukuran: %d bytes\n" +
            "Terakhir dimodifikasi: %s\n" +
            "Dapat dibaca: %s\n" +
            "Dapat ditulis: %s",
            file.getAbsolutePath(),
            file.length(),
            new java.util.Date(file.lastModified()),
            file.canRead() ? "Ya" : "Tidak",
            file.canWrite() ? "Ya" : "Tidak"
        );
    }
}
