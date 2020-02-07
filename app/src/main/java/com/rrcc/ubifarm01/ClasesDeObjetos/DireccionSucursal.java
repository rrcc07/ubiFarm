package com.rrcc.ubifarm01.ClasesDeObjetos;

public class DireccionSucursal {

    String lat;
    String lng;
    String nombreFarmacia;
    String nombreSucursal;
    String direccionEscrita;
    String dirKey;

    public DireccionSucursal() {
    }

    public DireccionSucursal(String lat, String lng, String nombreFarmacia, String nombreSucursal,String direccionEscrita) {
        this.lat = lat;
        this.lng = lng;
        this.nombreFarmacia = nombreFarmacia;
        this.nombreSucursal = nombreSucursal;
        this.direccionEscrita = direccionEscrita;
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

    public String getDirKey() {
        return dirKey;
    }

    public void setDirKey(String dirKey) {
        this.dirKey = dirKey;
    }

    public String getDireccionEscrita() {
        return direccionEscrita;
    }

    public void setDireccionEscrita(String direccionEscrita) {
        this.direccionEscrita = direccionEscrita;
    }

    public String getNombreFarmacia() {
        return nombreFarmacia;
    }

    public void setNombreFarmacia(String nombreFarmacia) {
        this.nombreFarmacia = nombreFarmacia;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }
}

