package com.materialsteam.materials.models;

public class User {

    private String kd_user, nama, email, no_telpon, alamat;

    public User(String kd_user, String nama, String email,
                String no_telpon, String alamat) {
        this.kd_user = kd_user;
        this.nama = nama;
        this.email = email;
        this.no_telpon = no_telpon;
        this.alamat = alamat;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKd_user() {
        return kd_user;
    }

    public void setKd_user(String kd_user) {
        this.kd_user = kd_user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNo_telpon() {
        return no_telpon;
    }

    public void setNo_telpon(String no_telpon) {
        this.no_telpon = no_telpon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

}
