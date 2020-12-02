package com.daa.jsmsapp.model;

import java.util.Date;
import java.util.Timer;
import java.util.UUID;

public class Appointment {

     UUID id;
     UUID id_cliente;
     UUID id_doctor;
     UUID id_servicio;
     UUID id_hospital;
     UUID id_especialidad;
     String nombre;
     String nombre_doctor;
     String descripcion;
     Integer dia;
     Integer hora;
     Date fecha;
     Timer horario;

    private static final String TAG = Appointment.class.getSimpleName();

    public Appointment(UUID id, UUID id_cliente, UUID id_doctor, UUID id_servicio, UUID id_hospital, UUID id_especialidad, String nombre, String nombre_doctor, String descripcion, Integer dia, Integer hora, Date fecha, Timer horario) {
        this.id = id;
        this.id_cliente = id_cliente;
        this.id_doctor = id_doctor;
        this.id_servicio = id_servicio;
        this.id_hospital = id_hospital;
        this.id_especialidad = id_especialidad;
        this.nombre = nombre;
        this.nombre_doctor = nombre_doctor;
        this.descripcion = descripcion;
        this.dia = dia;
        this.fecha = fecha;
        this.hora = hora;
        this.horario = horario;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(UUID id_cliente) {
        this.id_cliente = id_cliente;
    }

    public UUID getId_doctor() {
        return id_doctor;
    }

    public void setId_doctor(UUID id_doctor) {
        this.id_doctor = id_doctor;
    }

    public UUID getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(UUID id_servicio) {
        this.id_servicio = id_servicio;
    }

    public UUID getId_hospital() {
        return id_hospital;
    }

    public void setId_hospital(UUID id_hospital) {
        this.id_hospital = id_hospital;
    }

    public UUID getId_especialidad() {
        return id_especialidad;
    }

    public void setId_especialidad(UUID id_especialidad) {
        this.id_especialidad = id_especialidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre_doctor() {
        return nombre_doctor;
    }

    public void setNombre_doctor(String nombre_doctor) {
        this.nombre_doctor = nombre_doctor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }


    public void setHorario(Timer horario) {
        this.horario = horario;
    }

    public Timer getHorario(Timer horario) {
        return horario;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public Integer getHora() {
        return hora;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }



}
