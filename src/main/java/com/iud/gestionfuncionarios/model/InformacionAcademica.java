package com.iud.gestionfuncionarios.model;

public class InformacionAcademica {
    private int id_estudio;         // Clave primaria
    private int Funcionarios_id_funcionario;     // Clave foránea (FK)
    private String universidad;
    private String nivel_estudio;
    private String titulo_estudio;

    // Constructor vacío
    public InformacionAcademica() {
    }

    // Constructor con todos los campos
    public InformacionAcademica(int idEstudio, int Funcionarios_id_funcionario, String universidad, String nivel_estudio, String titulo_estudio) {
        this.id_estudio = id_estudio;
        this.Funcionarios_id_funcionario = Funcionarios_id_funcionario;
        this.universidad = universidad;
        this.nivel_estudio = nivel_estudio;
        this.titulo_estudio = titulo_estudio;
    }

    // Getters y Setters
    public int getIdEstudio() {
        return id_estudio;
    }

    public void setIdEstudio(int idEstudio) {
        this.id_estudio = id_estudio;
    }

    public int getIdFuncionario() {
        return Funcionarios_id_funcionario;
    }

    public void setIdFuncionario(int Funcionarios_id_funcionario) {
        this.Funcionarios_id_funcionario = Funcionarios_id_funcionario;
    }

    public String getUniversidad() {
        return universidad;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    public String getNivelEstudio() {
        return nivel_estudio;
    }

    public void setNivelEstudio(String nivel_estudio) {
        this.nivel_estudio = nivel_estudio;
    }

    public String getTituloEstudio() {
        return titulo_estudio;
    }

    public void setTituloEstudio(String titulo_estudio) {
        this.titulo_estudio = titulo_estudio;
    }

    @Override
    public String toString() {
        return titulo_estudio + " en " + universidad + " (" + nivel_estudio + ")";
    }
}