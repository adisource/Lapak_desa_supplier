package id.lombokit.emarkethamzanwadisupplier.Models;

/**
 * Created by one on 22/1/18.
 */
public class Products {
    private int id_barang;
    private String nama_barang;
    private String gambar;
    private String harga;
    private String qty;
    private String total;
    private String toko;
    private String jam;
    private String status;
    private String token;

    public Products(int id_barang, String nama_barang, String gambar, String harga, String qty, String total, String jam,String status,String token) {
        this.id_barang = id_barang;
        this.nama_barang = nama_barang;
        this.gambar = gambar;
        this.harga = harga;
        this.qty = qty;
        this.total = total;
        this.jam = jam;
        this.status = status;
        this.token = token;

    }

    public Products(int id_barang, String nama_barang, String gambar, String harga) {
        this.id_barang = id_barang;
        this.nama_barang = nama_barang;
        this.gambar = gambar;
        this.harga = harga;
    }

    public int getId_barang() {
        return id_barang;
    }

    public void setId_barang(int id_barang) {
        this.id_barang = id_barang;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getgambar() {
        return gambar;
    }

    public void setgambar(String gambar) {
        this.gambar = gambar;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}