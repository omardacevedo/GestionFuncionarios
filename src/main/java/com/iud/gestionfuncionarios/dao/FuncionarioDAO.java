package com.iud.gestionfuncionarios.dao;
import com.iud.gestionfuncionarios.model.Funcionario;
import com.iud.gestionfuncionarios.exceptions.DAOException; // Importamos nuestra excepción personalizada

import java.util.List;

// Esta es la INTERFAZ. Define qué operaciones se pueden hacer, pero no cómo.
public interface FuncionarioDAO {

    /**
     * Crea un nuevo funcionario en la base de datos.
     * @param funcionario El objeto Funcionario a crear.
     * @throws DAOException Si ocurre un error al interactuar con la base de datos.
     */
    void create(Funcionario funcionario) throws DAOException;

    /**
     * Lee (obtiene) un funcionario por su ID.
     * @param id El ID del funcionario a buscar.
     * @return El objeto Funcionario encontrado, o null si no existe.
     * @throws DAOException Si ocurre un error al interactuar con la base de datos.
     */
    Funcionario read(int id) throws DAOException
            ;

    /**
     * Actualiza un funcionario existente en la base de datos.
     * @param funcionario El objeto Funcionario con los datos actualizados.
     * @throws DAOException Si ocurre un error al interactuar con la base de datos.
     */
    void update(Funcionario funcionario) throws DAOException;

    /**
     * Elimina un funcionario por su ID.
     * @param id El ID del funcionario a eliminar.
     * @throws DAOException Si ocurre un error al interactuar con la base de datos.
     */
    void delete(int id) throws DAOException;

    /**
     * Lista todos los funcionarios de la base de datos.
     * @return Una lista de todos los objetos Funcionario.
     * @throws DAOException Si ocurre un error al interactuar con la base de datos.
     */
    List<Funcionario> getAll() throws DAOException;

    /**
     * Busca funcionarios por número de identificación.
     * @param numeroIdentificacion El número de identificación a buscar.
     * @return Una lista de funcionarios que coinciden con el número de identificación.
     * @throws DAOException Si ocurre un error al interactuar con la base de datos.
     */
    List<Funcionario> findByNumeroIdentificacion(String numeroIdentificacion) throws DAOException;
}