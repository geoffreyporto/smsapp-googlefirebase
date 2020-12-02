package com.daa.jsmsapp.model;

import java.util.Date;
import java.util.UUID;

public class Pacient {

    UUID id;
    UUID id_usuario;
    String nombre;
    String apellido_materno;
    String apellido_paterno;
    Date fecha_nac;
    String email;
    String tel;

    public Pacient(UUID id, UUID id_usuario, String nombre, String apellido_materno, String apellido_paterno, Date fecha_nac, String email, String tel) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido_materno = apellido_materno;
        this.apellido_paterno = apellido_paterno;
        this.fecha_nac = fecha_nac;
        this.email = email;
        this.tel = tel;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId_paciente() {
        return id_usuario;
    }

    public void setId_paciente(UUID id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido_materno() {
        return apellido_materno;
    }

    public void setApellido_materno(String apellido_materno) {
        this.apellido_materno = apellido_materno;
    }

    public String getApellido_paterno() {
        return apellido_paterno;
    }

    public void setApellido_paterno(String apellido_paterno) {
        this.apellido_paterno = apellido_paterno;
    }

    public Date getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(Date fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
