package com.rrcc.ubifarm01.ClasesDeObjetos;

public class Farmacia {

    private String idFarmacia;
    private String nombreDeFarmacia;
    private String direccioDeFarmacia;
    private String correoDeFarmacia;
    private String telefonoDeFarmacia;
    private String propietarioFarmacia;
    private String rol;
    private String correoPropietario;
    private int imagenDeFarmacia;

    public Farmacia() {
    }

    public Farmacia(String nombreDeFarmacia, String direccioDeFarmacia, String correoDeFarmacia,
                    String telefonoDeFarmacia, String propietarioFarmacia, String rol,
                    String idFarmacia, String correoPropietario, int imagenDeFarmacia, Sucursal sucursal) {
        this.nombreDeFarmacia = nombreDeFarmacia;
        this.direccioDeFarmacia = direccioDeFarmacia;
        this.correoDeFarmacia = correoDeFarmacia;
        this.telefonoDeFarmacia = telefonoDeFarmacia;
        this.propietarioFarmacia = propietarioFarmacia;
        this.rol = rol;
        this.correoPropietario = correoPropietario;
        this.imagenDeFarmacia = imagenDeFarmacia;
    }

    public String getNombreDeFarmacia() {
        return nombreDeFarmacia;
    }

    public void setNombreDeFarmacia(String nombreDeFarmacia) {
        this.nombreDeFarmacia = nombreDeFarmacia;
    }

    public String getDireccioDeFarmacia() {
        return direccioDeFarmacia;
    }

    public void setDireccioDeFarmacia(String direccioDeFarmacia) {
        this.direccioDeFarmacia = direccioDeFarmacia;
    }

    public String getCorreoDeFarmacia() {
        return correoDeFarmacia;
    }

    public void setCorreoDeFarmacia(String correoDeFarmacia) {
        this.correoDeFarmacia = correoDeFarmacia;
    }

    public String getTelefonoDeFarmacia() {
        return telefonoDeFarmacia;
    }

    public void setTelefonoDeFarmacia(String telefonoDeFarmacia) {
        telefonoDeFarmacia = telefonoDeFarmacia;
    }

    public String getPropietarioFarmacia() {
        return propietarioFarmacia;
    }

    public void setPropietarioFarmacia(String propietarioFarmacia) {
        this.propietarioFarmacia = propietarioFarmacia;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
       this.rol = rol;
    }

    public String getIdFarmacia() {
        return idFarmacia;
    }

    public void setIdFarmacia(String idFarmacia) {
        this.idFarmacia = idFarmacia;
    }

    public String getCorreoPropietario() {
        return correoPropietario;
    }

    public void setCorreoPropietario(String correoPropietario) {
        this.correoPropietario = correoPropietario;
    }

    public int getImagenDeFarmacia() {
        return imagenDeFarmacia;
    }

    public void setImagenDeFarmacia(int imagenDeFarmacia) {
        this.imagenDeFarmacia = imagenDeFarmacia;
    }
}
