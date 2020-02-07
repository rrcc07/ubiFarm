package com.rrcc.ubifarm01.ClasesDeObjetos;

public class Notificaiones {

    String idNotificacion;
    String nombreFarmaciaSugerida;
    String correoFarmaciaSugerencia;
    String correoUsuario;
    String direccionFarmaciaSugerida;
    String telefonoFarmaciaSugerida;

    public Notificaiones() {
    }

    public Notificaiones(String idNotificacion, String nombreFarmaciaSugerida,
                         String correoFarmaciaSugerencia, String correoUsuario, String direccionFarmaciaSugerida,
                         String telefonoFarmaciaSugerida) {
        this.idNotificacion = idNotificacion;
        nombreFarmaciaSugerida = nombreFarmaciaSugerida;
        this.correoFarmaciaSugerencia = correoFarmaciaSugerencia;
        this.correoUsuario = correoUsuario;
        this.direccionFarmaciaSugerida = direccionFarmaciaSugerida;
        this.telefonoFarmaciaSugerida = telefonoFarmaciaSugerida;
    }

    public String getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(String idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getnombreFarmaciaSugerida() {
        return nombreFarmaciaSugerida;
    }

    public void senombreFarmaciaSugerida(String nombreFarmaciaSugerida) {
        nombreFarmaciaSugerida = nombreFarmaciaSugerida;
    }

    public String getCorreoFarmaciaSugerencia() {
        return correoFarmaciaSugerencia;
    }

    public void setCorreoFarmaciaSugerencia(String correoFarmaciaSugerencia) {
        this.correoFarmaciaSugerencia = correoFarmaciaSugerencia;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getDireccionFarmaciaSugerida() {
        return direccionFarmaciaSugerida;
    }

    public void setDireccionFarmaciaSugerida(String direccionFarmaciaSugerida) {
        this.direccionFarmaciaSugerida = direccionFarmaciaSugerida;
    }

    public String getTelefonoFarmaciaSugerida() {
        return telefonoFarmaciaSugerida;
    }

    public void setTelefonoFarmaciaSugerida(String telefonoFarmaciaSugerida) {
        this.telefonoFarmaciaSugerida = telefonoFarmaciaSugerida;
    }
}
