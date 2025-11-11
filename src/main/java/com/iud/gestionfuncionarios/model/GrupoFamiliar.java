package com.iud.gestionfuncionarios.model;

public class GrupoFamiliar {
    private int id_miembro_familiar; // Clave primaria
    private int Funcionarios_id_funcionario;     // Clave foránea (FK)
    private String nombre_miembro;
    private String apellido_miembro;
    private String parentesco;

    // Constructor vacío
    public GrupoFamiliar() {
    }

    // Constructor con todos los campos
    public GrupoFamiliar(int id_miembro_familiar, int Funcionarios_id_funcionario, String nombre_miembro, String apellido_miembro, String parentesco) {
        this.id_miembro_familiar = id_miembro_familiar;
        this.Funcionarios_id_funcionario = Funcionarios_id_funcionario;
        this.nombre_miembro = nombre_miembro;
        this.apellido_miembro = apellido_miembro;
        this.parentesco = parentesco;
    }

    // Getters y Setters
    public int getIdMiembroFamiliar() {
        return id_miembro_familiar;
    }

    public void setIdMiembroFamiliar(int id_miembro_familiar) {
        this.id_miembro_familiar = id_miembro_familiar;
    }

    public int getIdFuncionario() {
        return Funcionarios_id_funcionario;
    }

    public void setIdFuncionario(int Funcionarios_id_funcionario) {
        this.Funcionarios_id_funcionario = Funcionarios_id_funcionario;
    }

    public String getNombreMiembro() {
        return nombre_miembro;
    }

    public void setNombreMiembro(String nombre_miembro) {
        this.nombre_miembro = nombre_miembro;
    }

    public String getApellidoMiembro() {
        return apellido_miembro;
    }

    public void setApellidoMiembro(String apellido_miembro) {
        this.apellido_miembro = apellido_miembro;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    
 

    @Override
    public String toString() {
        return nombre_miembro + " " + apellido_miembro + " (" + parentesco + ")";
    }
}