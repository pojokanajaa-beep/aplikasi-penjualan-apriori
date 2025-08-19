# Aplikasi Penjualan dengan Metode Apriori

Aplikasi desktop berbasis Java NetBeans untuk analisis pola pembelian menggunakan algoritma Apriori. Aplikasi ini memungkinkan pengguna untuk menginput data penjualan dan melakukan analisis association rule mining untuk menemukan pola pembelian yang sering terjadi.

## 📋 Daftar Isi

- [Fitur Utama](#fitur-utama)
- [Teknologi yang Digunakan](#teknologi-yang-digunakan)
- [Persyaratan Sistem](#persyaratan-sistem)
- [Instalasi dan Setup](#instalasi-dan-setup)
- [Cara Penggunaan](#cara-penggunaan)
- [Struktur Proyek](#struktur-proyek)
- [Algoritma Apriori](#algoritma-apriori)
- [Screenshot](#screenshot)
- [Kontribusi](#kontribusi)
- [Lisensi](#lisensi)

## 🚀 Fitur Utama

### 1. **Input Data Penjualan**
- Interface yang user-friendly untuk input transaksi penjualan
- Manajemen produk dengan kategori dan harga
- Keranjang belanja untuk memudahkan input multiple items
- Validasi data input secara real-time
- Riwayat transaksi dengan pencarian dan sorting

### 2. **Analisis Algoritma Apriori**
- Implementasi lengkap algoritma Apriori
- Parameter yang dapat disesuaikan (minimum support dan confidence)
- Visualisasi frequent itemsets
- Generasi association rules dengan metrik kualitas
- Interpretasi hasil analisis untuk rekomendasi bisnis

### 3. **Visualisasi dan Reporting**
- Tabel interaktif untuk menampilkan hasil analisis
- Export hasil ke format CSV
- Statistik penjualan dan analisis
- Detail view untuk setiap aturan asosiasi

### 4. **Manajemen Data**
- Penyimpanan data otomatis ke file lokal
- Backup dan restore data transaksi
- Validasi integritas data

## 🛠 Teknologi yang Digunakan

- **Java 8+** - Bahasa pemrograman utama
- **Swing** - GUI framework untuk desktop application
- **NetBeans IDE** - Integrated Development Environment
- **Apache Ant** - Build automation tool
- **Java Serialization** - Untuk penyimpanan data lokal

## 💻 Persyaratan Sistem

### Minimum Requirements:
- **OS**: Windows 7/8/10/11, macOS 10.12+, atau Linux (Ubuntu 16.04+)
- **Java**: JRE/JDK 8 atau lebih tinggi
- **RAM**: 512 MB (disarankan 1 GB)
- **Storage**: 50 MB ruang kosong
- **Display**: Resolusi minimum 1024x768

### Recommended Requirements:
- **Java**: JDK 11 atau lebih tinggi
- **RAM**: 2 GB atau lebih
- **Storage**: 100 MB ruang kosong
- **Display**: Resolusi 1366x768 atau lebih tinggi

## 📦 Instalasi dan Setup

### Opsi 1: Menggunakan NetBeans IDE

1. **Clone atau Download Proyek**
   ```bash
   git clone https://github.com/username/aplikasi-penjualan-apriori.git
   cd aplikasi-penjualan-apriori
   ```

2. **Buka di NetBeans**
   - Buka NetBeans IDE
   - File → Open Project
   - Pilih folder proyek
   - Klik "Open Project"

3. **Build dan Run**
   - Klik kanan pada proyek → "Clean and Build"
   - Klik kanan pada proyek → "Run"

### Opsi 2: Menggunakan Command Line

1. **Compile Proyek**
   ```bash
   # Menggunakan Ant
   ant clean build
   
   # Atau menggunakan javac langsung
   javac -d build -sourcepath src src/com/aplikasipenjualan/Main.java
   ```

2. **Jalankan Aplikasi**
   ```bash
   # Menggunakan Ant
   ant run
   
   # Atau menggunakan java langsung
   java -cp build com.aplikasipenjualan.Main
   ```

### Opsi 3: Menggunakan JAR File

1. **Build JAR**
   ```bash
   ant dist
   ```

2. **Jalankan JAR**
   ```bash
   java -jar dist/AplikasiPenjualan-standalone.jar
   ```

## 📖 Cara Penggunaan

### 1. Input Data Penjualan

1. **Buka Tab "Input Penjualan"**
2. **Pilih Produk** dari dropdown menu
3. **Tentukan Jumlah** menggunakan spinner
4. **Klik "Tambah ke Keranjang"** untuk menambah item
5. **Review Keranjang** - pastikan semua item sudah benar
6. **Klik "Submit Transaksi"** untuk menyimpan

**Tips:**
- Gunakan "Kosongkan Keranjang" jika ingin mengulang
- Double-click pada riwayat transaksi untuk melihat detail
- Data akan tersimpan otomatis

### 2. Analisis Apriori

1. **Buka Tab "Analisis Apriori"**
2. **Set Parameter:**
   - **Minimum Support**: 0.1 (10%) - seberapa sering itemset muncul
   - **Minimum Confidence**: 0.5 (50%) - tingkat kepercayaan aturan
3. **Klik "Jalankan Analisis"**
4. **Review Hasil:**
   - **Frequent Itemsets**: kombinasi produk yang sering dibeli
   - **Association Rules**: aturan "jika beli A maka beli B"
   - **Ringkasan Analisis**: interpretasi dan rekomendasi

**Interpretasi Hasil:**
- **Confidence tinggi (>80%)**: Aturan sangat kuat
- **Lift > 1**: Produk saling mendukung penjualan
- **Lift < 1**: Produk saling menghambat
- **Lift = 1**: Produk independen

### 3. Export dan Backup

- **Export Hasil**: Klik "Export Hasil" untuk menyimpan analisis ke CSV
- **Backup Data**: Data transaksi tersimpan otomatis di folder `data/`

## 📁 Struktur Proyek

```
aplikasi-penjualan-apriori/
├── src/
│   └── com/
│       └── aplikasipenjualan/
│           ├── Main.java                 # Entry point aplikasi
│           ├── model/                    # Data models
│           │   ├── Product.java          # Model produk
│           │   └── Transaction.java      # Model transaksi
│           ├── controller/               # Business logic
│           │   └── SalesController.java  # Controller penjualan
│           ├── view/                     # User interface
│           │   ├── MainFrame.java        # Frame utama
│           │   ├── InputPenjualanPanel.java  # Panel input
│           │   └── AprioriPanel.java     # Panel analisis
│           ├── algorithm/                # Algoritma Apriori
│           │   ├── Apriori.java          # Implementasi algoritma
│           │   ├── FrequentItemSet.java  # Frequent itemsets
│           │   └── Rule.java             # Association rules
│           └── util/                     # Utilities
│               └── DataLoader.java       # Data persistence
├── build.xml                            # Ant build script
├── manifest.mf                          # JAR manifest
├── README.md                            # Dokumentasi
└── data/                                # Data storage (auto-created)
    └── transactions.dat                 # File data transaksi
```

## 🔬 Algoritma Apriori

### Konsep Dasar

Algoritma Apriori adalah algoritma untuk menemukan frequent itemsets dan association rules dalam dataset transaksi. Algoritma ini bekerja dengan prinsip:

1. **Frequent Itemsets**: Kombinasi item yang muncul dengan frekuensi ≥ minimum support
2. **Association Rules**: Aturan "IF A THEN B" dengan confidence ≥ minimum confidence

### Langkah-langkah Algoritma

1. **Generate 1-itemsets** yang frequent
2. **Join** frequent k-itemsets untuk membuat candidate (k+1)-itemsets
3. **Prune** candidates yang memiliki infrequent subset
4. **Scan database** untuk menghitung support
5. **Ulangi** hingga tidak ada frequent itemsets baru
6. **Generate rules** dari frequent itemsets

### Metrik Kualitas

- **Support**: P(A ∪ B) - seberapa sering itemset muncul
- **Confidence**: P(B|A) - seberapa sering B muncul ketika A ada
- **Lift**: P(B|A) / P(B) - kekuatan asosiasi

### Contoh Interpretasi

```
Rule: {Roti, Susu} → {Telur}
Support: 20% (muncul di 20% transaksi)
Confidence: 80% (80% pembeli roti+susu juga beli telur)
Lift: 1.5 (pembelian telur 1.5x lebih mungkin dengan roti+susu)
```

## 📸 Screenshot

### 1. Tampilan Utama
![Main Interface](docs/screenshots/main-interface.png)

### 2. Input Penjualan
![Input Panel](docs/screenshots/input-panel.png)

### 3. Analisis Apriori
![Analysis Panel](docs/screenshots/analysis-panel.png)

### 4. Hasil Analisis
![Results](docs/screenshots/results.png)

## 🧪 Testing

### Manual Testing

1. **Test Input Data**
   - Input berbagai kombinasi produk
   - Validasi error handling
   - Test edge cases (keranjang kosong, dll)

2. **Test Algoritma**
   - Jalankan dengan parameter berbeda
   - Verifikasi hasil dengan perhitungan manual
   - Test dengan dataset kecil dan besar

3. **Test UI**
   - Navigasi antar tab
   - Responsivitas interface
   - Export functionality

### Unit Testing (Future Enhancement)

```java
// Contoh unit test untuk algoritma Apriori
@Test
public void testAprioriAlgorithm() {
    // Setup test data
    List<Transaction> testTransactions = createTestData();
    
    // Run algorithm
    Apriori apriori = new Apriori(testTransactions, 0.5, 0.7);
    List<Rule> rules = apriori.runApriori();
    
    // Verify results
    assertNotNull(rules);
    assertTrue(rules.size() > 0);
}
```

## 🤝 Kontribusi

Kontribusi sangat diterima! Berikut cara berkontribusi:

### 1. Fork dan Clone
```bash
git fork https://github.com/username/aplikasi-penjualan-apriori.git
git clone https://github.com/yourusername/aplikasi-penjualan-apriori.git
```

### 2. Buat Branch Baru
```bash
git checkout -b feature/nama-fitur-baru
```

### 3. Commit Changes
```bash
git add .
git commit -m "Add: fitur baru untuk..."
```

### 4. Push dan Pull Request
```bash
git push origin feature/nama-fitur-baru
```

### Guidelines Kontribusi

- Ikuti coding standards Java
- Tambahkan Javadoc untuk method public
- Test fitur baru sebelum submit
- Update dokumentasi jika diperlukan

### Ideas untuk Enhancement

- [ ] Database integration (MySQL/PostgreSQL)
- [ ] Web-based interface
- [ ] Advanced visualization (charts, graphs)
- [ ] Machine learning integration
- [ ] Multi-language support
- [ ] Advanced export formats (PDF, Excel)
- [ ] User authentication and roles
- [ ] Real-time analytics dashboard

## 📄 Lisensi

Proyek ini dilisensikan under MIT License - lihat file [LICENSE](LICENSE) untuk detail.

```
MIT License

Copyright (c) 2024 Developer

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 📞 Kontak dan Support

- **Email**: developer@example.com
- **GitHub Issues**: [Report Bug](https://github.com/username/aplikasi-penjualan-apriori/issues)
- **Documentation**: [Wiki](https://github.com/username/aplikasi-penjualan-apriori/wiki)

## 🙏 Acknowledgments

- Terima kasih kepada komunitas Java dan NetBeans
- Referensi algoritma Apriori dari paper akademik
- Inspirasi UI/UX dari aplikasi desktop modern
- Testing dan feedback dari beta users

---

**Dibuat dengan ❤️ menggunakan Java dan NetBeans IDE**

*Versi: 1.0 | Terakhir diupdate: 2024*
