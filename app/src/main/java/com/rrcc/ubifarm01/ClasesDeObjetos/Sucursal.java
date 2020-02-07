package com.rrcc.ubifarm01.ClasesDeObjetos;

public class Sucursal {

    private String NombreFarmacia;
    private String FotoFarmacia;
    private String NombreSucursal;
    private String DireccionSucursal;
    private String TelefonoSucursal;
    private String Delegado;
    private String diasMañana;
    private String diasTarde;
    private String horasMañana;
    private String horasTarde;
    private String idSucursal;

    private String lat;
    private String lng;

    public Sucursal() {
    }

    public Sucursal(String nombreFarmacia, String fotoFarmacia, String nombreSucursal, String direccionSucursal,
                    String telefonoSucursal, String delegado, String diasMañana, String diasTarde, String horasMañana,
                    String horasTarde, String idSucursal, String lat, String lng) {
        NombreFarmacia = nombreFarmacia;
        FotoFarmacia = fotoFarmacia;
        NombreSucursal = nombreSucursal;
        DireccionSucursal = direccionSucursal;
        TelefonoSucursal = telefonoSucursal;
        Delegado = delegado;
        this.diasMañana = diasMañana;
        this.diasTarde = diasTarde;
        this.horasMañana = horasMañana;
        this.horasTarde = horasTarde;
        this.idSucursal = idSucursal;
        this.lat = lat;
        this.lng = lng;
    }

    public String getNombreFarmacia() {
        return NombreFarmacia;
    }

    public void setNombreFarmacia(String nombreFarmacia) {
        NombreFarmacia = nombreFarmacia;
    }

    public String getFotoFarmacia() {
        return FotoFarmacia;
    }

    public void setFotoFarmacia(String fotoFarmacia) {
        FotoFarmacia = fotoFarmacia;
    }

    public String getNombreSucursal() {
        return NombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        NombreSucursal = nombreSucursal;
    }

    public String getDireccionSucursal() {
        return DireccionSucursal;
    }

    public void setDireccionSucursal(String direccionSucursal) {
        DireccionSucursal = direccionSucursal;
    }

    public String getTelefonoSucursal() {
        return TelefonoSucursal;
    }

    public void setTelefonoSucursal(String telefonoSucursal) {
        TelefonoSucursal = telefonoSucursal;
    }

    public String getDelegado() {
        return Delegado;
    }

    public void setDelegado(String delegado) {
        Delegado = delegado;
    }

    public String getDiasMañana() {
        return diasMañana;
    }

    public void setDiasMañana(String diasMañana) {
        this.diasMañana = diasMañana;
    }

    public String getDiasTarde() {
        return diasTarde;
    }

    public void setDiasTarde(String diasTarde) {
        this.diasTarde = diasTarde;
    }

    public String getHorasMañana() {
        return horasMañana;
    }

    public void setHorasMañana(String horasMañana) {
        this.horasMañana = horasMañana;
    }

    public String getHorasTarde() {
        return horasTarde;
    }

    public void setHorasTarde(String horasTarde) {
        this.horasTarde = horasTarde;
    }

    public String getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(String idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
