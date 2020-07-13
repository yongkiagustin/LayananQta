package id.yongki.layananqta.Model;


public class UsersModel {
    public UsersModel(String nama, String nohp,String kota, String alamat, String email, String profesi, String lamaKerja, String deskripsi,String status, String profilePic, String docid) {
        this.nama = nama;
        this.nohp = nohp;
        this.email = email;
        this.kota = kota;
        this.alamat = alamat;
        this.profesi = profesi;
        this.lamaKerja = lamaKerja;
        this.deskripsi = deskripsi;
        this.status = status;
        this.profilePic =profilePic;
        this.docid = docid;
    }

    public String nama;
    public String nohp;
    public String email;
    public String kota;
    public String alamat;
    public String profesi;
    public String lamaKerja;
    public String deskripsi;
    public String status;
    public String profilePic;
    public String docid;


}

