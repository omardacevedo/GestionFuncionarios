package com.iud.gestionfuncionarios.dao;

import com.iud.gestionfuncionarios.model.InformacionAcademica;
import com.iud.gestionfuncionarios.exceptions.DAOException;

import java.util.List;

public interface InformacionAcademicaDAO {
    void create(InformacionAcademica infoAcademica) throws DAOException;
    InformacionAcademica read(int id) throws DAOException;
    void update(InformacionAcademica infoAcademica) throws DAOException;
    void delete(int id) throws DAOException;
    List<InformacionAcademica> getAll() throws DAOException;
    List<InformacionAcademica> findByFuncionarioId(int funcionarioId) throws DAOException; // Método específico para buscar por funcionario
}