package com.rrcc.ubifarm01.ClasesDeObjetos;

public class correosHabilitados {

    public String correoFarmacia;
    private String idCorreoFarmacia;

    public correosHabilitados() {
    }

    public correosHabilitados(String correoFarmacia, String idCorreoFarmacia) {
        this.correoFarmacia = correoFarmacia;
        this.idCorreoFarmacia = idCorreoFarmacia;
    }

    public String getCorreoFarmacia() {
        return correoFarmacia;
    }

    public void setCorreoFarmacia(String correoFarmacia) {
        this.correoFarmacia = correoFarmacia;
    }

    public String getIdCorreoFarmacia() {
        return idCorreoFarmacia;
    }

    public void setIdCorreoFarmacia(String idCorreoFarmacia) {
        this.idCorreoFarmacia = idCorreoFarmacia;
    }
}
