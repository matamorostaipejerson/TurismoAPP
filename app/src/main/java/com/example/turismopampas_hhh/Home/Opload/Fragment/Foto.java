package com.example.turismopampas_hhh.Home.Opload.Fragment;

public class Foto {

    private String descripcion;
    private byte[] img_ver;

    public Foto() {
        // Debes inicializar los campos en el constructor
        this.descripcion = "";
        this.img_ver = null;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getImg() {
        return img_ver;
    }

    public void setImg(byte[] img) {
        this.img_ver = img;
    }
}
