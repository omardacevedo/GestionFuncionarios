package com.iud.gestionfuncionarios.model;

import java.util.Date;
import java.util.List; // Importamos List para las relaciones

public class Funcionario {
    // Atributos de la tabla 'funcionarios'
    private int idFuncionario; // Clave primaria
    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private String nombres;
    private String apellidos;
    private String estadoCivil;
    private String sexo;
    private String direccion;
    private String telefono;
    private Date fechaNacimiento;

    // Atributos para las relaciones (para poder cargar la información completa si es necesario)
    private List<GrupoFamiliar> grupoFamiliar;
    private List<InformacionAcademica> informacionAcademica;

    // --- Constructores ---
    // Constructor vacío (necesario para muchas librerías y para crear objetos antes de llenarlos)
    public Funcionario() {
    }

    // Constructor con todos los campos (útil para crear objetos completos)
    public Funcionario(int idFuncionario, String tipoIdentificacion, String numeroIdentificacion, String nombres, String apellidos, String estadoCivil, String sexo, String direccion, String telefono, Date fechaNacimiento) {
        this.idFuncionario = idFuncionario;
        this.tipoIdentificacion = tipoIdentificacion;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.estadoCivil = estadoCivil;
        this.sexo = sexo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
    }

    // --- Getters y Setters ---
    // Getters y Setters para cada atributo. Permiten acceder y modificar los valores de los atributos.

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    // Getters y Setters para las relaciones
    public List<GrupoFamiliar> getGrupoFamiliar() {
        return grupoFamiliar;
    }

    public void setGrupoFamiliar(List<GrupoFamiliar> grupoFamiliar) {
        this.grupoFamiliar = grupoFamiliar;
    }

    public List<InformacionAcademica> getInformacionAcademica() {
        return informacionAcademica;
    }

    public void setInformacionAcademica(List<InformacionAcademica> informacionAcademica) {
        this.informacionAcademica = informacionAcademica;
    }

    // --- Método toString ---
    @Override
    public String toString() {
        return nombres + " " + apellidos + " (" + numeroIdentificacion + ")";
    }
}