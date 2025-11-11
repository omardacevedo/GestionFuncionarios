package com.iud.gestionfuncionarios.dao;

import com.iud.gestionfuncionarios.model.GrupoFamiliar;
import com.iud.gestionfuncionarios.exceptions.DAOException;

import java.util.List;

public interface GrupoFamiliarDAO {
    void create(GrupoFamiliar grupoFamiliar) throws DAOException;
    GrupoFamiliar read(int id) throws DAOException;
    void update(GrupoFamiliar grupoFamiliar) throws DAOException;
    void delete(int id) throws DAOException;
    List<GrupoFamiliar> getAll() throws DAOException;
    List<GrupoFamiliar> findByFuncionarioId(int funcionarioId) throws DAOException; // Método específico para buscar por funcionario
}