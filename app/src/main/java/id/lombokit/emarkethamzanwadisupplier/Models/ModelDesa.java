package id.lombokit.emarkethamzanwadisupplier.Models;

public class ModelDesa {
    private int id_desa;
    private String nama_desa;

    public ModelDesa(int id_desa, String nama_desa) {
        this.id_desa = id_desa;
        this.nama_desa = nama_desa;
    }

    public int getId_desa() {
        return id_desa;
    }

    public void setId_desa(int id_desa) {
        this.id_desa = id_desa;
    }

    public String getNama_desa() {
        return nama_desa;
    }

    public void setNama_desa(String nama_desa) {
        this.nama_desa = nama_desa;
    }
}
