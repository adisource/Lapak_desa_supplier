package id.lombokit.emarkethamzanwadisupplier.Models;

public class Users {
    private  String id;
    private  String nama;
    private  String telpon;
    private  String alamat;
    private  int total_belanja;
    private  int jumlah;

    public Users(String id,String nama, String telpon,int total_belanja,int jumlah){
        this.id = id;
        this.nama = nama;
        this.telpon = telpon;
        this.total_belanja = total_belanja;
        this.jumlah = jumlah;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTelpon() {
        return telpon;
    }

    public void setTelpon(String telpon) {
        this.telpon = telpon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public int getTotal_belanja() {
        return total_belanja;
    }

    public void setTotal_belanja(int total_belanja) {
        this.total_belanja = total_belanja;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
