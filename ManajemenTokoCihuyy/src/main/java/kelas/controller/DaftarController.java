package kelas.controller;

import java.io.IOException;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kelas.Navigator;
import kelas.data.DataProduk;
import kelas.model.Produk;

public class DaftarController {

    @FXML
    private TableView<Produk> tabel;
    @FXML
    private TableColumn<Produk, String> kodeCol;
    @FXML
    private TableColumn<Produk, String> namaCol;
    @FXML
    private TableColumn<Produk, String> kategoriCol;
    @FXML
    private TableColumn<Produk, Long> hargaCol;
    @FXML
    private TableColumn<Produk, String> gambarCol;
    @FXML
    private TextField cariField;
    @FXML
    private ComboBox<String> filterKategoriBox;
    @FXML
    private CheckBox hargaCheck;
    @FXML
    private CheckBox gambarCheck;
    @FXML
    private Label pesanLabel;

    private Navigator navigator;
    private DataProduk data;
    private final ObservableList<Produk> dataTampilan
            = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // KOLOM KODE
        PropertyValueFactory<Produk, String> sumberKode
                = new PropertyValueFactory<Produk, String>("kode");
        kodeCol.setCellValueFactory(sumberKode);

        // KOLOM NAMA PRODUK
        PropertyValueFactory<Produk, String> sumberNama
                = new PropertyValueFactory<Produk, String>("nama");
        namaCol.setCellValueFactory(sumberNama);

        // KOLOM KATEGORI
        PropertyValueFactory<Produk, String> sumberKategori
                = new PropertyValueFactory<Produk, String>("kategori");
        kategoriCol.setCellValueFactory(sumberKategori);

        // KOLOM HARGA
        PropertyValueFactory<Produk, Long> sumberHarga
                = new PropertyValueFactory<Produk, Long>("harga");
        hargaCol.setCellValueFactory(sumberHarga);

        // KOLOM GAMBAR (Menampilkan thumbnail gambar produk di dalam tabel)
        PropertyValueFactory<Produk, String> sumberGambar
                = new PropertyValueFactory<Produk, String>("gambarUri");
        gambarCol.setCellValueFactory(sumberGambar);
        gambarCol.setCellFactory(col -> new TableCell<Produk, String>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(60);
                imageView.setFitHeight(45);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String uri, boolean empty) {
                super.updateItem(uri, empty);
                if (empty || uri == null || uri.isBlank()) {
                    setGraphic(null);
                    setText(empty ? null : "Tanpa Gambar");
                } else {
                    Image img = new Image(uri, 60, 45, true, true);
                    if (img.isError()) {
                        setGraphic(null);
                        setText("Gagal Muat");
                    } else {
                        imageView.setImage(img);
                        setGraphic(imageView);
                        setText(null);
                    }
                }
            }
        });

        filterKategoriBox.getItems().add("Semua");
        filterKategoriBox.getItems().add("Makanan");
        filterKategoriBox.getItems().add("Minuman");
        filterKategoriBox.setValue("Semua");

        tabel.setItems(dataTampilan);
        tabel.setPlaceholder(new Label("Belum ada data produk yang sesuai."));
        ubahTampilanHarga();
        ubahTampilanGambar();
    }

    public void setKonteks(Navigator navigator, DataProduk data) {
        this.navigator = navigator;
        this.data = data;
        cari();
    }

    @FXML
    private void cari() {
        if (data == null) {
            return;
        }
        dataTampilan.clear();
        String kata = cariField.getText().trim().toLowerCase(Locale.ROOT);
        String kategoriDipilih = filterKategoriBox.getValue();

        for (Produk produk : data.getDaftar()) {
            String kode = produk.getKode().toLowerCase(Locale.ROOT);
            String nama = produk.getNama().toLowerCase(Locale.ROOT);
            boolean cocokKata = false;
            boolean cocokKategori = false;

            if (nama.contains(kata) || kode.contains(kata)) {
                cocokKata = true;
            }
            if ("Semua".equals(kategoriDipilih)) {
                cocokKategori = true;
            } else if (produk.getKategori().equals(kategoriDipilih)) {
                cocokKategori = true;
            }
            if (cocokKata && cocokKategori) {
                dataTampilan.add(produk);
            }
        }
        tabel.sort();
        pesanLabel.setText("Jumlah data yang tampil: " + dataTampilan.size());
    }

    @FXML
    private void resetPencarian() {
        cariField.clear();
        filterKategoriBox.setValue("Semua");
        cari();
    }

    @FXML
    private void ubahTampilanHarga() {
        if (hargaCheck.isSelected()) {
            hargaCol.setVisible(true);
        } else {
            hargaCol.setVisible(false);
        }
    }

    @FXML
    private void ubahTampilanGambar() {
        if (gambarCheck.isSelected()) {
            gambarCol.setVisible(true);
        } else {
            gambarCol.setVisible(false);
        }
    }

    @FXML
    private void tambah() {
        try {
            navigator.bukaForm();
        } catch (IOException e) {
            pesanLabel.setText("Form gagal dibuka.");
            e.printStackTrace();
        }
    }

    @FXML
    private void detail() {
        Produk produk = tabel.getSelectionModel().getSelectedItem();
        if (produk == null) {
            pesanLabel.setText("Pilih satu baris dahulu.");
            return;
        }
        try {
            navigator.bukaDetail(produk);
        } catch (IOException e) {
            pesanLabel.setText("Detail gagal dibuka.");
            e.printStackTrace();
        }
    }

    @FXML
    private void hapus() {
        Produk produk = tabel.getSelectionModel().getSelectedItem();
        if (produk == null) {
            pesanLabel.setText("Pilih satu baris dahulu.");
            return;
        }
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Hapus " + produk.getKode() + " (" + produk.getNama() + ")?",
                ButtonType.YES, ButtonType.NO);
        dialog.initOwner(tabel.getScene().getWindow());
        ButtonType jawaban = dialog.showAndWait().orElse(ButtonType.NO);
        if (jawaban == ButtonType.YES) {
            data.hapus(produk);
            cari();
        }
    }
}
