# Manajemen Produk Toko (JavaFX)

Aplikasi desktop sederhana berbasis JavaFX dan Maven untuk pencatatan inventaris produk toko (Makanan dan Minuman). Proyek ini dibuat sebagai pemenuhan Tugas Individu mata kuliah Pemrograman Lanjut, dengan memodifikasi arsitektur dasar dari modul praktikum GUI JavaFX.

## Fitur Utama

* **Input Produk Baru**: Menambahkan data produk yang terdiri dari nama produk, kategori (`Makanan` atau `Minuman`), harga, serta lampiran gambar lokal opsional (`.png`, `.jpg`, `.jpeg` maks. 5 MB). Kode produk digenerate secara otomatis (`PR001`, `PR002`, dst.).
* **Validasi Input**: Mencegah penyimpanan data jika nama produk kosong atau harga bernilai kurang dari/sama dengan nol (`IllegalArgumentException` & `NumberFormatException`).
* **Tabel Daftar Produk**: Menampilkan seluruh produk yang tersimpan di memori (RAM) menggunakan `TableView` dan `ObservableList`, lengkap dengan render *thumbnail* gambar langsung di dalam sel tabel (`TableCell`).
* **Pencarian & Filter**: Mencari produk berdasarkan nama atau kode melalui tombol **Cari**, serta menyaring daftar berdasarkan kategori. Tersedia juga opsi *checkbox* untuk menyembunyikan/menampilkan kolom harga dan gambar.
* **Detail Produk**: Menampilkan informasi lengkap dari objek `Produk` yang dipilih pada tabel beserta pratinjau gambar ukuran penuh dan format mata uang Rupiah (`id-ID`).

## Struktur Proyek

```text
src/main/
├── java/kelas/
│   ├── MainApp.java                 # Entry point aplikasi JavaFX
│   ├── Navigator.java               # Pengatur perpindahan halaman (Scene root)
│   ├── controller/
│   │   ├── FormController.java      # Logika form input & FileChooser gambar
│   │   ├── DaftarController.java    # Logika TableView, pencarian, filter & hapus
│   │   └── DetailController.java    # Logika halaman detail produk
│   ├── data/
│   │   └── DataProduk.java          # Penyimpanan sementara di RAM (ObservableList)
│   └── model/
│       └── Produk.java              # Model objek produk, enkapsulasi & validasi
└── resources/kelas/
    ├── form.fxml                    # Layout halaman tambah produk
    ├── daftar.fxml                  # Layout halaman tabel daftar produk
    ├── detail.fxml                  # Layout halaman detail produk
    └── styles/
        └── app.css                  # Stylesheet komponen UI
